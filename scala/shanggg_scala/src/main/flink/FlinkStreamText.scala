import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.scala._

/**
 * @Auther: wxf
 * @Date: 2022/5/31 09:35:03
 * @Description: Flink 流处理
 * @Version 1.0.0
 */
object FlinkStreamText {
  def main(args: Array[String]): Unit = {

    //生成配置对象
    val conf: Configuration = new Configuration()
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)
    //        val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    environment.setParallelism(1)

    val inputStream: DataStream[String] = environment.socketTextStream("localhost", 6666)
    val result: DataStream[(String, Int)] = inputStream.flatMap(_.split(" "))
      .map((_, 1))
      .keyBy(0)
      .sum(1)
    result.print()

    environment.execute("FlinkStreamText")

    //    println(environment.getParallelism)
    //    environment.setParallelism(1)
    //    val value: DataStream[(String, Int)] = environment.readTextFile(path)
    //      .flatMap(_.split(" "))
    //      .map((_, 1))
    //      .keyBy(0)
    //      .reduce((x, y) => (x._1, x._2 + y._2))
    //
    //    println(value.getParallelism)
    //    value.print()
    //
    //    environment.execute("")
  }
}
