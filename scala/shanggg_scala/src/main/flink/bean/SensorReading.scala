package bean

import org.apache.flink.api.common.serialization.SerializationSchema

// 定义样例类，传感器id，时间戳，温度
case class SensorReading(id: String, timestamp: Long, temperature: Double){
  override def toString: String = s"id：${id},timestamp：${timestamp}，temperature：${temperature}"
}


// 定义样例类，烟雾传感器id，时间戳，烟雾浓度
case class SmokeAlarm(id: String, timestamp: Long, concentration: Double)