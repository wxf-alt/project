package spark_streaming.streaming

import java.io.File

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}

import scala.collection.mutable

object SparkStreamingSocketFileCogroup {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingFile").setMaster("local[*]")
    conf.set("spark.streaming.fileStream.minRememberDuration", "2592000s")
    val ssc: StreamingContext = new StreamingContext(conf, Durations.seconds(5))

    val hadoopConf: Configuration = ssc.sparkContext.hadoopConfiguration
    hadoopConf.set("fs.defaultFS", "file:///")

    val localPath: String = "E:\\A_data\\4.测试数据\\输入\\coGroup"
    val fileNameSet: mutable.Set[String] = mutable.Set[String]()
    val file: File = new File(localPath)
    val files: Array[File] = file.listFiles()
    for (elem <- files) {
      if (elem.isFile) {
        fileNameSet += elem.getName
      }
    }
    println(fileNameSet)

    // socket 流
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("s5.hadoop", 6666)
    lines.foreachRDD(r => println(r.collect.toList))
    val countryCountSocket: DStream[(String, Int)] = lines.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
    countryCountSocket.foreachRDD(r => println(r.collect.toList))

    // 文件流
    val fileDstream: InputDStream[(LongWritable, Text)] = ssc.fileStream[LongWritable, Text, TextInputFormat](localPath,
      (path: Path) => {
        // 判断是否是新文件  如果是新文件输出 文件名
        if (!(fileNameSet.contains(path.getName))) {
          println("新文件：" + localPath + "\\" + path.getName)
          fileNameSet += path.getName
        }
        path.getName.endsWith(".txt")
      }, newFilesOnly = false, hadoopConf)

    // 解析 文件流中数据
    val countryDictFile: DStream[(String, String)] = fileDstream.map(a => {
      val strings: Array[String] = a._2.toString.split(" ")
      (strings(0), strings(1))
    })
    //    // 这里可以使用 mapPartition 进行优化，这样可以少创建Pattern对象
    //    val countryDictFile: DStream[(String, String)] = fileDstream.mapPartitions(it => {
    //      val pattern: Pattern = new Pattern(" ", 0)
    //      val tuples: ListBuffer[(String, String)] = new ListBuffer[(String, String)]
    //      for (elem <- it) {
    //        val string: String = elem._2.toString
    //        val strings: Array[String] = pattern.split(string, 0)
    //        tuples += ((strings(0), strings(1)))
    //      }
    //      tuples.iterator
    //    })
    countryDictFile.foreachRDD(r => println(r.collect.toList))

    //    // 这个相当于sql中的join只有在两个流都有相同的key的时候，才会打印结果
    //    val value: DStream[(String, (Int, String))] = countryCountSocket.join(countryDictFile)
    //    value.foreachRDD((r, t) => {
    //      println(s"count time:${t}")
    //      r.foreach(f => {
    //        val city: String = f._1
    //        val region: String = f._2._2
    //        val num: Int = f._2._1
    //        println(s"城市名称:${city},所在地区:${region},出现次数:${num}")
    //      })
    //    })

    // 这个相当于sql中的full join在两个流都有相同的key的时候，相同的数据就join在一起
    // 如果没有相同的数据，有数据的那个流的value也会进入到函数中
    countryCountSocket.cogroup(countryDictFile)
      .foreachRDD((r, t) => {
        println(s"count time:${t}")
        r.foreach(f => {
          val city: String = f._1
          val region: Iterable[String] = f._2._2
          val num: Iterable[Int] = f._2._1
          println(s"城市名称:${city},所在地区:${region},出现次数:${num}")
        })
      })

    ssc.start()
    ssc.awaitTermination()
  }
}