package transform

import bean.SensorReading
import org.apache.flink.streaming.api.scala._

/**
 * @Auther: wxf
 * @Date: 2022/6/9 09:50:49
 * @Description: SplitSelectTest
 * @Version 1.0.0
 */
object SplitSelectTest {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val inputStream: DataStream[String] = env.socketTextStream("localhost", 6666)

    val mapStream: DataStream[SensorReading] = inputStream.map(x => {
      val str: Array[String] = x.split(" ")
      SensorReading(str(0), str(1).toLong, str(2).toDouble)
    })

    val splitStream: SplitStream[SensorReading] = mapStream.split(s => {
      if (s.temperature > 30) {
        List("high")
      } else {
        List("low")
      }
    })

    splitStream.select("high").print("high")
    splitStream.select("low").print("low")

    env.execute("SplitSelectTest")

  }
}
