package operator

import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.scala.{DataStream, SplitStream, StreamExecutionEnvironment}
import scala.collection.mutable.ListBuffer

// 6.split 与 select （拆分流）
/*     split   -> DataStream → SplitStream
        按照指定标准将指定的DataStream拆分成多个流用SplitStream来表示
      select   -> SplitStream → DataStream
        跟split搭配使用，从SplitStream中选择一个或多个流  */
object SplitAndSelect {
  def main(args: Array[String]): Unit = {
    //需要加上这一行隐式转换 否则在调用flatmap方法的时候会报错
    import org.apache.flink.api.scala._
    val config: Configuration = new Configuration()
    //开启spark-webui
    config.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    //配置webui的日志文件，否则打印日志到控制台
    config.setString("web.log.path", "/tmp/flink_log")
    //配置taskManager的日志文件，否则打印日志到控制台
    config.setString(ConfigConstants.TASK_MANAGER_LOG_PATH_KEY, "/tmp/flink_log")
    //配置tm有多少个slot
    config.setString("taskmanager.numberOfTaskSlots", "12")
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config)
    env.setParallelism(1)
    val input: DataStream[Long] = env.generateSequence(0, 10)
    val splitStream: SplitStream[Long] = input.split(f => {
      val out: ListBuffer[String] = new ListBuffer[String]
      if (f % 2 == 0) {
        out += "hainiu"
      } else {
        out += "dashuju"
      }
      out
    })
    //根据拆分标记选择数据
    splitStream.select("hainiu").print("hainiu")
    splitStream.select("dashuju").print("dashuju")
    splitStream.select("hainiu","dashuju").print("hainiu_dashuju")

    env.execute()
  }
}
