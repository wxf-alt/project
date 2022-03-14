import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.util.Collector

/*
flink开发基本流程：
  1.定义源  source
  2.写Transformations，就是写 operators
  3.定义输出 sink
*/
object SocketWordCount {
  def main(args: Array[String]): Unit = {
    //生成了配置对象
    val config = new Configuration()
    //开启flink-webui
    config.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    // http://localhost:8081/ 查看
    config.setString("rest.bind-port", "8081")
    //配置webui的日志文件，否则打印日志到控制台
    config.setString("web.log.path", "/tmp/flink_log")
    //配置taskManager的日志文件，否则打印日志到控制台
    config.setString(ConfigConstants.TASK_MANAGER_LOG_PATH_KEY, "/tmp/flink_log")

    //获得local运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config)
//    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //定义socket的source源
    val text: DataStream[String] = env.socketTextStream("localhost", 6666)

    //scala开发需要加一行隐式转换，否则在调用operator的时候会报错，作用是找到scala类型的TypeInformation
    import org.apache.flink.api.scala._

    //写Transformations进行数据的转换
    //定义operators，作用是解析数据，分组，并且求wordCount
    //    val wordCount: DataStream[(String, Int)] = text.flatMap(_.split(" ")).map((_, 1)).keyBy(_._1).sum(1)

    //使用FlatMapFunction自定义函数来完成flatMap和map的组合功能
    val wordCount: DataStream[(String, Int)] = text.flatMap(new FlatMapFunction[String, (String, Int)] {
      override def flatMap(value: String, out: Collector[(String, Int)]) = {
        val strings: Array[String] = value.split(" ")
        for (s <- strings) {
          out.collect((s, 1))
        }
      }
    }).keyBy(_._1).sum(1)

    //定义sink，打印数据到控制台
    wordCount.print()

    //定义任务的名称并运行
    //注意：operator是惰性的，只有遇到execute才执行
    env.execute("SocketWordCount")
  }
}
