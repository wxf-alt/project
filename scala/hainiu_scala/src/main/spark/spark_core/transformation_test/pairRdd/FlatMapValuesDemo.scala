package spark_core.transformation_test.pairRdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object FlatMapValuesDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("FlatMapValuesDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(String, String)] =sc.parallelize(List(("hainiu", "a,b"),("hainiu","c,d"),("niu","e")))
    val rdd1: Array[(String, String)] = rdd.flatMapValues(s => s.split(",")).collect
    println(rdd1.toList)  // List((hainiu,a), (hainiu,b), (hainiu,c), (hainiu,d), (niu,e))
  }
}
