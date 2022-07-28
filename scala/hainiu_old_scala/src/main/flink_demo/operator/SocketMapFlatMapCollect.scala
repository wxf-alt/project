package operator

import java.util

import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.datastream.DataStreamUtils
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

// 1.map flatMap 与 DataStreamUtils.collect的使用
object SocketMapFlatMapCollect {
  def main(args: Array[String]): Unit = {
    //需要加上这一行隐式转换 否则在调用flatmap方法的时候会报错
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

    val text: DataStream[String] = env.socketTextStream("localhost", 6666)
    // 定义operators，作用是解析数据, 分组
    val windowCounts: DataStream[(String, Int)] = text.flatMap(_.split(" ")).map((_, 1))
    // 定义sink打印输出
    windowCounts.print()

    // 把数据拉回到client端进行操作，比如发起一次数据连接把数据统一插入
    // 使用了DataStreamUtils.collect就可以省略env.execute
    import scala.collection.convert.wrapAll._ //这个和引入 collection.JavaConversions._ 没什么分别
//    import scala.collection.convert.wrapAsJava._ //单纯完成 Scala 到 Java 集合类型的隐式转换
//    import scala.collection.convert.wrapAsScala._ //只是完成 Java 到 Scala 集合的隐式转换
    val value: util.Iterator[(String, Int)] = DataStreamUtils.collect(windowCounts.javaStream)
    for(v <- value){
      println(v)
    }

    // 使用了DataStreamUtils.collect就可以省略env.execute
//    env.execute("Socket WordCount")

  }
}
