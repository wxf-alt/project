package spark_core.transformation_test.pairRdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object MapValuesDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("KeysValuesDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(String, Int)] =sc.parallelize(List(("hainiu",1),("hainiu",2),("niu",1)))
    val rdd1: RDD[(String, Int)] = rdd.mapValues(x => x * 10)
    println(rdd1.collect.toList)  // List((hainiu,10), (hainiu,20), (niu,10))
  }
}
