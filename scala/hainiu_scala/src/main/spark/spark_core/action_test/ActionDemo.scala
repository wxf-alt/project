package spark_core.action_test

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 行动输出算子
object ActionDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("ActionDemo").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)
    val list: List[Int] = List(1, 2, 3, 4, 4, 3, 5, 2, 1, 1, 2, 1)
    val array: Array[(String, Int)] = Array(("a", 1), ("b", 2))
    val rdd1: RDD[Int] = sc.parallelize(list)
    val rdd2: RDD[(String, Int)] = sc.parallelize(array)

    // collect
    val collect: Array[Int] = rdd1.collect
    println(collect.toBuffer) // ArrayBuffer(1, 2, 3, 4, 4, 3, 5, 2, 1, 1, 2, 1)

    // collectAsMap  只适用于 PairRDD
    val map: collection.Map[String, Int] = rdd2.collectAsMap()
    map.foreach(println) // (b,2) (a,1)

    // count
    val count: Long = rdd1.count()
    println(count)  // 12

    // countByValue  返回 Map 类型结果
    val countByValue: collection.Map[Int, Long] = rdd1.countByValue()
    println(countByValue.toList.sortBy(_._1)) // List((1,4), (2,3), (3,2), (4,2), (5,1))

    // take
    val takes: Array[Int] = rdd1.take(5)
    println(takes.mkString(","))  // 1,2,3,4,4

    // first
    val firstNum: Int = rdd1.first()
    println(firstNum) // 1

    // top  返回最大的 num 个元素
    val tops: Array[Int] = rdd1.top(3)
    println(tops.mkString(","))  // 5,4,4

    // takeOrdered  按照指定顺序返回前面num个元素
    val takeOrders: Array[Int] = rdd1.takeOrdered(3)(Ordering.Int.reverse)
    println(takeOrders.mkString(","))  // 5,4,4
  }
}
