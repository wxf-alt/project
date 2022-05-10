package spark_core.word_count

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 排序 测试
object WordCountSort {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCountSort")
    val sc: SparkContext = new SparkContext(conf)
    val path: String = """E:\A_data\4.测试数据\输入\wordcount"""
    val inputRdd: RDD[String] = sc.textFile(path)
    val flatMapRdd: RDD[String] = inputRdd.flatMap(_.split("\t"))
    val mapRdd: RDD[(String, Int)] = flatMapRdd.map((_, 1))
    val reduceByKeyRdd: RDD[(String, Int)] = mapRdd.reduceByKey(_ + _)
    val sortByRdd: RDD[(String, Int)] = reduceByKeyRdd.sortBy(_._2, false)
    val array: Array[(String, Int)] = sortByRdd.collect()
    println(array.toBuffer)
    println(sortByRdd.toDebugString)
  }
}
