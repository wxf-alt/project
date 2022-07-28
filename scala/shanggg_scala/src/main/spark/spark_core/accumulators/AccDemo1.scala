package spark_core.accumulators

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object AccDemo1 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("AccDemo1").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(conf)
    val p1: Person = Person(10)
    // 将来会把对象序列化之后传递到每个节点上
    val rdd1: RDD[Person] = sc.parallelize(Array(p1))
    val rdd2: RDD[Person] = rdd1.map(p => {p.age = 100; p})

    rdd2.count()
    // 仍然是 10
    println(p1.age)
  }
}
case class Person(var age:Int)