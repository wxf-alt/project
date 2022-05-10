package spark_core.transformation_test.Important_operator

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

// 广播变量和累加器
object BroadAccDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("BroadAccDemo")
    val sc: SparkContext = new SparkContext(conf)
    // 局部变量
    val list: List[Int] = List[Int](1, 3, 5)

    // 创建累加器
    //    val acc: LongAccumulator = sc.longAccumulator
    val acc: LongAccumulator = sc.longAccumulator("acc")
    // 封装外部变量到广播变量
    val broadCastList: Broadcast[List[Int]] = sc.broadcast(list)

    val rdd: RDD[Int] = sc.parallelize(List[Int](1, 2, 5, 3, 3, 6), 2)
    val filterRdd: RDD[Int] = rdd.filter(f => {
      val broad: List[Int] = broadCastList.value
      if (broad.contains(f)) {
        acc.add(1)
        true
      } else {
        false
      }
    })
//      .cache()
    println(filterRdd.collect.toList) // List(1, 5, 3, 3)
    // 注意: 在一个程序里面，如果有多个 action 操作，容易会重复计数
    // 可以利用缓存 解决
    println(filterRdd.count())    // 4
    println(s"acc: ${acc.value}") // 4  -- 重复计数 8
  }
}
