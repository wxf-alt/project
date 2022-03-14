package operator

import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.scala.{ConnectedStreams, DataStream, StreamExecutionEnvironment}
import org.apache.flink.util.Collector

// 5.connect 与 union (合并流)
/*
   connect之后生成ConnectedStreams，会对两个流的数据应用不同的处理方法，并且双流之间可以共享状态（比如计数）。这在第一个流的输入会影响第二个流时, 会非常有用。union 合并多个流，新的流包含所有流的数据。
   union是DataStream → DataStream
   connect只能连接两个流，而union可以连接多于两个流
   connect连接的两个流类型可以不一致，而union连接的流的类型必须一致
   */
object ConnectUnion {
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

    val input1: DataStream[Long] = env.generateSequence(0, 10)
    val input2: DataStream[String] = env.fromCollection(List("hainiu xueyuan dashuju"))
    // 连接两个流
    val connectStream: ConnectedStreams[Long, String] = input1.connect(input2)
    //使用connect连接两个流，类型可以不一致
    val connect: DataStream[String] = connectStream.map[String](
      //处理第一个流的数据，需要返回String类型
      (a: Long) => a.toString,
      //处理第二个流的数据，需要返回String类型
      (b: String) => b )
    connect.print()
    // flatMap之后的泛型确定了两个流合并之后的返回类型
    val value: DataStream[String] = connectStream.flatMap(
      //处理第一个流的数据，需要返回String类型
      (data: Long, out: Collector[String]) => {
        out.collect(data.toString)
      },
      //处理第二个流的数据，需要返回String类型
      (data: String, out: Collector[String]) => {
        val strings: Array[String] = data.split(" ")
        for (s <- strings) {
          out.collect(s)
        }
      }
    )
    value.print()

    val input3: DataStream[Long] = env.generateSequence(11, 20)
    val input4: DataStream[Long] = env.generateSequence(21, 30)
    //使用union连接多个流，要求数据类型必须一致，且返回结果是DataStream
    val value1: DataStream[Long] = input1.union(input3, input4)
    value1.print("union1")
    val value2: DataStream[Long] = input1.union(input3).union(input4)
    value2.print("union2")

    env.execute("ConnectUnion")
  }
}
