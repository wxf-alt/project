package spark_stream.transform

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

// UpadteStateByKey 算子
object SparkSqlUpadteStateByKeyDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("SparkSqlUpadteStateByKeyDemo")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))
    val checkPointPath: String = "E:\\A_data\\4.测试数据\\检查点\\SparkSqlUpadteStateByKeyDemo"
    val inputStream: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 6666)

    ssc.checkpoint(checkPointPath)
    val result: DStream[(String, Int)] = inputStream.flatMap(_.split(" "))
      .map((_, 1))
      .updateStateByKey((a: Seq[Int], b: Option[Int]) => {
        Some(a.sum + b.getOrElse(0))
      })

    result.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
