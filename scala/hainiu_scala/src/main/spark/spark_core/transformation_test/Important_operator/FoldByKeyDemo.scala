package spark_core.transformation_test.Important_operator

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 聚合算子
object FoldByKeyDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("FoldByKeyDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(String, Int)] = sc.parallelize(List(("hainiu", 1), ("hainiu", 2), ("niu", 1)), 2)
    val rdd1: RDD[(String, Int)] = rdd.mapPartitionsWithIndex((index, it) => {
      val list: List[(String, Int)] = it.toList
      println(s"${index} : ${list}")  //  0 : List((hainiu,1))  1 : List((hainiu,2), (niu,1))
      list.toIterator
    })
    val foldRdd: RDD[(String, Int)] = rdd1.foldByKey(10)(_ + _)
    println(foldRdd.collect.toList)  // List((niu,11), (hainiu,23))
  }
}
