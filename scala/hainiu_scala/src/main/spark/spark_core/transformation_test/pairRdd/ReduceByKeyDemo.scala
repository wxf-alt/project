package spark_core.transformation_test.pairRdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object ReduceByKeyDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("ReduceByKeyDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(String, Int)] =sc.parallelize(List(("hainiu",1),("hainiu",2),("niu",1)))
    val rdd1: Array[(String, Int)] = rdd.reduceByKey(_ + _ ).collect    // _ + _ 等价于 (a,b) => a+b
    println(rdd1.toList)  // List((niu,1), (hainiu,3))
  }
}
