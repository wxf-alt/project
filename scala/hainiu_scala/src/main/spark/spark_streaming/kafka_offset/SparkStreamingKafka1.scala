package spark_streaming.kafka_offset

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, ConsumerStrategy, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Durations, StreamingContext}

import scala.collection.mutable

// spark-streaming-kafka sparkStreaming 连接 kafka 取数据(使用直连模式)
// https://www.cnblogs.com/xupccc/p/9545693.html
object SparkStreamingKafka1 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingKafka1").setMaster("local[*]")
    val ssc: StreamingContext = new StreamingContext(conf, Durations.seconds(5))

    // 定义 topic
    val topic:String = "hainiu_test"
    //咱们使用的是sparkStreaming-kafka-010的直连模式，所以每个task都直接连接到topic的partition中
    //咱们使用直连模式时，spark的task任务数与topic的partition数一致
    val brokers:String = "nn1.hadoop:9092,nn2.hadoop:9092,s1.hadoop:9092"
    val kafkaParams: mutable.HashMap[String, Object] = new mutable.HashMap[String, Object]()
    kafkaParams += "bootstrap.servers" -> brokers
    kafkaParams += "group.id" -> "group15"
    //使用String的反序列化器，作用是将kafka中的二进制反序列化成字符串，这个反序列化器也能自己实现
    kafkaParams += "key.deserializer" -> classOf[StringDeserializer].getName
    kafkaParams += "value.deserializer" -> classOf[StringDeserializer].getName

    // 定义 ofset
    val offsets: mutable.HashMap[TopicPartition, Long] = new mutable.HashMap[TopicPartition, Long]()
    //因为使用了分配模式，所以这里指定的partition是不起作用的，但是这个1的作用是指定读取offset的位置
    offsets += new TopicPartition(topic,0) -> 1

    //使用了订阅模式去读取topic，然后读取topic的每个partition数据
    val Consumer: ConsumerStrategy[String,String] = ConsumerStrategies.Subscribe[String,String](topic.split(",").toSet, kafkaParams, offsets)
    //创建kafka的直连流，指定使用的任务分配模式为均衡分布到所有的executor中
    val lines: InputDStream[ConsumerRecord[String,String]] = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent, Consumer)

    val reduceByKey: DStream[(String, Int)] = lines.flatMap(_.value().split(" ")).map((_,1)).reduceByKey(_ + _)

    reduceByKey.foreachRDD((r,t) => {
      println(s"count time:${t},${r.collect().toList}")
    })

    ssc.start()
    ssc.awaitTermination()

  }
}
