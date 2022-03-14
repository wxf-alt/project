package sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}

import scala.collection.mutable.ListBuffer

// 多receiver源 union 的方式
object SparkStreamingUnionReceiver {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingFile").setMaster("local[3]")
    val ssc: StreamingContext = new StreamingContext(conf, Durations.seconds(5))

    //生成两个socket流
    val socketStream1: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 6666)
    val socketStream2: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 7777)

//    //合拼两个socket流
    val bu = new ListBuffer[DStream[String]]
    bu += socketStream1
    bu += socketStream2
    val unionStream: DStream[String] = ssc.union(bu)
    //合拼了的两个流使用了统一的业务处理逻辑
    unionStream.foreachRDD(r => {
      r foreach println
    })

//    val inputStream: DStream[String] = socketStream1.union(socketStream2)
//    inputStream.foreachRDD( f => {
//      f.foreach(println)
//    })

    // 启动
    ssc.start()
    ssc.awaitTermination()

  }
}
