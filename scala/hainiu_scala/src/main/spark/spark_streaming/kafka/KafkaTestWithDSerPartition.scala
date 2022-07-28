package spark_streaming.kafka

import java.util
import java.util.Properties

import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, Partitioner, ProducerRecord}
import org.apache.kafka.common.{Cluster, TopicPartition}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import spark_streaming.kafka.serializerBean.{KafkaDSerializer, KafkaSerializer}

import scala.actors.Actor


// 自定义分区类
// 根据写入数据的数字，偶数0分区，奇数1分区
class MyPartitioner extends Partitioner{

  override def partition(topic: String, key: Any, keyBytes: Array[Byte], value: Any, valueBytes: Array[Byte], cluster: Cluster): Int = {
    val data: KafkaData = value.asInstanceOf[KafkaData]
    // 获取属性值
    val string: String = data.str
    val num: Int = string.split("_")(1).toInt
    if(num % 2 == 0) 0 else 1
  }

  override def close(): Unit = {}

  override def configure(configs: util.Map[String, _]): Unit = {}

}


class KafkaProducerWithDSerPartition extends Actor {

  var producer: KafkaProducer[String, KafkaData] = _
  var topic: String = _

  // 利用辅助构造器初始化数据
  def this(topic: String) {
    this()
    this.topic = topic
    // 根据配置初始化 KafkaProducer
    val props: Properties = new Properties()
    props.put("bootstrap.servers", "s2.hadoop:9092,s3.hadoop:9092,s4.hadoop:9092")
    props.put("key.serializer", classOf[StringSerializer].getName)
    // 设置 序列化类为自定义 HainiuKafkaSerializer
    props.put("value.serializer", classOf[KafkaSerializer].getName)
    props.put("partitioner.class", classOf[MyPartitioner].getName)
    this.producer = new KafkaProducer[String, KafkaData](props)
  }

  override def act(): Unit = {
    // 目的是能消费到第一个发送过来的数据
    Thread.sleep(6000)
    var num: Int = 1;
    while (true) {
      val messageStr: String = new String(s"hainiu_${num}");
      val data: KafkaData = KafkaData(messageStr)
      println(s"send:${data}");
      // 发送到kafka
      this.producer.send(new ProducerRecord[String, KafkaData](this.topic, data));
      num += 1;
      if (num > 10) {
        num = 0;
      }
      Thread.sleep(3000)
    }
  }

}

class KafkaConsumerWithDSerPartition extends Actor {

  var consumer: KafkaConsumer[String, KafkaData] = _
  var topic: String = _

  def this(topic: String) {
    this()
    this.topic = topic
    val prop: Properties = new Properties()
    // 配置 kafka 消费者
    // 高级 api,直接读取 broker
    prop.put("bootstrap.servers", "s2.hadoop:9092,s3.hadoop:9092,s4.hadoop:9092")
    // 每个消费者指定自己属于哪个 消费者组
    prop.put("group.id", "group101")
    // consumer offset读取策略
    // earliest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费；
    // latest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据；
    // none：topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常；
    prop.put("auto.offset.reset", "earliest")
    // 是否自动提交offset
    prop.put("enable.auto.commit", "true")
    // 多长时间提交一次
    prop.put("auto.commit.interval.ms", "1000")
    // 使用自定义的序列化工具把二进制转成 KafkaData
    prop.put("key.deserializer", classOf[StringDeserializer].getName)
    prop.put("value.deserializer", classOf[KafkaDSerializer].getName)

    // 初始化 消费者
    this.consumer = new KafkaConsumer[String, KafkaData](prop)

    // 设置 kafka 消费方式

    // 指定consumer读取topic中的那一个partition，这个叫做分配方式
    this.consumer.assign(java.util.Arrays.asList(new TopicPartition(topic, 0)))
//        val topicPartition: TopicPartition = new TopicPartition(topic, 1)
    //    val list: List[TopicPartition] = List[TopicPartition](topicPartition)
    //    import scala.collection.JavaConversions._
    //    this.consumer.assign(list)

    // 指定consumer读取topic中所有的partition，这个叫做订阅方式
    //    this.consumer.subscribe(java.util.Arrays.asList(topic))
  }

  override def act(): Unit = {
    while (true) {
      // 消费数据
      val value: ConsumerRecords[String, KafkaData] = this.consumer.poll(100)
      val record: util.Iterator[ConsumerRecord[String, KafkaData]] = value.iterator()
      while (record.hasNext) {
        val r: ConsumerRecord[String, KafkaData] = record.next()
        val partitionId: Int = r.partition()
        val data: KafkaData = r.value()
        val offset: Long = r.offset()
        val dataTopic: String = r.topic()
        println(s"msg:${data},topic:${dataTopic},partition:${partitionId},offset:${offset}")
      }
    }
  }

}


object KafkaTestWithDSerPartition {
  def main(args: Array[String]): Unit = {
    val topic: String = "hainiu_test2"
    val kafkaProducerWithDSerPartition: KafkaProducerWithDSerPartition = new KafkaProducerWithDSerPartition(topic)
    val kafkaConsumerWithDSerPartition: KafkaConsumerWithDSerPartition = new KafkaConsumerWithDSerPartition(topic)
    kafkaProducerWithDSerPartition.start()
    kafkaConsumerWithDSerPartition.start()
  }
}
