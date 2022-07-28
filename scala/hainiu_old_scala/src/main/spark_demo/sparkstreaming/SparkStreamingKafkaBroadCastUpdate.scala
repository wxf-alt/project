package sparkstreaming

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
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.apache.spark.util.LongAccumulator

import scala.collection.mutable
import scala.collection.mutable.Map
import scala.collection.mutable.ListBuffer

// SparkStreaming-kafka高级开发
/*
需求：
    1、SparkStreaming多DirectStream方式去对接kaka数据源
    2、更新广播变量替代cogroup  fileStream达到更改配置的目的
    3、累加器重置
*/
object SparkStreamingKafkaBroadCastUpdate {
  def main(args: Array[String]): Unit = {

    //这里设置cpu cores为1的时候也能运行，说明KafkaUtils.createDirectStream是不需要receiver占用一个cpu cores的
    //而KafkaUtils.createStream是需要的，这种模式是出现在sparkStreaming-kafka-0.80版本的，0.10.x版本已经抛弃了
    //这个的cpu为什么设置为25，因为对于流式计算来说，应该快速完成每个任务的运算，所以在任务生成的时候每个task都应有对应的cpu，不让其等待，这样运算速度才会更快
    //再者由于是本地环境，所以这个25个cpu中有一个cpu是为了driver设置的，在实现的分布式环境中设置executor为24，就是kafka中topic的partition数据就可以了，然后driver单独设置
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingKafka").setMaster("local[25]")

    //设置blockInterval来调整并发数，也就是spark的RDD分区数，也就是task的数量
    //这个配置对于StreamingContext创建带receiver的流是起作用的，比如socket或者KafkaUtils.createStream
    //对于KafkaUtils.createDirectStream（kafka的直连模式）创建的流是不起作用的
    //因为直连模式是根据topic的分区数来决定并发度的，也就是task会直接连接到kafka中的topic的partition上
    //所以这里设置blockInterval对RDD的partition的划分是不起作用的
    //conf.set("spark.streaming.blockInterval","1000ms")
    val ssc = new StreamingContext(conf, Durations.seconds(5))

    // 创建 kafaka连接流
    val topic = "hainiu_test"

    // 设置 broker
    val brokers = "s1.hadoop:9092,s2.hadoop:9092,s3.hadoop:9092,s4.hadoop:9092,s5.hadoop:9092,s6.hadoop:9092,s7.hadoop:9092,s8.hadoop:9092"

    // 设置kafka需要的参数
    val kafkaParams: mutable.HashMap[String, Object] = new mutable.HashMap[String, Object]()
    kafkaParams += "bootstrap.servers" -> brokers
    kafkaParams += "group.id" -> "group15"
    //这两个的deserializer是sparkStreaming做为consumer时使用的
    kafkaParams += "key.deserializer" -> classOf[StringDeserializer].getName
    kafkaParams += "value.deserializer" -> classOf[StringDeserializer].getName
    //这两个的serializer是sparkStreaming做为producer时使用的
    //    kafkaParams += "key.serializer" -> classOf[StringSerializer].getName
    //    kafkaParams += "value.serializer" -> classOf[StringSerializer].getName

/*
    // 定义 ofset
    val offsets: mutable.HashMap[TopicPartition, Long] = new mutable.HashMap[TopicPartition, Long]()
    //因为使用了分配模式，所以这里指定的partition是不起作用的，但是这个1的作用是指定读取offset的位置
    offsets += new TopicPartition(topic,0) -> 1
    // 创建 kafka流
    val value: ConsumerStrategy[String, String] = ConsumerStrategies.Subscribe[String, String](topic.split(",").toList, kafkaParams, offsets)
    val directDStreamList: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent, value)
*/

    //这里包含了3个DStream
    val directDStreamList: ListBuffer[InputDStream[ConsumerRecord[String, String]]] = new ListBuffer[InputDStream[ConsumerRecord[String, String]]]
    //这里的作用是让每个DStream指定负责那些partition
    //这里使用的分配的方式，让每个DStream读取指定的partition
    //因为咱们的topic有24个partition，那咱们这里有3个DStream，之后每个DStream配置成读取8个Partition
    //但是对于直连流来讲RDD的Partition是被动由Kafka的Topic的Partition来决定的，所以也无法改变接受速度
    for (i <- 0 until 3) {
      val partitions = new ListBuffer[TopicPartition]
      for (ii <- (8 * i) until (8 * i) + 8) {
        val partition = new TopicPartition(topic, ii)
        partitions += partition
      }
      val value: ConsumerStrategy[String, String] = ConsumerStrategies.Assign(partitions, kafkaParams)
      val directDStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent, value)
      directDStreamList += directDStream
    }
    // 将流合并起来
    val unionDStream: DStream[ConsumerRecord[String, String]] = ssc.union(directDStreamList)
    val words: DStream[String] = unionDStream.flatMap(_.value().split(" "))


