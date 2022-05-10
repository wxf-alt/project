package spark_core

import java.util.Random

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 1.获取 RDD
object CreateRdd {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("CreateRdd")
    val sc: SparkContext = new SparkContext(conf)
    val rdd1: RDD[String] = sc.parallelize(Array("a", "b", "c", "d", "e"))
    //    val rdd1: RDD[String] = sc.makeRDD(Array("a", "b", "c", "d", "e"))
    val value: RDD[(String, Long)] = rdd1.zipWithIndex()
    println(value.collect().toBuffer)

    Thread.sleep(1000000)
    sc.stop()
  }
}
