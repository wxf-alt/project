package transform

import bean.{SensorReading, SmokeAlarm}
import org.apache.flink.streaming.api.functions.co.{CoFlatMapFunction, CoMapFunction}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

/**
 * @Auther: wxf
 * @Date: 2022/6/9 10:05:03
 * @Description: ConnectMapTest
 * @Version 1.0.0
 */
object ConnectMapTest {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val inputStream: DataStream[String] = env.socketTextStream("localhost", 6666)

    val sensorReadingStream: DataStream[SensorReading] = inputStream.map(x => {
      val str: Array[String] = x.split(" ")
      (SensorReading(str(0), str(1).toLong, str(2).toDouble))
    })

    val sensorAlarm: DataStream[(String, Double)] = sensorReadingStream.split(x => {
      if (x.temperature > 30) Seq("high") else Seq("low")
    }).select("high")
      .map(x => (x.id, x.temperature))

    val inputStream1: DataStream[String] = env.socketTextStream("localhost", 7777)
    val smoketream: DataStream[SmokeAlarm] = inputStream1.map(x => {
      val str: Array[String] = x.split(" ")
      (SmokeAlarm(str(0), str(1).toLong, str(2).toDouble))
    })

    val connected : ConnectedStreams[(String, Double), SmokeAlarm] = sensorAlarm.connect(smoketream)
//    val coMap: DataStream[Product] = connected.map(
//      warningData => (warningData._1, warningData._2, "warning"),
//      lowData => (lowData.id, "healthy")
//    )
//    connected.map(new CoMapFunction[(String, Double), SmokeAlarm,(String,Double,String)] {
//      override def map1(value: (String, Double)) = (value._1, value._2, "warning")
//
//      override def map2(value: SmokeAlarm) = (value.id,value.concentration, "healthy")
//    })

    val coMap: DataStream[Product] = connected.map(new CoMapFunction[(String, Double), SmokeAlarm, Product] {
      override def map1(value: (String, Double)) = (value._1, value._2, "warning")

      override def map2(value: SmokeAlarm) = (value.id, value.concentration, "healthy")
    })

    coMap.print("ConnectMapTest")
    
    env.execute("ConnectMapTest")
  }
}
