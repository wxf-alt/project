package spark_streaming.streaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.{PairRDDFunctions, RDD}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, Seconds, StreamingContext}

object SparkStreamingSocket {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingSocket").setMaster("local[2]")
    val ssc: StreamingContext = new StreamingContext(conf, Durations.seconds(5))
    // 该计算方式的缓存默认级别：StorageLevel.MEMORY_AND_DISK_SER_2
    // 从socket端接收一行数据，数据是按照空格分隔的
    val inputDS: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 6666)

//    // DStream --> DStream 方式
//    val flatMapDS: DStream[String] = inputDS.flatMap(_.split(" "))
//    val pairDS: DStream[(String, Int)] = flatMapDS.map((_,1))
//    val reduceByKeyDS: DStream[(String, Int)] = pairDS.reduceByKey(_ + _)
//    reduceByKeyDS.foreachRDD((r,t) =>{
//      println(s"count time:${t}, ${r.collect().toList}")
//    })

//    // transform 方式
//    val reduceByKeyDS: DStream[(String, Int)] = inputDS.transform(rdd => {
//      val reduceByKey: RDD[(String, Int)] = rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
//      reduceByKey
//    })
//    reduceByKeyDS.foreachRDD((r,t) =>{
//      println(s"count time:${t}, ${r.collect().toList}")
//    })

    ssc.remember(Seconds(15))

    // foreachRDD 方式
    inputDS.foreachRDD((rdd,t) =>{
      val reduceByKey: RDD[(String, Int)] = rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
      println(s"count time:${t}, ${reduceByKey.collect().toList}")
    })


    ssc.start()
    ssc.awaitTermination()

  }
}
