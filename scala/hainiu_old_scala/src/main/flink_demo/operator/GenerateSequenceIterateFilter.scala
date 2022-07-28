package operator

import java.util

import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.datastream.DataStreamUtils
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

// 2.filter 与 循环迭代流
//    含义：数据筛选（满足条件event的被筛选出来进行后续处理），根据FliterFunction返回的布尔值来判断是否保留元素，true为保留，false则丢弃
object GenerateSequenceIterateFilter {
  def main(args: Array[String]): Unit = {

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
    //需要加上这一行隐式转换 否则在调用flatmap方法的时候会报错
    import org.apache.flink.api.scala._
    //设置全局并行度为1
    env.setParallelism(1)
    // 生成包含一个1到10的DStream
    val inputStream: DataStream[Long] = env.generateSequence(1, 6)

    //流中的元素每个减1，并过滤出大于0的，然后生成新的流
    val result: DataStream[Long] = inputStream.iterate(
      f => (f.map(_ - 1),
        f.filter(_ > 2))
    )
    result.print()

//    val value: util.Iterator[Long] = DataStreamUtils.collect(result.javaStream)
//    import scala.collection.convert.wrapAll._
//    value.foreach(println)

    env.execute("SockectIterateFilter")

  }
}
