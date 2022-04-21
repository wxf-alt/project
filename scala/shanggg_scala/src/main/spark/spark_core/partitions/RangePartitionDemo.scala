package spark_core.partitions

import org.apache.spark.rdd.RDD
import org.apache.spark.{RangePartitioner, SparkConf, SparkContext}

// RangePartition 案例
object RangePartitionDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("RangePartitionDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd1: RDD[(Int, String)] = sc.parallelize(Array((10, "a"), (32, "c"), (54, "e"), (43, "d"), (21, "b"), (60, "f")))

    val rdd2: RDD[(Int, String)] = rdd1.partitionBy(new RangePartitioner(5, rdd1))
    val rdd3: RDD[(Int, String)] = rdd2.mapPartitionsWithIndex((index, it) => {
      it.map(x => (index, x._1 + " : " + x._2))
    })
    println(rdd3.collect.mkString(","))  // (0,10 : a),(0,21 : b),(1,32 : c),(2,43 : d),(3,54 : e),(4,60 : f)
  }
}
