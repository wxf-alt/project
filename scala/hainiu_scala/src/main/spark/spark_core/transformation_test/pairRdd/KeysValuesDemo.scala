package spark_core.transformation_test.pairRdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object KeysValuesDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("KeysValuesDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(String, Int)] =sc.parallelize(List(("hainiu",1),("hainiu",2),("niu",1)))
    val keys: Array[String] = rdd.keys.collect
    println(keys.toList)  // List(hainiu, hainiu, niu)
    val values: Array[Int] = rdd.values.collect
    println(values.toList)  // List(1, 2, 1)
  }
}
