package sink

import java.sql.{Connection, DriverManager, PreparedStatement}

import bean.SensorReading
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala._
import source.MySensource

/**
 * @Auther: wxf
 * @Date: 2022/6/14 20:26:56
 * @Description: JDBCSink
 * @Version 1.0.0
 */
object JDBCSink {
  def main(args: Array[String]): Unit = {
    val path: String = this.getClass.getClassLoader.getResource("sensor.txt").getPath
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

//    val inputStream: DataStream[String] = env.readTextFile(path)
//    val transform: DataStream[SensorReading] = inputStream.map(s => {
//      val str: Array[String] = s.split(" ")
//      SensorReading(str(0), str(1).toLong, str(2).toDouble)
//    })

    val transform: DataStream[SensorReading] = env.addSource(new MySensource())

    transform.print("transform -- ")

    transform.addSink(new MyJDBCSink())

    env.execute("JDBCSink")
  }
}

class MyJDBCSink extends RichSinkFunction[SensorReading] {

  var conn: Connection = _
  var insertStmt: PreparedStatement = _
  var updateStmt: PreparedStatement = _

  // 创建数据库连接
  override def open(parameters: Configuration): Unit = {
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "root", "root")
    insertStmt = conn.prepareStatement("insert into sensor (sensor, temperature, ts) values (?,?,?)")
    updateStmt = conn.prepareStatement("update sensor set temperature = ?,ts = ? where sensor = ?")
  }


  override def invoke(value: SensorReading, context: SinkFunction.Context[_]): Unit = {
    // 执行更新语句
    updateStmt.setDouble(1, value.temperature)
    updateStmt.setString(2, value.timestamp.toString)
    updateStmt.setString(3, value.id)
    updateStmt.execute
    if (updateStmt.getUpdateCount == 0) {
      insertStmt.setString(1, value.id)
      insertStmt.setDouble(2, value.temperature)
      insertStmt.setString(3, value.timestamp.toString)
      insertStmt.execute()
    }
  }

  // 关闭操作
  override def close(): Unit = {
    insertStmt.close()
    updateStmt.close()
    conn.close()
  }

}