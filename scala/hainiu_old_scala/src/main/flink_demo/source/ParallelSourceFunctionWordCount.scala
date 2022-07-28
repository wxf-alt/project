package source

import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.functions.source.{ParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.util.Collector

// 3.自定义数据源  并行的
//    实现SourceFunction（并行的）
object ParallelSourceFunctionWordCount {
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
    // 加入隐式转换
    import org.apache.flink.api.scala._

    // 读取自定义 source
    val sourceInput: DataStream[String] = env.addSource(new MyParallelSource())
    // String -》输入 ; (String,Int) -》输出
    // 定义operators，作用是解析数据, 分组, 窗口化, 并且聚合求SUM
    val windowCounts: DataStream[(String, Int)] = sourceInput.setParallelism(2).flatMap(new FlatMapFunction[String, (String, Int)] {
      override def flatMap(value: String, out: Collector[(String, Int)]) = {
        val str: Array[String] = value.split(" ")
        for (s <- str) {
          // 调用collect 方法 return 出去
          out.collect((s, 1))
        }
      }
    }).setParallelism(2).keyBy(0).sum(1).setParallelism(2)

    // 定义sink打印输出
    windowCounts.slotSharingGroup("hainiu").print().setParallelism(2)

    //打印任务执行计划
    println(env.getExecutionPlan)

    //定义任务的名称并运行
    //注意：operator是惰性的，只有遇到env.execute才执行
    env.execute("Source Window Parallel WordCount")

  }
}

class MyParallelSource extends ParallelSourceFunction[String] {
  var num = 0
  // 设置变量 是否需要 cancel
  var isCancel = true

  override def run(ctx: SourceFunction.SourceContext[String]): Unit = {
    while (isCancel) {
      ctx.collect(s"MySource_${num}")
      Thread.sleep(1000)
      num += 1
    }
  }

  override def cancel(): Unit = {
    println("caceling........")
    isCancel = false
  }
}
