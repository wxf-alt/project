package spark_streaming.streaming

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataOutputStream, FileSystem, Path}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}

import scala.collection.mutable.ArrayBuffer

object SparkStreamingSocketPortHDFS {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("sparkstreamingsocketporthdfs").setMaster("local[2]")
    val ssc: StreamingContext = new StreamingContext(conf, Durations.seconds(5))

    // 处理数据
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("s5.hadoop", 6666)
    val reduceByKey: DStream[(String, Int)] = lines.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)

    // 写出数据
    reduceByKey.foreachRDD(rdd => {
      if (!rdd.isEmpty()) {
        // 可以使用coalesce减少输出的文件数，但是不建议设置为1，因为如果为1就把并行计算变成单机的了
        // 这里为什么用mapPartitionsWithIndex呢，因为可以每个partition的task拿自己的partitionId
        // 从而把partitionId拼接到输出文件的文件名上，以防止各个task之间写文件的冲突
        val value: RDD[String] = rdd.coalesce(2).mapPartitionsWithIndex((index, it) => {
          val configuration: Configuration = new Configuration()
          configuration.set("fs.defaultFS", "file:///")
          val fs: FileSystem = FileSystem.get(configuration)

          val list: List[(String, Int)] = it.toList
          if (list.nonEmpty) {
            // 这里为什么生成每小时的字符串呢？
            // 因为可以拼接到文件名上，用以判断每小时的文件是否存在
            // 如果存在就append，不存在就创建
            val formatDate: String = new SimpleDateFormat("yyyyMMddHH").format(new Date())
            val path: Path = new Path(s"E:/A_data/4.测试数据/输出/sparkstreamingsocketporthdfs/${index}_${formatDate}")

            // 存在就是append流，不存在就是create流，生成create流的时候会自动创建文件
            // append流只支持集群的hdfs，不支持local模式的hdfs
            // 所以如果local模式的hdfs，那在后续的append的数据就会给你报 not support append
            val outPutStream: FSDataOutputStream = if (fs.exists(path)) {
              fs.append(path)
            } else {
              fs.create(path)
            }

            // 开始写出
            list.foreach(ff => {
              outPutStream.write(s"${ff._1}\t${ff._2}\n".getBytes("UTF-8"))
            })
            outPutStream.close()
          }
          new ArrayBuffer[String]().toIterator
        })
        // 这里为什么用一个action，因为刚才的mapPartitionsWithIndex是个transFormation
        // 如果没有一个action，咱们的任务将不会执行，而刚才的所有逻辑都写在mapPartitionsWithIndex的function中
        // 所以必须得写一个action用于任务的启动，这样mapPartitionsWithIndex的function的逻辑才会被执行
        value.foreach(f => Unit)
      }
    })
    ssc.start()
    ssc.awaitTermination()
  }
}