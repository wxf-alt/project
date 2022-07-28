//package spark_stream.integrate_kafka0_10
//
//import java.lang
//
//import org.apache.kafka.clients.consumer.ConsumerRecord
//import org.apache.kafka.common.serialization.StringDeserializer
//import org.apache.spark.SparkConf
//import org.apache.spark.streaming.dstream.InputDStream
//import org.apache.spark.streaming.kafka010.{ConsumerStrategies, ConsumerStrategy, KafkaUtils, LocationStrategies}
//import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}
//
//object SparkStreamKafkaDemo1 {
//  def main(args: Array[String]): Unit = {
//    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("SparkStreamKafkaDemo1")
//    val ssc: StreamingContext = new StreamingContext(conf,Seconds(5))
//
//    val kafkaParams: Map[String, Object] = Map[String, Object](
//      "bootstrap.servers" -> "nn1.hadoop:9092,nn2.hadoop:9092,s1.hadoop:9092",
//      "key.deserializer" -> classOf[StringDeserializer],
//      "value.deserializer" -> classOf[StringDeserializer],
//      "group.id" -> "idea_kafka",
//      "auto.offset.reset" -> "latest",
//      "enable.auto.commit" -> (true: lang.Boolean)
//    )
//    val topics: Array[String] = Array("ads_log")
//    val inputStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](
//      ssc,
//      LocationStrategies.PreferConsistent,
//      ConsumerStrategies.Subscribe(topics, kafkaParams))
//  }
//}
