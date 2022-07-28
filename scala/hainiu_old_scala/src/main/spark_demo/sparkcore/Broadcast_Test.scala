package sparkcore

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

// 8.广播变量 broadcast
object Broadcast_Test {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("8- broadcast 广播变量操作").setMaster("local[10]")
    val sc: SparkContext = new SparkContext(conf)

    // 不使用 累加器和广播变量情况  拿不到 Executor 返回的数据
    //    val rdd: RDD[Int] = sc.parallelize(List[Int](1,2,5,3,3,6),2)
    //    // 外部变量
    //    var num:Int = 0;
    //    // 外部变量
    //    val list: List[Int] = List[Int](1, 3, 5)
    //    // rdd.filter()的函数是在executor端运行
    //    val filter: RDD[Int] = rdd.filter(f => {
    //      if (list.contains(f)){
    //        // 数据可以计数，但driver端拉取不到这个外部变量
    //        num += 1
    //        true
    //      }  else {
    //        false
    //      }
    //    })
    //    val arr: Array[Int] = filter.collect()
    //    println(arr.toBuffer)
    //    // 由于 driver端拉取不到这个外部变量，所以 num = 0
    //    println(s"num:${num}")

    val rdd: RDD[Int] = sc.parallelize(List[Int](1, 2, 5, 3, 3, 6), 2)
    // 设置 累加器
    val acc: LongAccumulator = sc.longAccumulator
    // 设置广播变量
    val list: List[Int] = List[Int](1, 3, 5)
    val broadCast: Broadcast[Unit] = sc.broadcast(list)
    val filterRdd: RDD[Int] = rdd.filter(f => {
      if (list.contains(f)) {
        acc.add(1L)
        true
      } else {
        false
      }
    })
    val arr: Array[Int] = filterRdd.collect()
    println(arr.toBuffer)
    acc.reset()
    //  注意，在一个程序里，如果有多个action操作，容易重复计数  --> 可以在两个 action 算子之间调用累加器的 reset() 方法进行重置
    filterRdd.count()
    println(s"num:${acc.value}")


  }
}
