package source

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

// 1.内置数据源
object SourceTest1 {
  def main(args: Array[String]): Unit = {

    //生成了配置对象
    //    val config = new Configuration()
    //开启flink-webui
    //    config.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    //    // http://localhost:8081/ 查看
    //    config.setString("rest.bind-port", "8081")
    //    //配置webui的日志文件，否则打印日志到控制台
    //    config.setString("web.log.path", "/tmp/flink_log")
    //    //配置taskManager的日志文件，否则打印日志到控制台
    //    config.setString(ConfigConstants.TASK_MANAGER_LOG_PATH_KEY, "/tmp/flink_log")

    //获得local运行环境
    //    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config)
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 引入隐式转换
    import org.apache.flink.streaming.api.scala._

    //    // 1.基于文件
    //    val textStream: DataStream[String] = env.readTextFile("")
    //    val testStream1: DataStream[String] = env.readFile(new TextInputFormat(null), "")
    //
    //    // 2.基于socket
    //    val socketStream: DataStream[String] = env.socketTextStream("localhost", 6666)
    //
    //    // 3.基于Collection
    //    val collectStream: DataStream[Int] = env.fromCollection(List(1, 5, 8, 7, 9, 4, 2, 5, 6))
    //    val ElementStream: DataStream[Int] = env.fromElements(1, 2, 3)
    //    val SequenceStream: DataStream[Long] = env.generateSequence(0, 1000)
    val sequenceStream: DataStream[Long] = env.generateSequence(0, 100)
    env.setParallelism(2)
    sequenceStream.print()
    //    println(SequenceStream.getParallelism)

    env.execute("SourceTest1")
    Thread.sleep(1000000)

  }
}
