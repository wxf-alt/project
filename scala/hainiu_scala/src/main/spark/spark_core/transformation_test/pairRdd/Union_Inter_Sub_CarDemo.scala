package spark_core.transformation_test.pairRdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// rdd 间操作
// 并集,交集,差集,笛卡尔积
object Union_Inter_Sub_CarDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("UnionDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd1: RDD[Int] = sc.parallelize(List(1, 2, 3))
    val rdd2: RDD[Int] = sc.parallelize(List(3, 4, 5))
    val unionRdd: RDD[Int] = rdd1.union(rdd2)
    println(unionRdd.collect.toList) // List(1, 2, 3, 4, 5, 6)

    val intersectionRdd: RDD[Int] = rdd1.intersection(rdd2)
    println(intersectionRdd.collect.toList) // List(3)

    val subtractRdd: RDD[Int] = rdd1.subtract(rdd2)
    println(subtractRdd.collect.toList) // List(1, 2)

    val cartesianRdd: RDD[(Int, Int)] = rdd1.cartesian(rdd2)
    println(cartesianRdd.collect.toList) // List((1,3), (1,4), (1,5), (2,3), (2,4), (2,5), (3,3), (3,4), (3,5))

    cartesianRdd.repartition(20)




  }
}
