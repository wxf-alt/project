package sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}


// 定义一个对象用于保存key对应的值，和值的状态
case class StreamingStateValue(var value: Int, var isUpdate: Boolean = false) {
  override def toString: String = {
    "(value：" + value + "\t isUpdate：" + isUpdate + ")"
  }
}

// 4.updateStateByKey只使用最近更新的值
// 使用到 checkpoint 和 updateStateByKey 和 getOrCreate
// 需要使用到 自定义类 进行判定输入的值和值的状态
object SparkStreamingSocketPortUpdateStateObject {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreamingSocketPortUpdateStateObject")
    // 设置检查点
    val checkpointPath: String = "C:\\Users\\wxf\\Desktop\\4.测试数据\\检查点\\SparkStreamingSocketPortUpdateStateObject"


    // 将一段代码抽取出方法的快捷键：alt + shift + m  选择第二个
    def UpdateStateObject: () => StreamingContext = {
      () => {
        val sc: StreamingContext = new StreamingContext(conf, Durations.seconds(5))
        sc.checkpoint(checkpointPath)

        val inputStream: ReceiverInputDStream[String] = sc.socketTextStream("localhost", 6666)
        val reduceByKetStream: DStream[(String, StreamingStateValue)] = inputStream.flatMap(_.split(" ")).map((_, StreamingStateValue(1))).reduceByKey((a, b) => {
          StreamingStateValue(a.value + b.value)
        })

        val updateStateByKeyStream: DStream[(String, StreamingStateValue)] = reduceByKetStream.updateStateByKey((a: Seq[StreamingStateValue], b: Option[StreamingStateValue]) => {
          var sum = 0
          for (i <- a) {
            sum += i.value
          }
          //这里为什么要判断一下，因为这个key有可能是第一次进入，也就是说没有历史数据，那此时应该给个初始值0
          val last: StreamingStateValue = if (b.isDefined) b.get else StreamingStateValue(0)
          //如果本批次key有更新的话，那这个key对应的StreamingStateValue对象的状态改为true
          if (a.size != 0) {
            // 有更新
            last.isUpdate = true
          } else {
            last.isUpdate = false
          }
          // 最终返回的值
          val now: Int = last.value + sum
          // 将之前的值进行保存
          last.value = now
          // 返回
          Some(last)
        })

        //在有checkpoint数据的时候，修改算子中的function是有效的
        //所以使用了checkpoint恢复streamingContext的程序，这样的程序并不影响你后续的升级代码
        updateStateByKeyStream.foreachRDD((r, t) => {
          //可以用rdd的isEmpty方法，判断此批次是否有数据，如果有数据再执行
          if (!r.isEmpty()) {
            //            println(s"count time:${t},${r.collect().toList}")
            //            println("============================================")
            //            //过滤状态为true，代表本批次有更新的数据，这样的话比如插入数据库就不会每次都全量插入
            //            //只插入每批次有变化的数据就可以了
            //            val filter: RDD[(String, StreamingStateValue)] = r.filter(_._2.isUpdate)
            //            println(s"count time:${t},filter data:${filter.collect().toList}")
            val filterRdd: RDD[(String, StreamingStateValue)] = r.filter(_._2.isUpdate)
            val update_result: RDD[(String, Int)] = filterRdd.map(f => (f._1, f._2.value))
            println("最近更新的值" + update_result.collect().toList)
          }
        })
        // 返回一个 StreamingContext
        sc
      }
    }

    // 需要写在方法的下面
    // 调用 getOrCreate 创建 StreamingContext
    val sc: StreamingContext = StreamingContext.getOrCreate(checkpointPath, UpdateStateObject)

    sc.start()
    sc.awaitTermination()


  }
}
