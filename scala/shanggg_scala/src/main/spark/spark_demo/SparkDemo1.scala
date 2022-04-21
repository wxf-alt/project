package spark_demo

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SparkDemo1 {
  def main(args: Array[String]): Unit = {
    // 1.创建一个 SparkContext
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("SparkDemo1")
    val sc: SparkContext = new SparkContext(conf)

    // 2.从数据源得到一个 RDD
    val rdd: RDD[String] = sc.textFile(args(0))

    // 3.对 RDD 进行转换
    val resultRdd: RDD[(String, Int)] = rdd.flatMap(_.split("\t"))
      .map((_, 1))
      .reduceByKey(_ + _)

    // 4.执行行动算子
    println(resultRdd.collect().toBuffer)

    // 关闭 SparkContext
    sc.stop()
  }
}
