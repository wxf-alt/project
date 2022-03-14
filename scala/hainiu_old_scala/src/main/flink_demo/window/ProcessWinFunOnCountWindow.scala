package window

import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow
import org.apache.flink.util.Collector

object ProcessWinFunOnCountWindow {
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
      ("hainiu", "class11", "小K", 50),
      ("hainiu", "class11", "小张", 50))
    // 定义socket数据源，使用集合生成
    val input: DataStream[(String, String, String, Int)] = env.fromCollection(list)
    //先分组，然后数据按分组进行不同的窗口，当窗口数据量达到两条时，启动process计算两条记录的平均值
    input
      .keyBy(_._2)
      .countWindow(2)
      .process(new AvgProcessWindowFunction)
      .print()

    env.execute()
  }
}

class AvgProcessWindowFunction extends ProcessWindowFunction[(String,String,String,Int),String, String, GlobalWindow]{
  /**
   * 分组并计算windows里所有数据的平均值
   *
   * @param key        分组key
   * @param context    windows上下文
   * @param elements   分组的value
   * @param out        operator的输出结果
   */
  override def process(key: String, context: Context, elements: Iterable[(String, String, String, Int)], out: Collector[String]): Unit ={
    // 保存 和
    var sum = 0
    // 保存数据个数
    var count = 0
    for(in <- elements){
      sum += in._4
      count += 1
    }
    out.collect(s"key:${key}  Window:${context.window} count:${count} avg:${sum/count}")
  }
}
