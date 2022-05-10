package spark_core.transformation_test.pairRdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object GroupByKeyDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("GroupByKeyDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(String, Int)] =sc.parallelize(List(("hainiu",1),("hainiu",2),("niu",1)))
    val rdd1: Array[(String, Iterable[Int])] = rdd.groupByKey().collect
    println(rdd1.toList)  // List((niu,CompactBuffer(1)), (hainiu,CompactBuffer(1, 2)))
  }
}
