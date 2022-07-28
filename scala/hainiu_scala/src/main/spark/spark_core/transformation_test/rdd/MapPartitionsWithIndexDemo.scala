package spark_core.transformation_test.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object MapPartitionsWithIndexDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("MapPartitionsWithIndexDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[Int] = sc.parallelize(Array[Int](1,2,3,4),2)
    val rdd1: RDD[Int] = rdd.mapPartitionsWithIndex((index, it) => {
      // index: 代表分区号
      // it: 代表分区对应的数据集
      print(s"index:${index}\t")  // index:1	index:0
      it.map(f => {
        print(s"index:${index}, f:${f}\t")  // index:0, f:1	index:0, f:2	index:1, f:3	index:1, f:4
        f + 10
      })
    })
    println(rdd1.collect.toList)  // List(11, 12, 13, 14)

  }
}
