package source

import java.util.Properties

import org.apache.flink.api.common.functions.RichFilterFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011
import org.apache.kafka.common.serialization.StringDeserializer

/**
 * @Auther: wxf
 * @Date: 2022/6/7 17:58:06
 * @Description: 读取Kafka数据
 * @Version 1.0.0
 */
object KafkaSource {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val properties: Properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("group.id", "consumer-group")
    properties.setProperty("key.deserializer", classOf[StringDeserializer].getName)
    properties.setProperty("value.deserializer", classOf[StringDeserializer].getName)
//    properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("auto.offset.reset", "latest")
    val kafkaSource: DataStream[String] = env.addSource(new FlinkKafkaConsumer011("", new SimpleStringSchema(), properties))

    kafkaSource.print("input1:").setParallelism(1)
    env.execute("KafkaSource")
  }
}