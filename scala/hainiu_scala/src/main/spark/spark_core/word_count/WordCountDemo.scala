package spark_core.word_count

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCountDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wordcount")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[String] = sc.textFile("E:/A_data/4.测试数据/spark-core数据/agent.log")
    // reduceByKey是 按照key分组，算出对应value的统计结果
    val reduceByKeyRdd: RDD[(String, Int)] = rdd.flatMap(_.split(" "))
      .map((_,1))
      .reduceByKey(_ + _)
      .coalesce(1)
//      .repartition(5)

    val outputPath:String = "E:/A_data/4.测试数据/输出/word_count"
    // 通过隐式转换删除输出目录
    outputPath.deletePath()
    // 将wordcount结果写入到hdfs中，以TextOutputFormat格式写入
    println(reduceByKeyRdd.toDebugString)
    println(reduceByKeyRdd.getNumPartitions)
    reduceByKeyRdd.saveAsTextFile(outputPath)

//    Thread.sleep(1000000)
  }


  implicit class HdfsDelete(path:String) {
    def deletePath():Unit = {
      val conf: Configuration = new Configuration
      val fs: FileSystem = FileSystem.get(conf)
      val outputPath: Path = new Path(path)

      if(fs.exists(outputPath)){
        fs.delete(outputPath,true)
        println(s"delete outputPath:${path} success")
      }
    }
  }
}


