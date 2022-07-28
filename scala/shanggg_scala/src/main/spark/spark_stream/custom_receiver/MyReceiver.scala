package spark_stream.custom_receiver

import java.io.{BufferedInputStream, BufferedReader, InputStream, InputStreamReader}
import java.net.Socket

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.receiver.Receiver

//noinspection DuplicatedCode
// 自定义 接收器
object MyReceiver {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("MyReceiver")
    // 1.创建 StreamingContext
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))
    val sourceStream: ReceiverInputDStream[String] = ssc.receiverStream(new MyReceiver("localhost", 6666))
    val result: DStream[(String, Int)] = sourceStream.flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
    result.print()
    // 5.启动 StreamingContext
    ssc.start()
    // 6.阻止主线程退出
    ssc.awaitTermination()
  }
}

// 接收 socket 接收数据
class MyReceiver(host: String, port: Int) extends Receiver[String](StorageLevel.MEMORY_ONLY) {
  var socket: Socket = _
  // 字符缓冲流(字符流(输入流))
  var reader: BufferedReader = _

  override def onStart(): Unit = {
    runInThread(
      {
        try {
          socket = new Socket(host, port)
          reader = new BufferedReader(new InputStreamReader(socket.getInputStream, "utf-8"))
          var line: String = reader.readLine()
          while (line != null && socket.isConnected) {
            store(line)
            line = reader.readLine()
          }
        } catch {
          case e: Exception => println(e.getMessage)
        } finally {
          restart("重启接收器")
          // 立即调用onstop 然后再调用 onstop
        }
      }
    )
  }

  def runInThread(op: => Unit): Unit = {
    new Thread() {
      override def run(): Unit = op
    }.start()
  }

  override def onStop(): Unit = {
    if (socket != null) socket.close()
    if (reader != null) reader.close()
  }
}
