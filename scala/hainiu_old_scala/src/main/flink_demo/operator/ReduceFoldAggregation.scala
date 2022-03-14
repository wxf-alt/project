package operator

import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment}

// 4.reduce 与 fold
//   对分组之后的数据也就是KeyedStream进行各种聚合操作
//       reduce --> KeyedStream流上，将上一次reduce的结果和本次的进行操作
//       fold -->  对keyedStream流上的event进行连接操作
//       sum/min/minBy/max/maxBy -->  reduce的特例，min和minBy的区别是min返回的是一个最小值，而minBy返回的是其字段中包含最小值的元素(同样原理适用于max和maxBy)
//       process -->  底层的聚合操作
object ReduceFoldAggregation {
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

    val tuple = List(
      ("hainiu", "class12", "小王", 50),
      ("hainiu", "class12", "小李", 55),
      ("hainiu", "class11", "小张", 50),
      ("hainiu", "class11", "小强", 45))
    val text = env.fromCollection(tuple)

    val keyBy: KeyedStream[(String, Int), String] = text
      .map(f => (f._2, 1))
      .keyBy(_._1)
    //相同的key的数据聚合在一起使用reduce求合，使用的时候注意与spark不同的地方是key也参与运算
    val reduce: DataStream[(String, Int)] = keyBy.reduce((f1, f2) => (f1._1, f1._2 + f2._2))
    reduce.print("reduce")

    //使用fold完成和reduce一样的功能，不同的是这里的返回值类型由fold的第一个参数决定
    val fold: DataStream[(String, Int)] = keyBy.fold(("",0))((a,b) => (b._1,a._2 + b._2))
    fold.print()

    env.execute("ReduceFoldAggregation")
  }
}
