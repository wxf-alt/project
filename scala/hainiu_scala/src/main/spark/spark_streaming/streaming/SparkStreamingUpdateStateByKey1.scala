package spark_streaming.streaming

import org.apache.hadoop.conf.Configuration
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}

object SparkStreamingUpdateStateByKey1 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingUpdateStateByKey1").setMaster("local[2]")
    val ssc: StreamingContext = new StreamingContext(conf, Durations.seconds(5))
    val configuration: Configuration = ssc.sparkContext.hadoopConfiguration
    configuration.set("fs.defaultFS", "file:///")

    val name: String = ssc.sparkContext.appName
    val checkPoint: String = s"E:\\A_data\\4.测试数据\\检查点\\${name}"
    ssc.checkpoint(checkPoint)

    val inputDs: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 6666)
    val reduceByKeyDs: DStream[(String, Int)] = inputDs.transform(rdd => {
      val value: RDD[(String, Int)] = rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
      value
    })

    val updateByKeyDS: DStream[(String, Int)] = reduceByKeyDs.updateStateByKey((a: Seq[Int], b: Option[Int]) => {
      var sum: Int = 0
      for (elem <- a) {
        sum += elem
      }
      // 上一个批次的结果
      val last: Int = b.getOrElse(0)
      // 本批次 + 上一个批次 = 所有值
      Some(sum + last)
    })

    updateByKeyDS.foreachRDD((r, t) => {
      println(s"count time:${t}, ${r.collect().toList}")
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
