package sink

import bean.SensorReading
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011

/**
 * @Auther: wxf
 * @Date: 2022/6/14 19:05:06
 * @Description: KafkaSink
 * @Version 1.0.0
 */
object KafkaSink {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val inputStream: DataStream[String] = env.readTextFile("E:\\A_data\\3.code\\project\\scala\\shanggg_scala\\src\\main\\resources\\sensor.txt")
    val mapStream: DataStream[String] = inputStream.map(x => {
      val str: Array[String] = x.split(" ")
      SensorReading(str(0), str(1).toLong, str(3).toDouble).toString
    })
    mapStream.addSink(new FlinkKafkaProducer011[String]("brokerList","topicId",new SimpleStringSchema()))
    env.execute("KafkaSink")
  }
}
