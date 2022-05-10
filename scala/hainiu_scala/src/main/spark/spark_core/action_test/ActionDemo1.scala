package spark_core.action_test

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 行动聚合算子
object ActionDemo1 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("ActionDemo").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)
    val list: List[Int] = List(1, 2, 3, 4, 4, 3, 5, 2, 1, 1, 2, 1)
    val array: Array[(String, Int)] = Array(("a", 1), ("b", 2), ("c", 6), ("b", 3), ("b", 1), ("a", 7), ("d", 4))
    val rdd1: RDD[Int] = sc.parallelize(list, 3)
    val rdd2: RDD[(String, Int)] = sc.parallelize(array)

    // reduce
    val reduceResult: Int = rdd1.reduce(_ + _)
    println(reduceResult) // 29

    // fold
    // 给定初值,每个分区计算时都会使用此初值;分区间计算也需要加上 初始值
    val foldResult: Int = rdd1.fold(10)(_ + _)
    println(foldResult) // 69

    // aggregate
    val aggregateResult: Int = rdd1.aggregate(10)(_ + _, _ + _)
    println(aggregateResult) // 69
  }
}
