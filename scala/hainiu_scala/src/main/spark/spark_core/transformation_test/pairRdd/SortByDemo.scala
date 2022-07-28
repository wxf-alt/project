package spark_core.transformation_test.pairRdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SortByDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SortByDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(String, Int)] =sc.parallelize(List(("a", 1),("c",3),("b",2)))
    val rdd1: RDD[(String, Int)] = rdd.sortBy(_._2)  // 按照 value 进行升序
    val rdd2: RDD[(String, Int)] = rdd.sortBy(_._2, false) // 按照 value 进行降序
    println(rdd1.collect.toList)  // List((a,1), (b,2), (c,3))
    println(rdd2.collect.toList)  // List((c,3), (b,2), (a,1))


  }
}
