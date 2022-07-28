package sparkcore

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


// 4.RDD操作 -> 执行操作
object Action_Test {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("4- RDD操作 -> 执行操作").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)

    //    // collect 操作 将executor上的pairRDD数据拉取回来并变成数组
    //    val rdd1 = sc.parallelize(List(("b", 2), ("a", 1)))
    //    val arr: Array[(String, Int)] = rdd1.collect()
    //    println(arr.toBuffer)

    //    // collectAsMap 将executor上的pairRDD数据拉取回来并变成map集合
    //    val rdd1 = sc.parallelize(List(("b", 2), ("a", 1)))
    //    val mapRdd = rdd1.collectAsMap()
    //    mapRdd.foreach(println)

    //    // count() RDD中的元素个数
    //    val rdd1 = sc.parallelize(List(("b", 2), ("a", 1),("a", 1)))
    //    println(rdd1.count())

    //    // countByValue() 各元素在RDD中出现的次数
    //    val rdd1 = sc.parallelize(List(("b", 2), ("a", 1),("a", 1)))
    //    println(rdd1.countByValue())  // --> Map((a,1) -> 2, (b,2) -> 1)

    //    // take(n) 从RDD中返回 n 个元素 返回Array
    //    val rdd1 = sc.parallelize(List(("b", 2), ("a", 1),("a", 1)))
    //    println(rdd1.take(2).toBuffer)
    //    rdd1.take(2)

    //    // first() 从RDD中返回第一个元素  返回元组
    //    val rdd1 = sc.parallelize(List(("b", 2), ("a", 1),("a", 1)))
    //    println(rdd1.first())

    //    // top(num) 返回最大的 num 个元素 返回Array
    //    val rdd1 = sc.parallelize(List(("b", 2), ("a", 1),("a", 1)))
    //    println(rdd1.top(2).toBuffer)

    //    // takeOrdered(num)(ordering) 按照指定顺序返回前面num个元素
    //    val rdd1 = sc.parallelize(List(("b", 2), ("a", 1),("a", 1)))
    //    println(rdd1.takeOrdered(2).toBuffer)

    //    // reduce(func) 通过函数func（输入两个参数并返回一个值）聚合数据集中的元素，比如求和
    //    val rdd1: RDD[Int] = sc.parallelize(List(1, 2, 3, 3))
    //    println(rdd1.reduce(_ + _))

//    // fold 和reduce一样，给定初值,每个分区计算时都会使用此初值
//    /*    （zeroValue+分区1的数据） 得到分区1中间结果
//          （zeroValue+分区2的数据） 得到分区2中间结果
//           zeroValue + （zeroValue+分区1的数据）+ （zeroValue+分区2的数据）
//    */
//    val rdd1: RDD[Int] = sc.parallelize(List(1, 2, 3, 3),2)
//    println(rdd1.fold(10)(_ + _))

//    // aggregate  和reduce类似，但可以返回类型不同的结果
//    val rdd1 = sc.parallelize(List(1,2,3,4,5,6,7,8,9), 2)
//    def func1(index: Int, iter: Iterator[(Int)]) : Iterator[String] = {
//      iter.toList.map(x => "[partID:" + index + ", val: " + x + "]").iterator
//    }
//
//    println(rdd1.mapPartitionsWithIndex(func1).collect.toBuffer)
//    println(rdd1.aggregate(0)(math.max(_, _), _ + _)) //0 +（4）+ （9） =13
//    println(rdd1.aggregate(5)(math.max(_, _), _ + _)) //5 + (5) + (9) = 19

//    // foreach 对每个元素使用func函数
//    val rdd1: RDD[Int] = sc.parallelize(List(1, 2, 3, 3))
//    rdd1.foreach(println(_))
//    println("===================")
//    rdd1.foreach(println)

    // foreachPartition 按照分区 对每个元素使用func函数
    val rdd1 = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 3)
    rdd1.foreachPartition(x => println(x.reduce(_ + _)))
    Thread.sleep(1000000000)


  }
}
