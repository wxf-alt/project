package sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}

// 1.socket 创建DStream
object SparkStreamingSocket1 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("SparkStreamingSocket")
    val ssc: StreamingContext = new StreamingContext(conf,Durations.seconds(5))

    val socketStream: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 6666, StorageLevel.MEMORY_ONLY)
//    val word_count_Stream: DStream[(String, Int)] = socketStream.transform(rdd => {
//      val result: RDD[(String, Int)] = rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
//      result
//    })
//
//    word_count_Stream.foreachRDD((r,t) =>{
//      println(s"count time:${t}, ${r.collect().toList}")
////      r.foreach(println)
//    })

    socketStream.foreachRDD((rdd,t) =>{
      val reduceByKey: RDD[(String, Int)] = rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
      println(s"count time:${t}, ${reduceByKey.collect().toList}")
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
