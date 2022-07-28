package spark_stream.transform

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

//noinspection DuplicatedCode
// Window 算子
object SparkSqlWindowDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("SparkSqlWindowDemo")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))

    val inputStream: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 6666)
    //    val windowDStream: DStream[String] = inputStream.window(Seconds(12), Seconds(3))
    //    val result: DStream[(String, Int)] = windowDStream
    //      .flatMap(_.split(" "))
    //      .map((_, 1))
    //      .reduceByKey(_ + _)

    val result: DStream[(String, Int)] = inputStream.flatMap(_.split(" ")).map((_, 1))
      // 默认滑动步长 = 批次时间长度
      // 窗口长度 和 滑动步长 必须是 批次时间长度的整数倍
//      .reduceByKeyAndWindow(_ + _, Seconds(6))
      // 优化
      .reduceByKeyAndWindow(_ + _, _ - _, Seconds(6))

    result.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
