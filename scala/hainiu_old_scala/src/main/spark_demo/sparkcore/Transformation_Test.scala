package sparkcore

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

// 3.RDD操作 -> 转换操作
object Transformation_Test {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("3- RDD操作 -> 转换操作").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)

//    // map 操作
//    val rdd = sc.parallelize(List("1 2 3", "4 5 6"))
//    val map_rdd: RDD[Array[String]] = rdd.map(f => f.split(" "))
//    val map_result: Array[Array[String]] = map_rdd.collect()
//    for(a <- map_result){
//      println(a.toBuffer)
//    }

//    // flatMap 操作
//    val rdd = sc.parallelize(List("1 2 3", "4 5 6"))
//    val rdd1 = rdd.flatMap(s => s.split(" ")).collect
//    rdd1.foreach(println)

//    // filter 操作
//    val rdd = sc.parallelize(List(1,2,3,4))
//    val rdd1 = rdd.filter(s => s < 3).collect
//    rdd1.foreach(println)

//    // distinct 操作   --> 实际上使用的是 map((_,null)).reduceByKey(x,numPartitions).map(_._1)
//    val rdd = sc.parallelize(List(1,2,3,4,1,2,2))
//    val rdd1: RDD[Int] = rdd.distinct()
//    rdd1.foreach(println)

//    // map vs mapPartitionsWithIndex
//    val rdd = sc.parallelize(List(1,2,3,4,1,2,2),2)
//    val rdd2: RDD[Int] = rdd.mapPartitionsWithIndex((index, it) => {
//      // index: 代表分区号
//      // it: 代表分区对应的数据集
//      println(s"index:${index}")
//      it.map(f =>{
//        println(s"index:${index}, f:${f}")
//        f + 10
//      })
//    })
//    val arr: Array[Int] = rdd2.collect()
//    println(arr.toBuffer)

    println("键值对RDD操作 pair RDD")

    // groupByKey 操作
//    val rdd=sc.parallelize(List(("hainiu",1),("hainiu",2),("niu",1)))
//    println(rdd.groupByKey().collect.toBuffer)

//    // reduceByKey 操作
//    val rdd=sc.parallelize(List(("hainiu",1),("hainiu",2),("niu",1)))
//    println(rdd.reduceByKey(_ + _).collect.toBuffer)

//    // keys 操作
//    val rdd=sc.parallelize(List(("hainiu",1),("hainiu",2),("niu",1)))
//    println(rdd.keys.collect.toBuffer)

//    // values 操作
//    val rdd=sc.parallelize(List(("hainiu",1),("hainiu",2),("niu",1)))
//    println(rdd.values.collect.toBuffer)

//    // mapValues 操作
//    val rdd=sc.parallelize(List(("hainiu",1),("hainiu",2),("niu",1)))
//    println(rdd.mapValues(s => s + 1).collect.toBuffer)
//    println(rdd.map(f => (f._1,f._2 + 1)).collect.toBuffer)

//    // flatMapValues 操作
//    val rdd=sc.parallelize(List(("hainiu", "a,b"),("hainiu","c,d"),("niu","e")))
//    println(rdd.flatMapValues(s => s.split(" ")).collect.toBuffer)

//    // sortByKey 操作
//    val rdd=sc.parallelize(List(("a", 1),("c",3),("b",2)))
//    // 按 key 进行排序
//    println(rdd.sortBy(_._1, false).collect.toBuffer)
//    println(rdd.sortByKey(false).collect.toBuffer)

//    // cogroup 操作
//    val rdd1=sc.parallelize(List(("id01", "aa"),("id02","bb"),("id03","cc")))
//    val rdd2=sc.parallelize(List(("id01", 10),("id03",13),("id04",14)))
//    println(rdd1.cogroup(rdd2).collect().toBuffer)

//    // join 操作
//    val rdd1=sc.parallelize(List(("id01", "aa"),("id02","bb"),("id03","cc")))
//    val rdd2=sc.parallelize(List(("id01", 10),("id03",13),("id04",14),("id01", 20),("id01", 70),("id01", 80)))
//    println(rdd1.join(rdd2).collect.toBuffer)

//    // leftjoin 操作
//    val rdd1=sc.parallelize(List(("id01", "aa"),("id02","bb"),("id03","cc")))
//    val rdd2=sc.parallelize(List(("id01", 10),("id03",13),("id04",14)))
//    println(rdd1.leftOuterJoin(rdd2).collect.toBuffer)

//    // rightjoin 操作
//    val rdd1=sc.parallelize(List(("id01", "aa"),("id02","bb"),("id03","cc")))
//    val rdd2=sc.parallelize(List(("id01", 10),("id03",13),("id04",14)))
//    println(rdd1.rightOuterJoin(rdd2).collect.toBuffer)

    println("RDD 间操作" + "\n \t注意：类型要一致")

//    // union(other) 并集 操作   带有重复值
//    val rdd1 = sc.parallelize(List(1,2,3))
//    val rdd2 = sc.parallelize(List(3,4,5))
//    println(rdd1.union(rdd2).collect.toBuffer)

