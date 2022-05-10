package spark_core.transformation_test.pairRdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CoGroupDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("CoGroupDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd1: RDD[(String, String)] =sc.parallelize(List(("id01", "aa"),("id02","bb"),("id03","cc"),("id01", "dd")))
    val rdd2: RDD[(String, Int)] =sc.parallelize(List(("id01", 10),("id03",13),("id04",14),("id03",20)))
    val coGroup: Array[(String, (Iterable[String], Iterable[Int]))] = rdd1.cogroup(rdd2).collect
    println(coGroup.toList)  // List((id01,(CompactBuffer(aa, dd),CompactBuffer(10))), (id02,(CompactBuffer(bb),CompactBuffer())), (id03,(CompactBuffer(cc),CompactBuffer(13, 20))), (id04,(CompactBuffer(),CompactBuffer(14))))


  }
}
