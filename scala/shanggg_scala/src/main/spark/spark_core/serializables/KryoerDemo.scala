package spark_core.serializables

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// Kryo 序列化
//noinspection DuplicatedCode
object KryoerDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
      .setAppName("SerDemo")
      .setMaster("local[*]")
      // 替换默认的序列化机制 可以省(如果调用registerKryoClasses
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      // 注册需要使用 kryo 序列化的自定义类
      .registerKryoClasses(Array(classOf[Searcher1]))
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[String] = sc.parallelize(
      Array("hello world", "hello atguigu", "atguigu", "hahah"), 2)
    val searcher1: Searcher1 = new Searcher1("hello")
    val result: RDD[String] = searcher1.getMatchedRDD1(rdd)
    result.collect.foreach(println)
  }
}

case class Searcher1(val query: String) {
  // 判断 s 中是否包括子字符串 query
  def isMatch(s: String): Boolean = {
    s.contains(query)
  }
  // 过滤出包含 query字符串的字符串组成的新的 RDD
  def getMatchedRDD1(rdd: RDD[String]): RDD[String] = {
    rdd.filter(isMatch) //
  }
  // 过滤出包含 query字符串的字符串组成的新的 RDD
  def getMatchedRDD2(rdd: RDD[String]): RDD[String] = {
    val q: String = query
    rdd.filter(_.contains(q))
  }
}