package operator

import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

// 7.rebalance 与 rescale (物理分区)
/*
rebalance：
  含义：再平衡，用来减轻数据倾斜
  转换关系: DataStream → DataStream
  使用场景：处理数据倾斜，比如某个kafka的partition的数据比较多
rescale：
  原理：通过轮询调度将元素从上游的task一个子集发送到下游task的一个子集
  转换关系：DataStream → DataStream
  使用场景：数据传输都在一个TaskManager内，不需要通过网络。
*/
object RebalanceRescale {
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
    env.setParallelism(3)
    val input: DataStream[Long] = env.generateSequence(0, 10)
    val flatMap: DataStream[String] = input.flatMap(_.toString.split(" "))

    // 使用 reblance
    val rebalance: DataStream[String] = flatMap.rebalance
    // 使用 rescale
    val rescale: DataStream[String] = flatMap.rescale

    rebalance.print("rebalance")
    rescale.print("rescale")

    env.execute()
  }
}
