package sparkcore

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 2.rdd 创建方式
object CreatRdd_Test {
  def main(args: Array[String]): Unit = {
    // local[*] --> 使用cpu核数当做分区数
    // 如果想对集合的数据产生的rdd分区，可以设置分区数
    // 如果不设置，默认是当前电脑CPU核数
    val conf: SparkConf = new SparkConf().setAppName("2- rdd创建").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(conf)

    //  1）textFile()：从文件系统加载数据创建RDD
//    val rdd1: RDD[String] = sc.textFile("")
    // 2）parallelize()：集合并行化，从一个已经存在的集合上创建RDD
    val rdd2: RDD[String] = sc.parallelize(List("asd", "sad", "esc", "jdi", "iru", "our"))

    println("分区数：" + rdd2.getNumPartitions)
    println("数据集个数：" + rdd2.count())
  }
}
