package transform

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala._

/**
 * @Auther: wxf
 * @Date: 2022/6/8 15:09:36
 * @Description: KeyBeyTest
 * @Version 1.0.0
 */
object KeyByTest {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val inputStream: DataStream[String] = env.socketTextStream("localhost", 6666)

    val keyByStream: KeyedStream[(String, Int), Tuple] = inputStream.flatMap(_.split(" ")).map((_, 1))
      // 按照第一个元素分组
      .keyBy(0)

    keyByStream.print("keyBy:")
    env.execute("KeyByTest")

  }
}