    // 设置 广播变量
    var mapBroadCast: Broadcast[Map[String, String]] = ssc.sparkContext.broadcast(Map[String, String]())
    //配置更新广播变量数据间隔的时间
    val updateInterval = 10000L
    //用于存储最后一次更新的时候
    var lastUpdateTime = 0L
    //声明两个累加器，用于每批次数据的累加统计，而不是历史上所有的批次，所以这两个累加器在每批次使用完成之后要进行重置
    val matchAcc: LongAccumulator = ssc.sparkContext.longAccumulator
    val noMatchAcc: LongAccumulator = ssc.sparkContext.longAccumulator

    words.foreachRDD(r => {

   // ==> 读取数据文件 封装到广播变量中 代替文件流 进行 cogroup
      //第一次启动的时候先进行广播变量的更新操作，以后的每次更新操作是根据时间的间隔来进行的
      //这里的代码是在Driver端进行广播变量重建的
      // 如果 广播变量的值为空 或者 已经超过 配置更新广播变量数据间隔的时间 会进入 if 中
      if (mapBroadCast.value.isEmpty || System.currentTimeMillis() - lastUpdateTime >= updateInterval) {
        val map: mutable.Map[String, String] = mutable.Map[String,String]()
        val dictFilePath = "/Users/leohe/Data/input/updateSparkBroadCast"
        val fs: FileSystem = FileSystem.get(new Configuration())
        val fileStatuses: Array[FileStatus] = fs.listStatus(new Path(dictFilePath))
        for(f <- fileStatuses){
          val filePath: Path = f.getPath
          val stream: FSDataInputStream = fs.open(filePath)
          val reader = new BufferedReader(new InputStreamReader(stream))
          var line: String = reader.readLine()
          while (line != null){
            val strings: Array[String] = line.split("\t")
            val code: String = strings(0)
            val countryName: String = strings(1)
            map += code -> countryName
            line = reader.readLine()
          }
        }
        //手动取消广播变量的持久化
        mapBroadCast.unpersist()
        //重新创建广播变量  -> 更新值
        mapBroadCast = ssc.sparkContext.broadcast(map)
        //修改最后一次的更新时间
        lastUpdateTime = System.currentTimeMillis()
      }
      println(s"broadCast:${mapBroadCast.value}")

      // 对 kafka 中的数据进行逻辑处理
      if(!r.isEmpty()){
        r.foreachPartition(it => {
          //这里的代码是属于RDD算子中的function的，所以函数中的代码是在集群运行的
          val cast: mutable.Map[String, String] = mapBroadCast.value
          it.foreach(f => {
            println(s"kakfa:${f}")
            //scala中国continue的写法
            import scala.util.control.Breaks._
            breakable {
              if(f == null){
                break()
              }
              if(cast.contains(f)){
                matchAcc.add(1L)
              }else{
                noMatchAcc.add(1L)
              }
            }
          })
        })

        //统计每个批次的累加器结果
        val matchA: Long = matchAcc.count
        val noMatchA: Long = noMatchAcc.count

        //这里是使用每个批次累加器的值，比如可以保存到mysql中，用于流式计算的统计，最后可以实时的进行报表的展示
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
