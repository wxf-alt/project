package sparkcore

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 6.coalesce 和 repartition 都是用来对RDD的分区重新划分。
// coalesce 默认是没有shuffle
/*
  当分区由少变多时，需要执行shuffle，也就是父RDD与子RDD之间是宽依赖。可以用repartition或者coalesce,true都行，但是一定要有shuffle操作，
分区数量才会增加，为了让该函数并行执行，通常把shuffle的值设置成true；
        当分区由多变少时，不需要shuffle，也就是父RDD与子RDD之间是窄依赖。
        但极端情况下（1000个分区变成1个分区），这时如果将shuffle设置为false，父子RDD是窄依赖关系，他们同处在一个Stage中，就可能造成spark程序的并行度不够，
从而影响性能，如果1000个分区变成1个分区，为了使coalesce之前的操作有更好的并行度，可以将shuffle设置为true。
*/
object Coalesce_Repartition_Test {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("6- coalesce - repartition 操作").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)

    val rdd1 = sc.parallelize(1 to 10, 10)
    println(rdd1.partitions.length)
    val rdd2 = rdd1.coalesce(5)  // false ：不进行shuffle   默认false， 多分区转少分区用false 窄依赖
    println(rdd2.partitions.length)
    val rdd3: RDD[Int] = rdd2.repartition(8) // 少分区转多分区使用 repartition 宽依赖
    println(rdd3.partitions.length)
    println(rdd3.count())
  }
}
