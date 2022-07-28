package spark_core.transformation_test.Important_operator

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 范围过滤
object FilterByRangeDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("FoldByKeyDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd1: RDD[(String, Int)] = sc.parallelize(List(("e", 5), ("c", 3), ("d", 4), ("b", 2), ("a", 1)))
//    val filterRdd: RDD[(String, Int)] = rdd1.filterByRange("b", "d")
    // 也可以使用 filter + 条件 实现
    val filterRdd: RDD[(String, Int)] = rdd1.filter {
      case (x, y) => if (x.compareTo("b") >= 0 && x.compareTo("d") <= 0) true else false
      case _ => false
    }
    println(filterRdd.collect.toList)  // List((c,3), (d,4), (b,2))
  }
}
