package window

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

// 3.Windows的Aggregate窗口自定义聚合函数
object AggFunctionOnCountWindow {
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

    //先分组，然后数据按分组进行不同的窗口，当窗口数据量达到两条时，启动aggregate计算两条记录的分组合
    input.keyBy(_._2).countWindow(2).aggregate(new SumAggregate()).print()

    env.execute()
  }
}

class SumAggregate extends AggregateFunction[(String,String,String,Int),(String,Int),(String,Int)]{
  /**
   * 创建累加器来保存中间状态(name和count)
   */
  override def createAccumulator(): (String, Int) = ("",0)

  /**
   * 将元素添加到累加器并返回新的累加器value
   */
  override def add(value: (String, String, String, Int), accumulator: (String, Int)): (String, Int) = {
    (s"${value._3}\t${accumulator._1}", accumulator._2 + value._4)
  }

  /**
   * 从累加器提取结果
   */
  override def getResult(accumulator: (String, Int)): (String, Int) = accumulator

  /**
   * 合并两个累加器并返回
   */
  override def merge(a: (String, Int), b: (String, Int)): (String, Int) = {
    (s"${a._1}\t${b._1}", a._2 + b._2)
  }
}