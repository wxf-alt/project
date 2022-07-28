//package spark_stream.integrate_kafka0_8
//
//import kafka.common.TopicAndPartition
//import kafka.serializer.StringDecoder
//import org.apache.spark.SparkConf
//import org.apache.spark.streaming.dstream.{DStream, InputDStream}
//import org.apache.spark.streaming.kafka.KafkaUtils
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//
//// SaprkStream 连接 kafka
//object SparkStreamKafkaDemo1 {
//  def main(args: Array[String]): Unit = {
//    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("SparkStreamKafkaDemo1")
//    val ssc: StreamingContext = new StreamingContext(conf,Seconds(3))
//
//    val kafkaParams: Map[String, String] = Map[String, String](
//      "bootstrap.servers" -> "nn1.hadoop:9092,nn2.hadoop:9092,s1.hadoop:9092",
//      "group.id" -> "idea_spark_kafka "
////      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "nn1.hadoop:9092,nn2.hadoop:9092,s1.hadoop:9092",
////      ConsumerConfig.GROUP_ID_CONFIG -> "idea_spark_kafka "
//    )
//    val sourceStream: DStream[(String, Int)] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
//      ssc,
//      kafkaParams,
//      Set("kafakFirst01")
//    ).flatMap(_._2.split(" ")).map((_,1)).reduceByKey(_ + _)
//
//    sourceStream.print()
//
//    ssc.start()
//    ssc.awaitTermination()
//  }
//}
