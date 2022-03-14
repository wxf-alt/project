package sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}

// 2.sparkStreaming 调用 updateStateByKey 更新上次数据
// 需要设置 checkpoint 才能获取之前批次的数据
object SparkStreamingSocket2UpdateStateByKey {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreamingSocket2")
    val ssc: StreamingContext = new StreamingContext(conf, Durations.seconds(2))

    ssc.checkpoint("C:\\Users\\wxf\\Desktop\\4.测试数据\\检查点\\SparkStreamingSocket2")

    val socketStream: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 6666, StorageLevel.MEMORY_ONLY)
    val reduceByKeyDS: DStream[(String, Int)] = socketStream.transform(rdd => {
      val reduce: RDD[(String, Int)] = rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
      reduce
    })

    // a -> 当前批次数据    b -> 上次数据
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
      println(s"count time:${t}, ${r.sortBy(_._2,false).collect().toList}")
    })

    ssc.start()
    ssc.awaitTermination()
  }

}
