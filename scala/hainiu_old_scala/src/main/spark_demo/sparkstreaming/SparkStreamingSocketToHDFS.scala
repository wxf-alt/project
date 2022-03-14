package sparkstreaming

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataOutputStream, FileSystem, Path}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}

import scala.collection.mutable.ArrayBuffer

// SparkStreaming输出到HDFS
/*
    考虑的问题是可能会产生很多小文件，小文件带来的问题是什么？占用很大的namenode的元数据空间，下游使用小文件的JOB会产生很多个partition，如果是mr任务就会有很多个map，如果是spark任务就会有很多个task，那如何解决？
    可以使用的方法有4种
    1、增加batch大小  (时间间隔)
    2、使用批处理任务进行小文件的合并  (影响性能)
    3、使用HDFS的append方式
    4、使用coalesce
1和2是不建议使用的
*/

object SparkStreamingSocketToHDFS {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingSocketToHDFS").setMaster("local[*]")
    val ssc: StreamingContext = new StreamingContext(conf, Durations.seconds(5))

    val inputStream: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 6666)
    val reduceByKey: DStream[(String, Int)] = inputStream.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)

    reduceByKey.foreachRDD(f => {
      if (!f.isEmpty()) {
        //可以使用coalesce减少输出的文件数，但是不建议设置为1，因为如果为1就把并行计算变成单机的了
        //这里为什么用mapPartitionsWithIndex呢，因为可以每个partition的task拿自己的partitionId
        //从而把partitionId拼接到输出文件的文件名上，以防止各个task之间写文件的冲突
        val value: RDD[String] = f.coalesce(5).mapPartitionsWithIndex((partitionID, e) => {
          // 开始写文件
          val hadoop_conf: Configuration = new Configuration()
          val fs: FileSystem = FileSystem.get(hadoop_conf)

          val list: List[(String, Int)] = e.toList
          if (list.length > 0) {
            //这里为什么生成每小时的字符串呢？
            //因为可以拼接到文件名上，用以判断每小时的文件是否存在
            //如果存在就append，不存在就创建
            val format: String = new SimpleDateFormat("yyyyMMddHH").format(new Date)
            // 设置 路径
            val path = new Path(s"C:\\Users\\wxf\\Desktop\\4.测试数据\\输出\\SparkStreamingSocketToHDFS\\${partitionID}_${format}")
            println("path：" + path)
            //存在就是append流，不存在就是create流，生成create流的时候会自动创建文件
            //append流只支持集群的hdfs，不支持local模式的hdfs
            //所以如果local模式的hdfs，那在后续的append的数据就会给你报not support append
            val outputStream: FSDataOutputStream = if (fs.exists(path)) {
              fs.append(path)
            } else {
              fs.create(path)
            }

            list.foreach(ff => {
              outputStream.write(s"${ff._1}\t${ff._2}\n".getBytes("UTF-8"))
            })
            outputStream.close()
          }
          //这里为什么用一个action，因为刚才的mapPartitionsWithIndex是个transFormation
          //如果没有一个action，咱们的任务将不会执行，而刚才的所有逻辑都写在mapPartitionsWithIndex的function中
          //所以必须得写一个action用于任务的启动，这样mapPartitionsWithIndex的function的逻辑才会被执行
          new ArrayBuffer[String]().toIterator
        })
        value.foreach(f => Unit)
      }
    })
    ssc.start()
    ssc.awaitTermination()

  }
}
