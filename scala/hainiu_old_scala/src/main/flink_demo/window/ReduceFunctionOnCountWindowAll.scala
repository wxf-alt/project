package window

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

// 2.WindowsAll基础使用
//   WindowAll与Windows的区别是一个windows里的数据只能在一个task中进行运行
object ReduceFunctionOnCountWindowAll {
  def main(args: Array[String]): Unit = {
    import org.apache.flink.api.scala._
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
    val list = List(
      ("hainiu", "class12", "小王", 50),
      ("hainiu", "class12", "小李", 55),
      ("hainiu", "class13", "小L", 100),
      ("hainiu", "class11", "小A", 75),
      ("hainiu", "class11", "小张", 50))
    // 定义socket数据源，使用集合生成
    val input: DataStream[(String, String, String, Int)] = env.fromCollection(list)

    //先分组，然后数据按分组进行不同的窗口，当窗口数据量达到两条时，启动reduce计算两条记录的分组合
    val windows: DataStream[(String, String, String, Int)] = input.keyBy(_._2).countWindowAll(2).reduce(new ReduceFunction[(String, String, String, Int)] {
      override def reduce(value1: (String, String, String, Int), value2: (String, String, String, Int)) = {
        (value2._1, value2._2, value2._3, value1._4 + value2._4)
      }
    })

    windows.print()

    env.execute()
  }
}
