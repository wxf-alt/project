package spark_streaming.kafka_streaming

import java.io.{BufferedReader, InputStreamReader}

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataInputStream, FileStatus, FileSystem, Path}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, ConsumerStrategy, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.util.LongAccumulator

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks

object SparkStreamingKafkaBroadCastUpdate {
  def main(args: Array[String]): Unit = {

    // 定义 topic
    val topic: String = "stream_kafka1"
    // 定义 brokers
    val brokers: String = "s2.hadoop:9092,s3.hadoop:9092,s4.hadoop:9092"
    // 定义 消费者组
    val groupId: String = "group_idea"

    // 这里设置cpu cores为1的时候也能运行，说明KafkaUtils.createDirectStream是不需要receiver占用一个cpu cores的
    // 而KafkaUtils.createStream是需要的，这种模式是出现在sparkStreaming-kafka-0.80版本的，0.10.x版本已经抛弃了
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreamingKafkaBroadCastUpdate")
    // 设置blockInterval来调整并发数，也就是spark的RDD分区数，也就是task的数量
    // 这个配置对于StreamingContext创建带receiver的流是起作用的，比如socket或者KafkaUtils.createStream
    // 对于KafkaUtils.createDirectStream（kafka的直连模式）创建的流是不起作用的
    // 因为直连模式是根据topic的分区数来决定并发度的，也就是task会直接连接到kafka中的topic的partition上
    // 所以这里设置blockInterval对RDD的partition的划分是不起作用的
    // conf.set("spark.streaming.blockInterval","1000ms")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(2))

    // kafka 配置
//    val topics: Set[String] = topic.split(",").toSet

    val kafkaParams: mutable.HashMap[String, Object] = new mutable.HashMap[String, Object]()
    kafkaParams += "bootstrap.servers" -> brokers
    kafkaParams += "group.id" -> groupId
    // 这两个的deserializer是sparkStreaming做为consumer时使用的
    kafkaParams += "key.deserializer" -> classOf[StringDeserializer].getName
    kafkaParams += "value.deserializer" -> classOf[StringDeserializer].getName

    val offsets: mutable.HashMap[TopicPartition, Long] = new mutable.HashMap[TopicPartition, Long]()
    offsets += new TopicPartition(topic, 1) -> 0

    //这里包含了3个DStream
    var directDStreamList: ListBuffer[InputDStream[ConsumerRecord[String, String]]] = new ListBuffer[InputDStream[ConsumerRecord[String, String]]]

    // 这里的作用是让每个DStream指定负责那些partition
    // 这里使用的分配的方式，让每个DStream读取指定的partition
    // topic有4个partition，那这里有2个DStream，之后每个DStream配置成读取2个Partition
    // 但是对于直连流来讲，RDD的Partition是被动由Kafka的Topic的Partition来决定的，所以也无法改变接受速度
    for (i <- 0 until (2)) {
      val partitions: ListBuffer[TopicPartition] = new ListBuffer[TopicPartition]
      for (ii <- (2 * i) until (2 * i) + 2) {
        val partition: TopicPartition = new TopicPartition(topic, ii)
        partitions += partition
      }
      val value: ConsumerStrategy[String, String] = ConsumerStrategies.Assign(partitions, kafkaParams)
      val directDStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(ssc,
        LocationStrategies.PreferConsistent,
        value)
      directDStreamList += directDStream
    }

    // 合并流
    val unionDStream: DStream[ConsumerRecord[String, String]] = ssc.union(directDStreamList)
    // 解析
    val words: DStream[String] = unionDStream.flatMap(_.value().split(" "))

    // 创建 广播变量
    var mapBroadCast: Broadcast[Map[String, String]] = ssc.sparkContext.broadcast(Map[String, String]())

    // 配置更新广播变量数据间隔的时间
    val updateInterval: Long = 10000L
    // 用于存储最后一次更新的时候
    var lastUpdateTime: Long = 0L
    // 声明两个累加器，用于每批次数据的累加统计，而不是历史上所有的批次，所以这两个累加器在每批次使用完成之后要进行重置
    val matchAcc: LongAccumulator = ssc.sparkContext.longAccumulator
    val noMatchAcc: LongAccumulator = ssc.sparkContext.longAccumulator

    words.foreachRDD(r => {
      // 第一次启动的时候先进行广播变量的更新操作，以后的每次更新操作是根据时间的间隔来进行的
      // 这里的代码是在Driver端进行广播变量重建的
      // 广播变量是空的 并且 达到更新时间
      if(mapBroadCast.value.isEmpty || System.currentTimeMillis() - lastUpdateTime >= updateInterval){
        var map: Map[String, String] = Map[String, String]()
        val dictFilePath: String = """E:\A_data\4.测试数据\spark-streaming数据"""
        val hadoopConf: Configuration = new Configuration()
        hadoopConf.set("fs.defaultFS", "file:///")
        val fs: FileSystem = FileSystem.get(hadoopConf)
        val fileStatuses: Array[FileStatus] = fs.listStatus(new Path(dictFilePath))
        for (elem <- fileStatuses) {
          val filePath: Path = elem.getPath
          val stream: FSDataInputStream = fs.open(filePath)
          val reader: InputStreamReader = new InputStreamReader(stream)
          val bufferedReader: BufferedReader = new BufferedReader(reader)
          var line: String = bufferedReader.readLine()
          while(line != null){
            val strings: Array[String] = line.split("\t")
            val code: String = strings(0)
            val countryName: String = strings(1)
            map += code -> countryName
            line = bufferedReader.readLine()
          }
        }
        // 手动取消 广播变量持久化
        mapBroadCast.unpersist()
        // 重新创建广播变量
        mapBroadCast = ssc.sparkContext.broadcast(map)
        // 修改 更新时间
        lastUpdateTime = System.currentTimeMillis()
      }
//      println(s"broadCast: ${mapBroadCast.value}")
//      println("kafka 数据：" + r.collect.toList)

      // 处理 kafka 中数据
      if (!r.isEmpty()) {
        r.foreachPartition(it => {
          // 拿出 广播变量中的数据
          val cast: Map[String, String] = mapBroadCast.value
          it.foreach(f => {
            Breaks.breakable{
              if (f == null) {
                Breaks.break()
              }
              if(cast.contains(f)){
                val value: String = cast(f)
                println(s"key -> ${f};value -> ${value}")
                matchAcc.add(1L)
              }else{
                noMatchAcc.add(1L)
              }
            }
          })
        })
        // 统计每个批次的累加器结果
        val matchA: Long = matchAcc.count
        val noMatchA: Long = noMatchAcc.count

        // 这里是使用每个批次累加器的值，比如可以保存到mysql中，用于流式计算的统计，最后可以实时的进行报表的展示
        println(s"match:${matchA},noMatch:${noMatchA}")

        //累加器清0
        matchAcc.reset()
        noMatchAcc.reset()
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }
}