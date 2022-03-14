package sparkcore

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

// 7.persist  和 unpersist 缓存 持久化
object Cache_Persist_Unpersist_Tets {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("7- persist - cache 操作").setMaster("local[10]")
    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.textFile("E:\\A_data\\3.code\\scala\\hainiu\\words1",30)
    val mapRdd: RDD[(String, Int)] = rdd
      .flatMap(_.split("\t"))
      .map((_, 1))
//    val persistRdd= mapRdd.persist(StorageLevel.MEMORY_ONLY)
    val persistRdd= mapRdd.cache()
    val result: RDD[(String, Int)] = persistRdd
      .reduceByKey(_ + _)
    result.foreach(println)
    println(result.getNumPartitions)

  }
}
