package spark_streaming.kafka

import java.util.Properties

import kafka.consumer.{Consumer, ConsumerConfig, ConsumerConnector, ConsumerIterator, KafkaStream}
import kafka.message.MessageAndMetadata
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import scala.actors.Actor
import scala.collection.mutable
import scala.collection.mutable.ListBuffer


// kafka 生产数据
class HainiuProducer1 extends Actor{

  // 属性
  var producer:KafkaProducer[String,String] = _
  var topic:String = _

  // 辅助构造器  初始化属性
  def this(topic:String){
    // 调用主构造
    this()
    // 初始化 生产者 producer
    this.topic = topic
    val properties: Properties = new Properties()
    properties.put("bootstrap.servers","s2.hadoop:9092,s3.hadoop:9092,s4.hadoop:9092")
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.put("value.serializer",classOf[StringSerializer].getName)
    this.producer = new KafkaProducer[String,String](properties)
  }

  override def act(): Unit = {
    // 等待 消费者启动
    Thread.sleep(8000)
    var num:Int = 1
    while(true){
      val messAgeStr: String = s"hainiu_${num}"
      println(s"生产者 发送 --> send:${messAgeStr}" )
      // 发送到 Kafka
      this.producer.send(new ProducerRecord[String,String](this.topic,messAgeStr))
      num += 1
      if(num > 10){
        num = 0
      }
      // 2 秒发送一次数据
      Thread.sleep(3000)
    }
  }

}


//noinspection DuplicatedCode
// kafka 消费数据
//class HainiuConsumer extends Actor{
class HainiuConsumer1 extends Actor {

  // 属性
  var consumer:ConsumerConnector = _
  var topic:String = _
  var prop: Properties = _

  def this(topic:String){
    this()
    this.topic = topic
//    val prop: Properties = new Properties()
    prop = new Properties()
    prop.setProperty("zookeeper.connect", "nn1.hadoop:2181,nn2.hadoop:2181,s1.hadoop:2181")
//    prop.put("bootstrap.servers","s2.hadoop:9092,s3.hadoop:9092,s4.hadoop:9092")
    prop.put("group.id", "group3")
//    prop.put("zookeeper.session.timeout.ms", "600000")
    // 初始化消费者
    this.consumer = Consumer.create(new ConsumerConfig(prop))
  }

  // 消费数据
  override def act(): Unit = {
    // 两个consumer线程 消费 kafka topic 的2个分区的数据
    val topicCountMap: mutable.HashMap[String, Int] = new mutable.HashMap[String, Int]()
    topicCountMap += ((topic, 2))

    val createMessageStreams: collection.Map[String, List[KafkaStream[Array[Byte], Array[Byte]]]] = this.consumer.createMessageStreams(topicCountMap)
    val stream: List[KafkaStream[Array[Byte], Array[Byte]]] = createMessageStreams.get(topic).get

    for (elem <- stream) {
      new Thread(new Runnable {
        override def run(): Unit = {
          val it: ConsumerIterator[Array[Byte], Array[Byte]] = elem.iterator
          while(it.hasNext()){
            val next: MessageAndMetadata[Array[Byte], Array[Byte]] = it.next()
            val list: ListBuffer[Any] = ListBuffer[Any]()
            val msg: Array[Byte] = next.message()
            val topicName: String = next.topic
            val partitionId: Int = next.partition
            val offset: Long = next.offset
            val threadName: String = Thread.currentThread().getName
            list.append(threadName)
            list.append(new String(msg))
            list.append(topicName)
            list.append(partitionId)
            list.append(offset)
            println(s"receive: ${list}")
            Thread.sleep(1)
          }
        }
      }).start()
    }
  }

}

object KafkaTest1 {
  def main(args: Array[String]): Unit = {
    val topic:String = "hainiu_test1"
    val producer: HainiuProducer1 = new HainiuProducer1(topic)
    val consumer: HainiuConsumer1 = new HainiuConsumer1(topic)
    consumer.start()
    producer.start()
  }
}
