package sparkstreaming

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}

// 监控目录的文件的读入，目录进入新文件时，新的文件就会参与本批次运算，
// 本批次运算完成之后这个文件就等于已经处理过了，下个批次就不会再重复读取了，
// 但是注意：进入到这个目录的文件必须保证原子性
object SparkStreamingFile {
  def main(args: Array[String]): Unit = {
    //local[1]也好使，说明fileStream不需要receiver
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingFile").setMaster("local[1]")
    //设置读取多长时间范围内的文件
    conf.set("spark.streaming.fileStream.minRememberDuration", "2592000s")
    //每5秒去检查一次你指定的目录
    val ssc: StreamingContext = new StreamingContext(conf, Durations.seconds(5))

    // 设置读取目录
    val localPath: String = "E:\\A_data\\4.测试数据\\输入\\newFileInput"
    val hadoop_conf: Configuration = new Configuration()

    val fileDStream: InputDStream[(LongWritable, Text)] = ssc.fileStream[LongWritable, Text, TextInputFormat](localPath,
      // 设置文件的过滤条件
      (path: Path) => {
        println(path)
        path.getName.endsWith(".txt")
        // param newFilesOnly应仅处理新文件，而忽略目录中的现有文件
      }, true, hadoop_conf
    )

    val flatMap: DStream[String] = fileDStream.flatMap(_._2.toString.split(" "))
    //fileStream里的RDD有多少个partition是由inputFormat类的getSplits方法决定的
    flatMap.foreachRDD(r => println(r.getNumPartitions))
    val map: DStream[(String, Int)] = flatMap.map((_,1))
    val wordCount: DStream[(String, Int)] = map.reduceByKey(_ + _)
    wordCount.foreachRDD((r,t) => {
      println(s"count time:${t},${r.collect().toList}")
    })

    ssc.start()
    ssc.awaitTermination()



  }
}
