package spark_core

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

// RDD 中 传递函数
object RddFunctionDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SerDemo").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[String] = sc.parallelize(
      Array("hello world", "hello atguigu", "atguigu", "hahah"), 2)
    val searcher: Searcher = new Searcher("hello")
    // 传递函数
//    val result: RDD[String] = searcher.getMatchedRDD1(rdd)

    // 传递变量
    val result: RDD[String] = searcher.getMatchedRDD2(rdd)

    println(result.collect.toBuffer)
  }
}
class Searcher(val query: String){
  // 判断 s 中是否包括子字符串 query
  def isMatch(s : String): Boolean ={
    s.contains(query)
  }
  // 过滤出包含 query字符串的字符串组成的新的 RDD
  def getMatchedRDD1(rdd: RDD[String]): RDD[String] ={
    rdd.filter(s => isMatch(s))  //
  }
  // 过滤出包含 query字符串的字符串组成的新的 RDD
  def getMatchedRDD2(rdd: RDD[String]): RDD[String] ={
    val q: String = query
    rdd.filter(_.contains(q))
  }
}