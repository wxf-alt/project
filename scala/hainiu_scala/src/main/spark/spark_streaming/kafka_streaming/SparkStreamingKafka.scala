package spark_streaming.kafka_streaming

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, ConsumerStrategy, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import scala.collection.mutable

object SparkStreamingKafka {
  def main(args: Array[String]): Unit = {

    // 这里设置为 local[1]也可以运行spark任务，那就说明，DireceStream是不需要Rceiver的
    val conf: SparkConf = new SparkConf().setMaster("local[1]").setAppName("StreamKafkaReadWrite")
    val ssc: StreamingContext = new StreamingContext(conf,Seconds(5))

    // 定义 topic
    val topic:String = "hainiu_test"
    // 定义 brokers
    val brokers:String = "s2.hadoop:9092,s3.hadoop:9092,s4.hadoop:9092"
    // 定义 消费者组
    val groupId:String = "group1"

    // kafka 消费者配置
    val kafkaParams: mutable.HashMap[String, Object] = new mutable.HashMap[String, Object]()
    kafkaParams += "bootstrap.servers" -> brokers
    kafkaParams += "group.id" -> groupId
    // 使用String的反序列化器，作用是将Kafka中的二进制 反序列化变成 字符串，这个反序列化也能自己实现
    kafkaParams += "key.deserializer" -> classOf[StringDeserializer].getName
    kafkaParams += "value.deserializer" -> classOf[StringDeserializer].getName
    // consumer 读取的策略
    // earliest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费；
    // latest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据；
    // none：topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常；
    kafkaParams += "auto.offset.reset" -> "earliest"
    // 是否自动提交offset
    kafkaParams += "enable.auto.commit" -> "true"

    // topicPartition 和 offset 配置
    val offset: mutable.HashMap[TopicPartition, Long] = new mutable.HashMap[TopicPartition, Long]
    // 因为使用了分配模式,所以这里指定的partition是不起作用的。会读取所有的分区数据
    // 每一次都是 从零开始
    offset += new TopicPartition(topic,0) -> 0


    // 使用了 订阅模式去读取topic，然后读取topic的每一个partition的数据
    val consumerStrategies: ConsumerStrategy[String, String] = ConsumerStrategies.Subscribe[String, String](topic.split(",").toSet, kafkaParams, offset)
    // 创建 kafka的直连流，指定使用的任务分配模式为 均衡分配分布到所有的executor中 ; 消费数据模式采用订阅模式
    val kafkaInputStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent, consumerStrategies)

    kafkaInputStream.foreachRDD(rdd => {
      val value: RDD[String] = rdd.flatMap(_.value().split(" "))
      value.foreach(println)
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
