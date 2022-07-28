package spark_core.transformation_test.Important_operator

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 最底层的聚合算子
object CombineByKeyDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("CombineByKeyDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(String, Int)] = sc.parallelize(List(("hainiu", 1), ("hainiu", 2), ("hainiu", 3), ("niu", 1), ("niu", 4), ("niu", 2), ("niu", 1)), 3)
    val rdd1: RDD[(String, Int)] = rdd.mapPartitionsWithIndex((index, it) => {
      val list: List[(String, Int)] = it.toList
      println(s"${index} : ${list}") // 2 : List((niu,4), (niu,2), (niu,1));1 : List((hainiu,3), (niu,1));0 : List((hainiu,1), (hainiu,2))
      list.toIterator
    })
    val rdd2: RDD[(String, Int)] = rdd1
      .combineByKey(x => x + 10, (a: Int, b: Int) => a + b, (m: Int, n: Int) => m + n)
    println(rdd2.collect.toList)  // List((hainiu,26), (niu,28))
  }
}
