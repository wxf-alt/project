//package spark_stream.integrate_kafka0_8
//
//import kafka.serializer.StringDecoder
//import org.apache.kafka.clients.consumer.ConsumerConfig
//import org.apache.spark.SparkConf
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//import org.apache.spark.streaming.dstream.DStream
//import org.apache.spark.streaming.kafka.KafkaUtils
//
//object SparkStreamKafkaDemo2 {
//  def createSSC(): StreamingContext = {
//    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("SparkStreamKafkaDemo2")
//    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))
//    ssc.checkpoint("ck1")
//    val kafkaParams: Map[String, String] = Map[String, String](
//      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "nn1.hadoop:9092,nn2.hadoop:9092,s1.hadoop:9092",
//      ConsumerConfig.GROUP_ID_CONFIG -> "idea_spark_kafka "
//    )
//    val sourceStream: DStream[(String, Int)] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
//      ssc,
//      kafkaParams,
//      Set("kafakFirst01")
//    ).flatMap(_._2.split(" ")).map((_, 1)).reduceByKey(_ + _)
//    sourceStream.print()
//    ssc
//  }
//
//  def main(args: Array[String]): Unit = {
//    // 从checkpoint 中恢复一个 StreamingContext
//    // 如果 ck 不存在,则使用后面的函数创建 StreamingContext
//    val ssc: StreamingContext = StreamingContext.getActiveOrCreate("ck1", createSSC)
//    ssc.start()
//    ssc.awaitTermination()
//  }
//}
