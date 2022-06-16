package transform

import bean.SensorReading
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala._

/**
 * @Auther: wxf
 * @Date: 2022/6/8 16:17:25
 * @Description: reduceTest
 * @Version 1.0.0
 */
object reduceTest {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val inputStream: DataStream[String] = env.socketTextStream("localhost", 6666)

    val reduceStream: DataStream[SensorReading] = inputStream.map(x => {
      val str: Array[String] = x.split(" ")
      SensorReading(str(0), str(1).toLong, str(2).toDouble)
    })
      .keyBy("id")
      .reduce((x, y) => SensorReading(x.id, x.timestamp + 1, y.temperature))


    reduceStream.print("reduce:")

    env.execute("reduceTest")
  }
}
