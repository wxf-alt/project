package spark_core.partitions

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object MyPartitionerDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("MyPartitionerDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd1: RDD[(Int, String)] = sc.parallelize(Array((10, "a"), (32, "c"), (54, "e"), (43, "d"), (21, "b"), (60, "f")))

    val rdd2: RDD[(Int, String)] = rdd1.partitionBy(new MyPartition(5))
    val rdd3: RDD[(Int, String)] = rdd2.mapPartitionsWithIndex((index, items) => items.map(x => (index, x._1 + " : " + x._2)))
    println(rdd3.collect.mkString(" "))  // (0,10 : a) (1,32 : c) (1,21 : b) (2,43 : d) (3,54 : e) (3,60 : f)
  }
}

class MyPartition(numPars:Int) extends Partitioner{

  override def numPartitions: Int = numPars

  override def getPartition(key: Any): Int = {
    var parNum: Int = 0
    key match {
      case k: Int =>
        if(k < 20){
          parNum = numPars - numPars
        }else if (k < 40){
          parNum = numPars - numPars + 1
        }else if (k <= 50){
          parNum = numPars - numPars + 2
        }else if (k > 50){
          parNum = numPars - numPars + 3
        }
      case _ =>
        throw new Exception("含有非整数数据类型,无法分区")
    }
    parNum
  }
}
