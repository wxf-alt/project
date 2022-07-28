package sparkstreaming

import java.util.regex.Pattern

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}

// 文件流 与 socket流 合并
object SparkStreamingSocketCogroupFile {
  def main(args: Array[String]): Unit = {
    //local[1]也好使，说明fileStream不需要receiver
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingFile").setMaster("local[*]")
    //设置读取多长时间范围内的文件
    conf.set("spark.streaming.fileStream.minRememberDuration", "2592000s")

    val localPath: String = "C:\\Users\\wxf\\Desktop\\4.测试数据\\输入\\SparkStream"
    val hadoop_conf: Configuration = new Configuration()

    val ssc: StreamingContext = new StreamingContext(conf, Durations.seconds(5))

    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 6666)
    val countryCountSocket: DStream[(String, Int)] = lines.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)

    // 文件流
    val fileStream: InputDStream[(LongWritable, Text)] = ssc.fileStream[LongWritable, Text, TextInputFormat](localPath,
      (path: Path) => {
        println(path.getName)
        path.getName.endsWith(".conf")
      }, true, hadoop_conf
    )

    val countryDictFile: DStream[(String, String)] = fileStream.map((a: (LongWritable, Text)) => {
      //这里可以使用mapPartition，new Pattern("\t",0).split("CN\t中国\tAndorra",0) 进行优化，这样可以少创建Pattern对象
      //      fileDStream.mapPartitions(t => {
      //        val pattern = new Pattern("\t",0)
      //        val tuples = new ListBuffer[(String,String)]
      //
      //        for(s <- t){
      //          val string: String = s._2.toString
      //          val strings: Array[String] = pattern.split(string,0)
      //          tuples+= ((strings(0), strings(1)))
      //        }
      //        tuples.iterator
      //      })
      val strings: Array[String] = a._2.toString.split("\t")
      (strings(0), strings(1))
    })

    //这个相当于sql中的join只有在两个流都有相同的key的时候，才会打印结果
    //    countryCountSocket.join(countryDictFile).foreachRDD((r,t) => {
    //      println(s"count time:${t}")
    //      r.foreach(f=> {
    //        val countryCode: String = f._1
    //        val countryCount: Int = f._2._1
    //        val countryName: String = f._2._2
    //        println(s"countryCode:${countryCode},countryCount:${countryCount},countryName:${countryName}")
    //      })
    //    })

    //这个相当于sql中的 left join在两个流都有相同的key的时候，相同的数据就join在一起
    //如果没有相同的数据，有数据的那个流的value也会进入到函数中
    countryCountSocket.cogroup(countryDictFile).foreachRDD((r, t) => {
      println(s"count time:${t}")
      r.foreach(f => {
        val countryCode: String = f._1
        val countryCount: Iterable[Int] = f._2._1
        val countryName: Iterable[String] = f._2._2
        println(s"countryCode:${countryCode},countryCount:${countryCount},countryName:${countryName}")
      })
    })

    ssc.start()
    ssc.awaitTermination()

  }
}
