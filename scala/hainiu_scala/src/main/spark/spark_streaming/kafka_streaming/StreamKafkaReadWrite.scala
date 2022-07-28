package spark_streaming.kafka_streaming

import java.util.Properties

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, ConsumerStrategy, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object StreamKafkaReadWrite {
  def main(args: Array[String]): Unit = {

    val topic: String = "stream_kafka"
    // 我们使用的是 sparkStreaming-kafka-010的直连模式
    // 每一个task 都直接连接到topic的partition中
    // 直连模式 ： spark的task任务数 与 topic的partition数一致
    val brokers: String = "s2.hadoop:9092,s3.hadoop:9092,s4.hadoop:9092"
    val groupId: String = "group_idea"

    val conf: SparkConf = new SparkConf().setMaster("local[1]").setAppName("StreamKafkaReadWrite")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(2))

    val topics: Set[String] = topic.split(",").toSet

    val kafakParams: mutable.HashMap[String, Object] = new mutable.HashMap[String, Object]()
    kafakParams += "bootstrap.servers" -> brokers
    kafakParams += "group.id" -> groupId
    // 使用 String的反序列化器，作用是将Kafka中的二进制 反序列化变成 字符串，这个反序列化也能自己实现
    kafakParams += "key.deserializer" -> classOf[StringDeserializer].getName
    kafakParams += "value.deserializer" -> classOf[StringDeserializer].getName
    // consumer offset读取策略
    // earliest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费；
    // latest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据；
    // none：topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常；
    kafakParams += "auto.offset.reset" -> "earliest"
    // 是否自动提交offset
    kafakParams += "enable.auto.commit" -> "true"

    val offsets: mutable.HashMap[TopicPartition, Long] = new mutable.HashMap[TopicPartition, Long]()
    // 因为使用了订阅模式,所以这里指定的partition是不起作用的，
    // 后面这个 0 是指定读取 offset 的位置
    offsets += (new TopicPartition(topic, 0) -> 1)

    val kafkaStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topics, kafakParams, offsets))

    val reduceByKeyDS: DStream[(String, Int)] = kafkaStream.flatMap(_.value().split(" ")).map((_, 1)).reduceByKey(_ + _)

    reduceByKeyDS.foreachRDD((rdd,time) => {
      println(s"time:${time},${rdd.collect().toList}")

      rdd.foreachPartition(it => {
        val prop: Properties = new Properties()
        prop.put("bootstrap.servers",brokers)
        prop.put("key.serializer", classOf[StringSerializer].getName)
        prop.put("value.serializer",classOf[StringSerializer].getName)
        val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](prop)
        it.foreach{
          case (x,y) => {
            producer.send(new ProducerRecord[String,String]("hainiu_test",x + "-->" + y))
          }
        }
      })

    })

    ssc.start()
    ssc.awaitTermination()
  }
}