package source


import bean.SensorReading
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala._

import scala.collection.immutable
import scala.util.Random

/**
 * @Auther: wxf
 * @Date: 2022/6/7 20:16:33
 * @Description: 自定义 Source
 * @Version 1.0.0
 */
//noinspection ComparingUnrelatedTypes
object MySensource {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val MySource: DataStream[SensorReading] = env.addSource(new MySensource())

    MySource.print("MySource").setParallelism(1)
    env.execute("MySensource")
  }
}


class MySensource extends SourceFunction[SensorReading] {

  // 定义一个flag，表示数据是否正常运行
  var running: Boolean = true

  override def run(ctx: SourceFunction.SourceContext[SensorReading]): Unit = {
    // 定义一个随机数发生器
    val random: Random = new Random()
    // 温度随机上下波动
    // 生成10个传感器的初始温度
    var curTemps: immutable.IndexedSeq[(String, Double)] = (1 to 10).map(x => ("sensor_" + x, 60 + random.nextGaussian() * 20))

    while (running) {
      curTemps = curTemps.map(x => (x._1, x._2 + random.nextGaussian()))
      val times: Long = System.currentTimeMillis()
      curTemps.foreach(
        x => ctx.collect(SensorReading(x._1, times, x._2))
      )
      Thread.sleep(1000L)
    }

  }
  override def cancel(): Unit = running = false

}
