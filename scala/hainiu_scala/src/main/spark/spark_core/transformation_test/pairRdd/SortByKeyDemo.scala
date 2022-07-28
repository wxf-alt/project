package spark_core.transformation_test.pairRdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SortByKeyDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SortByKeyDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(String, Int)] =sc.parallelize(List(("a", 1),("c",3),("b",2)))
    rdd.sortByKey().collect.toList.foreach(print)  // (a,1)(b,2)(c,3)
    println()
    rdd.sortByKey(false).collect.toList.foreach(print)  // (c,3)(b,2)(a,1)
  }
}
