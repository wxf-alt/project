package spark_stream.transform

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

// transform 算子
object SparkSqlTransformDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("SparkSqlTransformDemo")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))

    val inputStream: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 6666)
    val result: DStream[(String, Int)] = inputStream.transform(rdd => {
      rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
    })
    result.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
