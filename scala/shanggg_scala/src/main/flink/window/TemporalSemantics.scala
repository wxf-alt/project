package window

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * @Auther: wxf
 * @Date: 2022/6/21 14:47:00
 * @Description: 时间语义
 * @Version 1.0.0
 */
object TemporalSemantics {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val inputStream: DataStream[String] = env.socketTextStream("localhost", 6666)
    // 设置并行度为 1
    env.setParallelism(1)
    val timeCharacteristic: TimeCharacteristic = env.getStreamTimeCharacteristic
    println(timeCharacteristic)   // 默认 ProcessingTime
    // 设置时间语义 为 EventTime
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val streamTimeCharacteristic: TimeCharacteristic = env.getStreamTimeCharacteristic
    println(streamTimeCharacteristic)  // EventTime
  }
}
