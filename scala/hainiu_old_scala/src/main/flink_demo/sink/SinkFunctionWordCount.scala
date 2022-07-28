package sink

import java.text.SimpleDateFormat
import java.util.Date
import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment}

//  1.实现SinkFunction
object SinkFunctionWordCount {
  def main(args: Array[String]): Unit = {
    // 引入 防止后面使用 flatMap 报错
    import org.apache.flink.api.scala._
    //生成配置对象
    val config = new Configuration()
    //开启spark-webui
    config.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    //配置webui的日志文件，否则打印日志到控制台
    config.setString("web.log.path", "/tmp/flink_log")
    //配置taskManager的日志文件，否则打印日志到控制台
    config.setString(ConfigConstants.TASK_MANAGER_LOG_PATH_KEY, "/tmp/flink_log")
    //配置tm有多少个slot
    config.setString("taskmanager.numberOfTaskSlots", "12")

    // 获取运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config)
    env.setParallelism(1)

    val input: DataStream[String] = env.fromCollection(List("hainiu hainiu hainiu aa ecc asdf sfdv aa cee ecc sffdf ecc"))
    val result1: DataStream[(String, Int)] = input.flatMap(_.split(" ")).map((_, 1)).keyBy(_._1).reduce((a, b) => (a._1, a._2 + b._2))
//    // 下标从 0 开始
//    val result1: DataStream[(String, Int)] = input.flatMap(f => f.split(" ")).map((_, 1)).keyBy(0).sum(1)
//    // keyBy(_._1)  按照元组的写法 下标从 1 开始
//    val result1: DataStream[(String, Int)] = input.flatMap(f => f.split(" ")).map((_, 1)).keyBy(_._1).sum(1)

    // 使用自定义的sink
    result1.addSink(new MySinkFunction)

    env.execute()
  }
}

class MySinkFunction extends SinkFunction[(String,Int)]{
  override def invoke(value: (String, Int), context: SinkFunction.Context[_]): Unit = {

    // 修改时间戳格式
    val date: Date = new Date(context.currentProcessingTime())
    val format: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val str: String = format.format(date)

    println(s"value:${value}," +
      s"processTime:${context.currentProcessingTime()}," +
      s"date: ${new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(context.currentProcessingTime()))}," +
      s"waterMark:${context.currentWatermark()}")
  }
}
