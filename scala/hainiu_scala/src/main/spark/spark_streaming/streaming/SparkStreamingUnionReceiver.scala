package spark_streaming.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}

import scala.collection.mutable.ListBuffer

object SparkStreamingUnionReceiver {
  def main(args: Array[String]): Unit = {
    //这个地方设置local[3]，为什么呢？因为这个程序里面有两个socket的receiver
    //每个receiver都占用了一个cpu，所以设置cpu必须大于2
    //不然没有可以负责任务运行的cpu了
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingFile").setMaster("local[3]")
    val ssc: StreamingContext = new StreamingContext(conf, Durations.seconds(5))

    //生成两个socket流
    val lines1: ReceiverInputDStream[String] = ssc.socketTextStream("s5.hadoop", 6666)
    val lines2: ReceiverInputDStream[String] = ssc.socketTextStream("s5.hadoop", 6667)

//    // 方式一
//    val unionStream: DStream[String] = lines1.union(lines2)

//    // 方式二
//    val seq: Seq[ReceiverInputDStream[String]] = Seq(lines1, lines2)
//    val unionStream: DStream[String] = ssc.union(seq)

    //合拼两个socket流
    val bu: ListBuffer[DStream[String]] = new ListBuffer[DStream[String]]
    bu += lines1
    bu += lines2
    val unionStream: DStream[String] = ssc.union(bu)

    // 打印
    unionStream.foreachRDD(r => {r.foreach(println)})

    ssc.start()
    ssc.awaitTermination()
  }
}
