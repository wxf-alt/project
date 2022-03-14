package sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

// 3.streaming用checkpoint恢复历史数据
//  --》通过StreamingContext.getOrCreate()
object SparkStreamingSocket3GetOrCreate {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreamingSocket3")
    val checkpointPath: String = ("C:\\Users\\wxf\\Desktop\\4.测试数据\\检查点\\SparkStreamingSocket2")

    // 将一段代码抽取出方法的快捷键：alt + shift + m  选择第二个
    def createFunc: () => StreamingContext = {
      () => {
        val ssc: StreamingContext = new StreamingContext(conf, Durations.seconds(10))

        ssc.checkpoint(checkpointPath)

        val socketStream: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 6666)
        val reduceByKeyDS: DStream[(String, Int)] = socketStream.transform(rdd => {
          val reduce: RDD[(String, Int)] = rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
          reduce
        })

        // a -> 当前批次数据    b -> 上次数据
        val updateByKeyDS: DStream[(String, Int)] = reduceByKeyDS.updateStateByKey((a, b) => {
          var sum: Int = 0
          for (i <- a) {
            sum += i
          }
          // 上一批次结果
          val last: Int = if (b.isDefined) b.get else 0
          // 本次 + 上一次 = 最新批次
          Some(sum + last)
        })
        updateByKeyDS.foreachRDD((r, t) => {
          println(s"count time:${t}, ${r.sortBy(_._2, false).collect().toList}")
        })
        ssc
      }
    }

    val ssc: StreamingContext = StreamingContext.getOrCreate(checkpointPath, createFunc)
    ssc.start()
    ssc.awaitTermination()

  }
}
