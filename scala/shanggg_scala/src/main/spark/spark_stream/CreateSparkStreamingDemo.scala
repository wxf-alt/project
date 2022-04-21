package spark_stream

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

//noinspection SpellCheckingInspection
// 测试 socket 输入数据
object CreateSparkStreamingDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("CreateSparkStreamingDemo")

    // 1.创建 StreamingContext
    // new Duration(毫秒值)
//    val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))
    val ssc: StreamingContext = new StreamingContext(conf,new Duration(2000))
    // 2.创建 soctket流
    val dStream1: ReceiverInputDStream[String] =
      ssc.socketTextStream("localhost", 6666)
    // 3.转换处理
    val wordCountDStream: DStream[(String, Int)] = dStream1
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
    // 4.行动算子 将结果打印到控制台
    wordCountDStream.print()
    // 5.启动 StreamingContext
    ssc.start()
    // 6.阻止主线程退出
    ssc.awaitTermination()
  }
}
