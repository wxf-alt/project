package spark_streaming.kafka

import java.util.Properties

import kafka.consumer.{Consumer, ConsumerConfig, ConsumerConnector, ConsumerIterator, KafkaStream}
import kafka.message.MessageAndMetadata
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

import scala.actors.Actor
import scala.collection.mutable.ListBuffer

class HainiuProducerBak(val topic: String) extends Actor {

  var producer: KafkaProducer[String, String] = _

  //初始化producer需要指定brokder的地址
  def init: HainiuProducerBak = {
    val pro: Properties = new Properties()
    pro.put("bootstrap.servers", "s2.hadoop:9092,s3.hadoop:9092,s4.hadoop:9092")
    //这个序列化器的作用是将String转成二进制
    pro.put("key.serializer", classOf[StringSerializer].getName)
    pro.put("value.serializer", classOf[StringSerializer].getName)
    this.producer = new KafkaProducer[String, String](pro)
    this
  }

  override def act(): Unit = {
    var num: Int = 1
    //这个是sleep是因为防止producer比consumer启动快，导致consumer启动的时候少读一个数据
    Thread.sleep(3000)
    while (true) {
      val message: String = s"hainiu_${num}"
      println(s"send:${message}")
      this.producer.send(new ProducerRecord[String, String](this.topic, message))
      num += 1
      if (num > 10) num = 0
      Thread.sleep(3000)
    }
  }
}

object HainiuProducerBak {
  def apply(topic: String): HainiuProducerBak = new HainiuProducerBak(topic).init
}

class HainiuCounsumerBak(val topic: String) extends Actor {

  var consumer: ConsumerConnector = _

  def init: HainiuCounsumerBak = {
    val pro: Properties = new Properties()
    pro.put("zookeeper.connect", "nn1.hadoop:2181,nn2.hadoop:2181,s1.hadoop:2181")
    //每个consumer在消费数据的时候指定自己属于那个consumerGroup
    pro.put("group.id", "group111")
    pro.put("zookeeper.session.timeout.ms", "60000")

    this.consumer = Consumer.create(new ConsumerConfig(pro))
    this
  }

  override def act(): Unit = {
    //这个map里的value指定的消费者线程数
    val createMessageStream: collection.Map[String, List[KafkaStream[Array[Byte], Array[Byte]]]] = consumer.createMessageStreams(Map(topic -> 1))
    val kafkaStream: KafkaStream[Array[Byte], Array[Byte]] = createMessageStream.get(topic).get(0)
    val iterator: ConsumerIterator[Array[Byte], Array[Byte]] = kafkaStream.iterator()
    //得到的数据是一个二进制，那说明kafka中存的是二进制，那对象变成二进制也可能存里
    while (iterator.hasNext()) {
      val next: MessageAndMetadata[Array[Byte], Array[Byte]] = iterator.next()
      val list: ListBuffer[Any] = ListBuffer[Any]()
      val msg: Array[Byte] = next.message()
      val topicName: String = next.topic
      val partitionId: Int = next.partition
      val offset: Long = next.offset
      list.append(new String(msg))
      list.append(topicName)
      list.append(partitionId)
      list.append(offset)

      println(s"receive: ${list}")
      Thread.sleep(1)
    }
  }
}

object HainiuCounsumerBak {
  def apply(topic: String): HainiuCounsumerBak = new HainiuCounsumerBak(topic).init
}

object KafkaTestBak {
  def main(args: Array[String]): Unit = {
    val topic: String = "hainiu_test1"
    HainiuProducerBak(topic).start()
    HainiuCounsumerBak(topic).start()
  }
}
