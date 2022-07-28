package processFunction

import bean.SensorReading
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

/**
 * @Auther: wxf
 * @Date: 2022/7/4 10:14:04
 * @Description: SideOutputTest  温度值低于 32 的 告警信息输出到 side output
 * @Version 1.0.0
 */
// 温度值低于 32 的 告警信息输出到 side output
object SideOutputTest {
  def main(args: Array[String]): Unit = {
    //生成配置对象
    val conf: Configuration = new Configuration()
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    // web 查看 http://localhost:8081/
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)
    //    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    environment.setParallelism(1)

    // 获取 source
    val inputStream: DataStream[String] = environment.socketTextStream("localhost", 6666)
    // 解析数据
    val warningStream: DataStream[SensorReading] = inputStream.map(x => {
      val str: Array[String] = x.split(" ")
      SensorReading(str(0), str(1).toLong, str(2).toDouble)
    })
      .keyBy("id")
      .process(new MyProcessFunctionSideOutPut())

    val monitoredReadingsHigh: DataStream[String] = warningStream.getSideOutput(OutputTag[String]("freezing-alarms-high"))
    val monitoredReadingsLow: DataStream[String] = warningStream.getSideOutput(OutputTag[String]("freezing-alarms-low"))

    warningStream.print("SensorReading：")
    monitoredReadingsHigh.print("freezing-alarms-high：")
    monitoredReadingsLow.print("freezing-alarms-low：")

    environment.execute("ProcessFunctionTest")
  }
}

// 自定义 processFunction 设置 侧输出流
// 温度值低于 32 的 告警信息输出到 side output
class MyProcessFunctionSideOutPut extends KeyedProcessFunction[Tuple, SensorReading, SensorReading] {

  // 定义侧输出流的标签
  private lazy val freezingAlarmOutputHigh: OutputTag[String] = new OutputTag[String]("freezing-alarms-high")
  private lazy val freezingAlarmOutputLow: OutputTag[String] = new OutputTag[String]("freezing-alarms-low")

  override def processElement(value: SensorReading, ctx: KeyedProcessFunction[Tuple, SensorReading, SensorReading]#Context, out: Collector[SensorReading]): Unit = {
    if (value.temperature < 32) {
      ctx.output(freezingAlarmOutputLow, value.toString)
    }else if(value.temperature > 40){
      ctx.output(freezingAlarmOutputHigh,value.toString)
    }
    out.collect(value)
  }



}