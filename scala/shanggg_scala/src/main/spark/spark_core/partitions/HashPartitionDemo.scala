package spark_core.partitions

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

// HashPartition 案例
object HashPartitionDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("HashPartiitonDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd1: RDD[(Int, String)] = sc.parallelize(Array((10, "a"), (21, "b"), (32, "c"), (43, "d"), (54, "e"), (60, "f")))

    val rdd2: RDD[(Int, String)] = rdd1.mapPartitionsWithIndex((index, it) => {
      it.map(x => (index, x._1 + " : " + x._2))
    })
    println(rdd2.collect.mkString(","))  // (0,10 : a),(0,21 : b),(0,32 : c),(1,43 : d),(1,54 : e),(1,60 : f)

    // 使用 HashPartitioner重新分区
    val rdd3: RDD[(Int, String)] = rdd1.partitionBy(new HashPartitioner(5))
    val rdd4: RDD[(Int, String)] = rdd3.mapPartitionsWithIndex((index, it) => {
      it.map(x => (index, x._1 + " : " + x._2))
    })
    println(rdd4.collect.mkString(","))  // (0,10 : a),(0,60 : f),(1,21 : b),(2,32 : c),(3,43 : d),(4,54 : e)
  }
}
