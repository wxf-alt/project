package window

import bean.SensorReading
import org.apache.flink.api.common.ExecutionConfig
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.{AssignerWithPeriodicWatermarks, AssignerWithPunctuatedWatermarks}
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * @Auther: wxf
 * @Date: 2022/6/21 14:47:00
 * @Description: WaterMarkTest
 * @Version 1.0.0
 */
object WaterMarkTest {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 设置并行度为 1
    env.setParallelism(1)

    // 设置时间语义 为 EventTime
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    // 设置生成 WaterMark 的周期
    // 每隔100毫秒产生一个watermark
    env.getConfig.setAutoWatermarkInterval(100L)

    val inputStream: DataStream[SensorReading] = env.socketTextStream("localhost", 6666)
      .map(s => {
        val str: Array[String] = s.split(" ")
        SensorReading(str(0), str(1).toLong, str(2).toDouble)
      })

    // 升序数据可以使用；如果乱序数据的话需要使用 下面的方法
    inputStream.assignAscendingTimestamps(_.timestamp * 1000)

    // 设置 WaterMark   周期性生成一个 WaterMark
    inputStream.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[SensorReading](Time.seconds(2)) {
      // 从元素中获取 时间戳字段
      override def extractTimestamp(element: SensorReading): Long = element.timestamp * 1000
    })
    // 使用自定义的WaterAssigner
    inputStream.assignTimestampsAndWatermarks(new MyWMAssigner(2000L))
  }
}


// 自定义一个 周期性 生成 WaterMark的Assigner
class MyWMAssigner(lateTime:Long) extends AssignerWithPeriodicWatermarks[SensorReading]{
  // 需要两个关键参数 1.当前的延迟时间 2.当前数据中最大的时间戳
  val lateness:Long = lateTime
  var maxTs : Long = Long.MinValue + lateness

  // 获取当前的 WM
  override def getCurrentWatermark: Watermark = {
    val watermark: Watermark = new Watermark(maxTs - lateness)
    watermark
  }

  // 提取当前数据中的时间戳
  override def extractTimestamp(element: SensorReading, previousElementTimestamp: Long): Long = {
    maxTs = Math.max(maxTs, element.timestamp * 1000L)
    element.timestamp * 1000L
  }

}

// 自定义一个 断点式 生成 WaterMark的Assigner
class MyWMAssigner2(lateTime:Long) extends AssignerWithPunctuatedWatermarks[SensorReading]{

  // 每来一条数据 会同时调用下面两个方法
  override def checkAndGetNextWatermark(lastElement: SensorReading, extractedTimestamp: Long): Watermark = {
    // 只有在 sensor1 数据来时,才会生成新的 WM
    if (lastElement.id == "sendor_1") {
      new Watermark(extractedTimestamp - lateTime)
    }else {
      null
    }
  }
  override def extractTimestamp(element: SensorReading, previousElementTimestamp: Long): Long = {
    element.timestamp * 1000L
  }

}


class MyPeriodicAssigner(lateTime:Long) extends AssignerWithPeriodicWatermarks[SensorReading]{

  // 需要两个关键参数 1.当前的延迟时间 2.当前数据中最大的时间戳
  val lateness:Long = lateTime
  var maxTs : Long = Long.MinValue + lateness

  // 通过 数据流中最大时间戳 - 延迟时间 定义 WaterMark 触发窗口操作
  override def getCurrentWatermark: Watermark = new Watermark(maxTs - lateness)

  // 指定数据流中的 时间戳字段
  override def extractTimestamp(element: SensorReading, previousElementTimestamp: Long): Long = {
    // 更新数据流中最大时间戳
    maxTs = Math.max(element.timestamp * 1000L,maxTs)
    element.timestamp * 1000L
  }

}

class MyPunctuatedAssigner(lateTime:Long) extends AssignerWithPunctuatedWatermarks[SensorReading]{
  // 每来一条数据 会同时调用下面两个方法

  // 获取下一次 WaterMark
  override def checkAndGetNextWatermark(lastElement: SensorReading, extractedTimestamp: Long): Watermark = {
    // 判断 数据是否为 sensor_1
    if (lastElement.id == "sensor_1") {
      new Watermark(extractedTimestamp - lateTime)
    }else{
      null
    }
  }

  // 指定时间戳 字段
  override def extractTimestamp(element: SensorReading, previousElementTimestamp: Long): Long = element.timestamp * 1000L

}
