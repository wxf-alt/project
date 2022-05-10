package spark_streaming.streaming

import java.io.File

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object SparkStreamingFile {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("SparkStreamingFile")
    //设置读取多长时间范围内的文件
    conf.set("spark.streaming.fileStream.minRememberDuration", "2592000s")
    //每5秒去检查一次你指定的目录
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))
    val hadoopConf: Configuration = ssc.sparkContext.hadoopConfiguration
    hadoopConf.set("fs.defaultFS", "file:///")

    val localPath: String = "E:\\A_data\\4.测试数据\\输入\\newFileInput"
    // 存储目录下所有文件
    val fileNameSet: mutable.Set[String] = mutable.Set[String]()
    // 将目录下所有的文件名 添加到 Set 集合中
    val file: File = new File(localPath)
    val files: Array[File] = file.listFiles()
    for (elem <- files) {
      if (elem.isFile) {
        fileNameSet += elem.getName
      }
    }
    println("fileNameSet:" + fileNameSet)

    val fileDstream: InputDStream[(LongWritable, Text)] = ssc.fileStream[LongWritable, Text, TextInputFormat](localPath,
      (path: Path) => {
        // 判断是否是新文件  如果是新文件输出 文件名
        if(!(fileNameSet.contains(path.getName))){
          println("新文件：" + localPath + "\\" +  path.getName)
          fileNameSet += path.getName
        }
        path.getName.endsWith(".txt")
      }, newFilesOnly = false, hadoopConf)

    val flatMap: DStream[String] = fileDstream.flatMap(_._2.toString.split(" "))
    //fileStream里的RDD有多少个partition是由inputFormat类的getSplits方法决定的
    flatMap.foreachRDD(r => println(r.getNumPartitions))
    val map: DStream[(String, Int)] = flatMap.map((_, 1))
    val wordCount: DStream[(String, Int)] = map.reduceByKey(_ + _)
    wordCount.foreachRDD((r, t) => {
      println(s"count time:${t},${r.collect().toList}")
    })
    ssc.start()
    ssc.awaitTermination()
  }
}
