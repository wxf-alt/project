package operator

import org.apache.flink.api.common.functions.Partitioner
import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

// 8.自定义partitioner
/*
    转换关系：DataStream → DataStream
    使用场景：自定义数据处理负载
    实现方法：
    实现org.apache.flink.api.common.functions.Partitioner 接口
    覆盖partition方法
    设计算法返回partitionId
*/
object CustomPartitioner {
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
    val tuple: Seq[(String, String, String, Int)] = List(
      ("hainiu", "class12", "小王", 50),
      ("hainiu", "class12", "小李", 55),
      ("hainiu", "class11", "小张", 50),
      ("hainiu", "class11", "小强", 45))
    // 定义数据源，使用集合生成
    val input: DataStream[(String, String, String, Int)] = env.fromCollection(tuple)
    //第二个参数 _._2 是指定partitioner的key是数据中的那个字段
    val value: DataStream[(String, String, String, Int)] = input.partitionCustom(new MyFlinkPartitioner, _._4)
    value.print("MyFlinkPartitioner")

    env.execute()
  }
}

case class MyFlinkPartitioner() extends Partitioner[Int]{
  override def partition(key: Int, numPartitions: Int): Int = {
    println(key)
    if(key % 2 == 0){
        0
    }else{
      1
    }
  }
}

//class MyFlinkPartitioner extends Partitioner[String]{
//  override def partition(key: String, numPartitions: Int): Int = {
//    println(key)
//    1
//  }
//}

