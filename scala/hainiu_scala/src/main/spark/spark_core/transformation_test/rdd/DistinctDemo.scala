package spark_core.transformation_test.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object DistinctDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[Int] = sc.parallelize(List(1,2,3,4,1,2,2))
    val rdd1: Array[Int] = rdd.distinct().collect
    println(rdd1.toList)  // List(1, 2, 3, 4)
  }
}
