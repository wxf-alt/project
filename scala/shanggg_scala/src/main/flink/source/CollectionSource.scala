package source

import bean.SensorReading
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * @Auther: wxf
 * @Date: 2022/6/7 17:46:33
 * @Description:  从集合读取数据
 * @Version 1.0.0
 */
object CollectionSource {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    println("getParallelism：" + env.getParallelism)
    val stream1 : DataStream[SensorReading] = env.fromCollection(List(
      SensorReading("sensor_1", 1547718199, 35.8),
      SensorReading("sensor_6", 1547718201, 15.4),
      SensorReading("sensor_7", 1547718202, 6.7),
      SensorReading("sensor_10", 1547718205, 38.1)
    ))

    stream1.print("stream1:").setParallelism(1)

    env.execute("CollectionSource")
  }
}


