package spark_streaming.streaming

import org.apache.hadoop.conf.Configuration
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreamingGetOrCreate {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingGetOrCreate").setMaster("local[2]")
    val hadoopConf: Configuration = new Configuration()
    hadoopConf.set("fs.defaultFS", "file:///")

    val checkPoint: String = """E:\A_data\4.测试数据\检查点\SparkStreamingUpdateStateByKey1"""

    def creatFunc: () => StreamingContext = {
      () => {
        val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))
        ssc.sparkContext.hadoopConfiguration.set("fs.defaultFS", "file:///")
        ssc.checkpoint(checkPoint)
        val inputDS: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 6666)
        val flatMapDS: DStream[String] = inputDS.flatMap(_.split(" "))
        val pairDS: DStream[(String, Int)] = flatMapDS.map((_, 1))
        val reduceByKeyDS: DStream[(String, Int)] = pairDS.reduceByKey(_ + _)

        val updateByKeyDS: DStream[(String, Int)] = reduceByKeyDS.updateStateByKey((a: Seq[Int], b: Option[Int]) => {
          var sum: Int = 0
          for (i <- a) {
            sum += i
          }
          // 上一批次结果
          val last: Int = if (b.isDefined) b.get else 0
          // 本批次 + 上一批次 = 最新批次结果
          Some(sum + last)
        })

        updateByKeyDS.foreachRDD((r, t) => {
          println(s"count time:${t}, ${r.collect().toList}")
        })
        ssc
      }
    }

    val sscNew: StreamingContext = StreamingContext.getOrCreate(checkPoint, creatFunc,hadoopConf)

    sscNew.start()
    sscNew.awaitTermination()
  }
}