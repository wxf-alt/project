package spark_core

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.spark.rdd.{JdbcRDD, RDD}
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.parsing.json.JSON


object CreateRddReadOther {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("CreateRddReadOther")
    val sc: SparkContext = new SparkContext(conf)
    // 读写Text 文件
    val rdd: RDD[String] = sc.textFile("")

    // 读写 SequenceFile 文件
    val rddSeq: RDD[(String, Int)] = sc.sequenceFile[String,Int]("")
    rddSeq.saveAsSequenceFile("")

    // 读写 Object 文件
    val rddObject: RDD[String] = sc.objectFile[String]("")
    rddObject.saveAsObjectFile("")

    // 读写 Hadoop 文件
//    val rddHadoop: RDD[(String, Int)] = sc.newAPIHadoopFile("")
//    rddHadoop.saveAsHadoopFile("")

  }

}
