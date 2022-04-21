package spark_stream

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

import scala.collection.mutable

// 测试 RDD 队列 输入数据
object CreateSparkStreamingDemo2 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("CreateSparkStreamingDemo2")

    // 1.创建 StreamingContext
    val ssc: StreamingContext = new StreamingContext(conf,Seconds(3))
    val rdds: mutable.Queue[RDD[Int]] = mutable.Queue[RDD[Int]]()
    val sourceStream: InputDStream[Int] = ssc.queueStream(rdds)
    val result: DStream[Int] = sourceStream.reduce(_ + _)
    result.print()

    ssc.start()
    val sc: SparkContext = ssc.sparkContext
    // 向队列插入数据
    while (1 == 1){
      rdds.enqueue(sc.parallelize(1 to 100))
      Thread.sleep(2000)
    }
    ssc.awaitTermination()
  }
}
