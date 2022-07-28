package window

import bean.SensorReading
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.{ProcessWindowFunction, WindowFunction}
import org.apache.flink.streaming.api.windowing.assigners._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

/**
 * @Auther: wxf
 * @Date: 2022/6/17 11:24:49
 * @Description: FlinkWindowTest
 * @Version 1.0.0
 */
object FlinkWindowTest {
  def main(args: Array[String]): Unit = {
    val path: String = this.getClass.getClassLoader.getResource("sensor.txt").getPath
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val inputStream: DataStream[String] = env.socketTextStream("localhost", 6666)
    val transform: DataStream[SensorReading] = inputStream.map(s => {
      val str: Array[String] = s.split(" ")
      SensorReading(str(0), str(1).toLong, str(2).toDouble)
    })
    val keyedStream: KeyedStream[SensorReading, Tuple] = transform.keyBy("id")

    // 设置一个10s的 滚动窗口
    keyedStream.window(TumblingEventTimeWindows.of(Time.seconds(10)))
    keyedStream.window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
    // 设置一个15s的 滑动窗口，步长为 5 滑动一次
    keyedStream.window(SlidingProcessingTimeWindows.of(Time.seconds(15), Time.seconds(5)))
    keyedStream.window(SlidingEventTimeWindows.of(Time.seconds(15), Time.seconds(5)))
    // 设置一个10s 的会话窗口  没有简写方式 只能通过 window() 创建
    keyedStream.window(EventTimeSessionWindows.withGap(Time.seconds(10)))
    keyedStream.window(ProcessingTimeSessionWindows.withGap(Time.seconds(10)))

    val windowStream: WindowedStream[SensorReading, Tuple, TimeWindow] = keyedStream.timeWindow(Time.seconds(10))
    val applyStream: DataStream[Int] = windowStream.apply(new MyWindowFunction())
    val processStream: DataStream[Int] = windowStream.process(new MyProcessWindowFunction())

    // flink 默认的时间语义： ProcessingTime
    val characteristic: TimeCharacteristic = env.getStreamTimeCharacteristic
    println(characteristic)

    // 根据默认的 时间语义 创建窗口  可以通过  env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime) 设置不同时间语义
    // 创建时间窗口   默认的时间语义： ProcessingTime
    // 创建 滚动时间窗口  不能定义 offset
    keyedStream.timeWindow(Time.seconds(10))
    // 创建 滑动时间窗口 不能定义 offset
    keyedStream.timeWindow(Time.seconds(10), Time.seconds(5))

    // 创建 计数滚动窗口
    keyedStream.countWindow(10)
    // 创建 计数滑动窗口
    keyedStream.countWindow(10, 5)

    // 设置 数据的timestamp字段 为事件时间  --》根据它判断是否为迟到数据
    keyedStream.assignAscendingTimestamps(_.timestamp)
    // 将迟到数据放入 侧输出流
    val outPutStream: DataStream[Int] = keyedStream.timeWindow(Time.seconds(10))
      .sideOutputLateData(OutputTag[SensorReading]("late-data"))
      .apply(new MyWindowFunction())

    // 获取 迟到数据的侧输出流
    outPutStream.getSideOutput(OutputTag[SensorReading]("late-data"))

    env.execute("FlinkWindowTest")
  }
}

// 自定义一个全窗口函数
class MyWindowFunction extends WindowFunction[SensorReading, Int, Tuple, TimeWindow] {
  override def apply(key: Tuple, window: TimeWindow, input: Iterable[SensorReading], out: Collector[Int]): Unit = {
    out.collect(input.size)
  }
}

// 自定义一个 ProcessFunction
class MyProcessWindowFunction extends ProcessWindowFunction[SensorReading, Int, Tuple, TimeWindow] {
  override def process(key: Tuple, context: Context, elements: Iterable[SensorReading], out: Collector[Int]): Unit = {
    out.collect(elements.size)
  }
}