package spark_core.transformation_test.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object MapPartitionsDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("MapPartitionsDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[Int] = sc.parallelize(Array[Int](1,2,3,4),2)
    val rdd1: RDD[Int] = rdd.mapPartitions(it => {
      println(s"===================")
      it
    })
    println(rdd1.collect.toList)

  }
}
