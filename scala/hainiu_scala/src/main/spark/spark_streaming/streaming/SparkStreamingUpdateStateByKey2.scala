package spark_streaming.streaming

import org.apache.hadoop.conf.Configuration
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

//定义一个对象用于保存key对应的值，和值的状态
case class StreamingStateValue(var value: Int, var isUpdate: Boolean = false)

object SparkStreamingUpdateStateByKey2 {
  def main(args: Array[String]): Unit = {

    val hadoopConf: Configuration = new Configuration()
    hadoopConf.set("fs.defaultFS", "file:///")

    val checkPoint: String = """E:\A_data\4.测试数据\检查点\SparkStreamingUpdateStateByKey2"""

    def creatFunc: () => StreamingContext = {
      () => {
        val conf: SparkConf = new SparkConf().setAppName("SparkStreamingSocketPort").setMaster("local[2]")
        val ssc: StreamingContext = new StreamingContext(conf, Seconds(2))
        ssc.sparkContext.hadoopConfiguration.set("fs.defaultFS", "file:///")
        ssc.checkpoint(checkPoint)

        val inputDS: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 6666)
        val flatMapDS: DStream[String] = inputDS.flatMap(_.split(" "))
        val pairDS: DStream[(String, StreamingStateValue)] = flatMapDS.map((_, StreamingStateValue(1)))
        val reduceByKeyDS: DStream[(String, StreamingStateValue)] = pairDS.reduceByKey((a, b) => StreamingStateValue(a.value + b.value))
        val updateByKeyDS: DStream[(String, StreamingStateValue)] = reduceByKeyDS.updateStateByKey((a: Seq[StreamingStateValue], b: Option[StreamingStateValue]) => {
          //这个参数a是本批次的数据，b是这个key上次的历史结果
          var total: Int = 0
          for (elem <- a) {
            total += elem.value
          }
          //这里为什么要判断一下，因为这个key有可能是第一次进入，也就是说没有历史数据，那此时应该给个初始值0
          //          val last: StreamingStateValue = b.getOrElse(StreamingStateValue(0))
          val last: StreamingStateValue = if (b.isDefined) b.get else StreamingStateValue(0)
          if (a.size != 0) {
            last.isUpdate = true
          } else {
            last.isUpdate = false
          }
          val now: Int = last.value + total
          last.value = now
          //使用这个修改了状态的对象StreamingStateValue做为本批次这个key的结果
          Some(last)
        })

        // 在有checkpoint数据的时候，修改算子中的function是有效的
        // 所以使用了checkpoint恢复streamingContext的程序，这样的程序并不影响你后续的升级代码
        updateByKeyDS.foreachRDD((r, t) => {
          if (!r.isEmpty()) {
            println(s"count time:${t},${r.collect().toList}")
            // 过滤状态为true，代表本批次有更新的数据，这样的话比如插入数据库就不会每次都全量插入
            // 只插入每批次有变化的数据就可以了
            val filter: RDD[(String, StreamingStateValue)] = r.filter(x => x._2.isUpdate)
            println(s"count time:${t},filter data:${filter.collect().toList}")
          }
        })
        ssc
      }
    }

    //使用getOrCreate从checkPoint里恢复最后一次的StreamingContext状态，如果没有checkPoint地址，那就新建一个StreamingContext
    val sscNew: StreamingContext = StreamingContext.getOrCreate(checkPoint, creatFunc, hadoopConf)
    //这里必须得使用getOrCreate判断好（要么是历史的，要么是新create的）的那个streamingContext进行启动
    sscNew.start()
    sscNew.awaitTermination()
  }
}
