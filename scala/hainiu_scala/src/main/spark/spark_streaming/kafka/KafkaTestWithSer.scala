package spark_streaming.kafka

import java.io.{ByteArrayInputStream, ObjectInputStream}
import java.util.Properties

import kafka.consumer.{Consumer, ConsumerConfig, ConsumerConnector, ConsumerIterator, KafkaStream}
import kafka.message.MessageAndMetadata
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import spark_streaming.kafka.serializerBean.KafkaSerializer

import scala.actors.Actor
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks


// 发送到kafka 的对象数据
case class KafkaData(val str:String)

class KafkaProducerWithSer extends Actor {

  // 属性
  var kafkaProducer:KafkaProducer[String,KafkaData] = _
  var topic:String = _

  def this(topic:String){
    this()
    this.topic = topic
    val properties: Properties = new Properties()
    properties.put("bootstrap.servers", "s2.hadoop:9092,s3.hadoop:9092,s4.hadoop:9092")
    properties.put("key.serializer", classOf[StringSerializer].getName)
    properties.put("value.serializer", classOf[KafkaSerializer].getName)
    this.kafkaProducer = new KafkaProducer[String,KafkaData](properties)
  }

  override def act(): Unit = {
    // 目的是能消费到第一个发送过来的数据
    Thread.sleep(6000)
    var num : Int = 1;
    while (true) {
      val messageStr: String = new String(s"hainiu_${num}");
      val data: KafkaData = KafkaData(messageStr)
      println(s"send:${data}");
      // 发送到kafka
      this.kafkaProducer.send(new ProducerRecord[String, KafkaData](this.topic, data));
      num += 1;
      if (num > 10) {
        num = 0;
      }
      Thread.sleep(3000)
    }
  }

}

class KafkaConsumerWithSer extends Actor{

  var consumer:ConsumerConnector = _
  var topic:String = _

  def this(topic:String){
    this()
    this.topic = topic
    var pro: Properties = new Properties();
    pro.put("zookeeper.connect", "nn1.hadoop:2181,nn2.hadoop:2181,s1.hadoop:2181");
    pro.put("group.id", "group1");
    pro.put("zookeeper.session.timeout.ms", "60000");
    this.consumer = Consumer.create(new ConsumerConfig(pro))
  }

  override def act(): Unit = {
    var topicCountMap: mutable.Map[String, Int] = new mutable.HashMap[String,Int]()
    topicCountMap += ((topic, 1))
    val messageStreams: collection.Map[String, List[KafkaStream[Array[Byte], Array[Byte]]]] = this.consumer.createMessageStreams(topicCountMap)
    val stream: KafkaStream[Array[Byte], Array[Byte]] = messageStreams.get(topic).get(0)
    val it: ConsumerIterator[Array[Byte], Array[Byte]] = stream.iterator()
    while(it.hasNext()){
      Breaks.breakable{
        val value: MessageAndMetadata[Array[Byte], Array[Byte]] = it.next()
        val bytes: Array[Byte] = value.message()
        if (bytes == null) {
          Breaks.break()
        }
        val list: ListBuffer[Any] = ListBuffer[Any]()
        val topicName: String = value.topic
        val partitionId: Int = value.partition
        val offset: Long = value.offset
        // 反序列化 数据 封装成 KafkaData对象
        val bis: ByteArrayInputStream = new ByteArrayInputStream(bytes)
        val ois: ObjectInputStream = new ObjectInputStream(bis)
        val obj: AnyRef = ois.readObject()
        bis.close()
        ois.close()
        val data: KafkaData = obj.asInstanceOf[KafkaData]

        list.append(data)
        list.append(topicName)
        list.append(partitionId)
        list.append(offset)
        println(s"receive: ${list}")
        Thread.sleep(1)
      }
    }
  }

}

object KafkaTestWithSer {
  def main(args: Array[String]): Unit = {
    val topic: String = "hainiu_test1"
    val producer: KafkaProducerWithSer = new KafkaProducerWithSer(topic)
    val consumer: KafkaConsumerWithSer = new KafkaConsumerWithSer(topic)
    producer.start()
    consumer.start()
  }
}
