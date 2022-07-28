package spark_core.transformations

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

// 转换算子练习
object PracticeDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("PracticeDemo")
    val sc: SparkContext = new SparkContext(conf)
    val lineRdd: RDD[String] = sc.textFile("E:\\A_data\\4.测试数据\\spark-core数据\\agent.log")
    val provinceAdsOne: RDD[((String, String), Int)] = lineRdd.map(line => {
      val split: Array[String] = line.split(" ")
      println("split：" + split)
      ((split(1), split(4)), 1)
    })
    val provinceAdsCount: RDD[((String, String), Int)] = provinceAdsOne.reduceByKey(_ + _)
//    val provinceAndAdsCount: RDD[(String, (String, Int))] = provinceAdsCount.map(x => (x._1._1, (x._1._2, x._2)))
//    val proAndAdsCountGrouped: RDD[(String, Iterable[(String, Int)])] = provinceAndAdsCount.groupByKey()
//    val result: RDD[(String, List[(String, Int)])] = proAndAdsCountGrouped.map {
//      case (pro, adsCountIt) =>
//        (pro, adsCountIt.toList.sortBy(_._2)(Ordering.Int.reverse).take(3))
//    }
    provinceAdsCount.collect.foreach(println)

    sc.stop()
  }
}
