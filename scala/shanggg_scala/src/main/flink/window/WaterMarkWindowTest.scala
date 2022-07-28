package window

import bean.SensorReading
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.aggregation.AggregationFunction.AggregationType
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.{EventTimeSessionWindows, TumblingEventTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

import scala.collection.mutable

/**
 * @Auther: wxf
 * @Date: 2022/6/30 20:21:45
 * @Description: WaterMarkWindowTest
 * @Version 1.0.0
 */
object WaterMarkWindowTest {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 设置并行度为 1
    env.setParallelism(1)

    // 设置时间语义 为 EventTime
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    // 设置 周期生成 WaterMark  100毫秒
    env.getConfig.setAutoWatermarkInterval(100L)

    // 定义 source
    val inputStream: DataStream[SensorReading] = env.socketTextStream("localhost", 6666)
      .map(s => {
        val str: Array[String] = s.split(" ")
        SensorReading(str(0), str(1).toLong, str(2).toDouble)
      })

    // 设置 WaterMark  延迟 500毫秒
    val textWithEventTimeDstream: DataStream[SensorReading] = inputStream.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[SensorReading](Time.milliseconds(500L)) {
      override def extractTimestamp(element: SensorReading) = element.timestamp * 1000L
    })

    // 根据key 进行分组
    val keyByStream: KeyedStream[SensorReading, Tuple] = textWithEventTimeDstream.keyBy("id")

    // 设置滚动窗口
    val windowStream: WindowedStream[SensorReading, Tuple, TimeWindow] = keyByStream.window(TumblingEventTimeWindows.of(Time.seconds(3)))
//    // 设置滑动窗口
//    val windowStream: WindowedStream[SensorReading, Tuple, TimeWindow] = keyByStream.timeWindow(Time.seconds(6), Time.seconds(2))
//    // 设置 会话窗口
//    val windowStream: WindowedStream[SensorReading, Tuple, TimeWindow] = keyByStream.window(EventTimeSessionWindows.withGap(Time.seconds(3)))

    val foldStream: DataStream[mutable.HashSet[Long]] = windowStream.fold(new mutable.HashSet[Long]()) {
      case (set, key) =>
        set += key.timestamp
    }

    foldStream.print("window::::").setParallelism(1)

    // 自定义 aggregateFunction
    windowStream.aggregate(new AggregateFunction[SensorReading,Int,Int] {
      // 初始化累加器
      override def createAccumulator() = 0

      override def add(value: SensorReading, accumulator: Int) = accumulator + (value.temperature.toInt)

      override def getResult(accumulator: Int) = accumulator

      override def merge(a: Int, b: Int) = a + b
    })

    env.execute("WaterMarkWindowTest")

  }
}
