package spark_core

import org.apache.avro.generic.GenericRecord
import org.apache.avro.mapred.{AvroInputFormat, AvroWrapper}
import org.apache.hadoop.io.NullWritable
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// RDD创建
object CreateRDD {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("CreateRDD").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)
    val path: String = """E:\A_data\4.测试数据\spark-core数据\agent.log"""
    // 文件加载
    val rdd: RDD[String] = sc.textFile(path)
    println(rdd.collect.toBuffer)

    // 读取 AVRO 文件
//    sc.hadoopFile[AvroWrapper[GenericRecord], NullWritable, AvroInputFormat[GenericRecord]]("")


//    val arr: Array[Int] = Array(1,2,3,4,5)
//    // 集合加载
//    val rdd1: RDD[Int] = sc.parallelize(arr)
//    println(rdd1.collect.toBuffer)
  }
}