//    // intersection(other) 交集 操作
//    val rdd1 = sc.parallelize(List(1,2,3))
//    val rdd2 = sc.parallelize(List(3,4,5))
//    println(rdd1.intersection(rdd2).collect.toBuffer)

//    // subtract(other) 差集 操作
//    val rdd1 = sc.parallelize(List(1,2,3))
//    val rdd2 = sc.parallelize(List(3,4,5))
//    println("rdd1 差 rdd2: \t" + rdd1.subtract(rdd2).collect.toBuffer)
//    println("rdd2 差 rdd1: \t" + rdd2.subtract(rdd1).collect.toBuffer)

//    // cartesian(other)  笛卡尔积 操作 --> 相当于 全连接
//    val rdd1 = sc.parallelize(List(1,2,3))
//    val rdd2 = sc.parallelize(List(3,4,5))
//    println(rdd1.cartesian(rdd2).collect.toBuffer)

    println("其他转换操作")
    println(
      """
        |reduceByKey  foldByKey  aggregateByKey  combineByKey
        |reduceByKey 无初始值的聚合操作
        |foldByKey  有初始值， 初始值和元素类型一致，  分区内的算法和分区间的算法是一致的
        |aggregateByKey  有初始值， 初始值和元素类型可以不一致，分区内的算法和分区间的算法可以不一致。
        |combineByKey： 什么都可以定制
        |""".stripMargin)

    // foldByKey 操作 --》 在reduceByKey 的基础上增加了初始值，初始值类型和元素的 值类型一致。
//    val rdd1 = sc.parallelize(List("dog", "wolf", "cat", "bear"), 2)
//    val rdd2 = rdd1.map(x => (x.length, x))
//    val resultRdd: RDD[(Int, String)] = rdd2.foldByKey("")(_ + _)
//    println(resultRdd.collect().toBuffer)

/*     分区1：
       a：10+1+2 = 13
       分区2：
       a:  10 + 2 = 12
       b:  10 + 1 = 11
       最终结果：
       a: 13 + 12 = 25
       b:  11
       */
//    val rdd: RDD[(String, Int)] = sc.parallelize(List(("a", 1), ("a", 2), ("b", 1), ("a", 2)), 2)
//    val resultRdd: RDD[(String, Int)] = rdd.foldByKey(10)(_ + _)
//    println(resultRdd.collect().toBuffer)

    // aggregateByKey 操作 --》
    /*对PairRDD中相同的Key值进行聚合操作，在聚合过程中同样使用了一个中立的初始值。和aggregate函数类似，aggregateByKey返回值的类型不需要和
    RDD中value的类型一致。因为aggregateByKey是对相同Key中的值进行聚合操作，所以aggregateByKey函数最终返回的类型还是PairRDD，对应的结果
    是Key和聚合后的值。
    */
//    val pairRDD = sc.parallelize(List( ("cat",2), ("cat", 5), ("mouse", 4),("cat", 12), ("dog", 12), ("mouse", 2)), 2)
//    def func2(index: Int, iter: Iterator[(String, Int)]) : Iterator[String] = {
//      iter.map(x => "[partID:" + index + ", val: " + x + "]")
//    }
//
//    println(pairRDD.mapPartitionsWithIndex(func2).collect.toBuffer)
//    // 1.零值
//    // 2.分区内的操作
//    // 3.分区间的操作
//    println(pairRDD.aggregateByKey(0)(math.max(_, _), _ + _).collect.toBuffer)

//    // combineByKey 操作 --》 spark的reduceByKey、aggregateByKey、foldByKey函数底层调用的都是 combinerByKey(现在换成了 combineByKeyWithClassTag)；他们都可以实现局部聚合再全局聚合；
////    combineByKey 其中：
////    createCombiner: V => C ，这个函数把当前的值作为参数，此时我们可以对其做些附加操作(类型转换)并把它返回 (这一步类似于初始化操作)
////    mergeValue: (C, V) => C，该函数把元素V合并到之前的元素C(createCombiner)上 (这个操作在每个分区内进行)
////    mergeCombiners: (C, C) => C，该函数把2个元素C合并 (这个操作在不同分区间进行)
//    val rdd1 = sc.parallelize(List(("aa",1), ("aa",2),("bb",2),("aa",3), ("bb",3),("cc",4)), 2)
//    val rdd2 = rdd1.combineByKey(x => x, (a: Int, b: Int) => a + b, (m: Int, n: Int) => m + n)
//    println(rdd2.collect().toBuffer)
//    val rdd3 = rdd1.combineByKey(x => x + 10, (a: Int, b: Int) => a + b, (m: Int, n: Int) => m + n)
//    println(rdd3.collect().toBuffer)

//    // filterByRange 操作 --》对pairRDD中的元素按照key进行过虑 包含 lower 和 upper
//    val rdd1 = sc.parallelize(List(("e", 5), ("c", 3), ("d", 4), ("c", 2), ("a", 1)))
//    val rdd2 = rdd1.filterByRange("b", "e")
//    println(rdd2.collect().toBuffer)


  }
}
