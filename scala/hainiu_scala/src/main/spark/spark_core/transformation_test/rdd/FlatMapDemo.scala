package spark_core.transformation_test.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// FlatMap 测试
object FlatMapDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("FlatMapDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[String] = sc.parallelize(List("1 2 3", "4 5 6"))
    val rdd1: Array[String] = rdd.flatMap(s => s.split(" ")).collect
    println(rdd1.toBuffer)  // ArrayBuffer(1, 2, 3, 4, 5, 6)
  }
}
