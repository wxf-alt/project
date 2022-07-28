package spark_core.broadcast

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object BroadCastDemo1 {
  def main(args: Array[String]): Unit = {
    val bigArr: Array[Int] = 1 to 1000 toArray
    val conf: SparkConf = new SparkConf().setAppName("BroadCastDemo1").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(conf)
    // 设置 广播变量
    val broad: Broadcast[Array[Int]] = sc.broadcast(bigArr)
    val list: List[Int] = List(30, 500, 70, 60000, 10, 20)
    val rdd: RDD[Int] = sc.parallelize(list)
    val rdd2: RDD[Int] = rdd.filter(x => broad.value.contains(x))
    rdd2.foreach(x => print(x + "\t"))

  }
}
