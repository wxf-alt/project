package sparkcore

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.mutable

// 9.WordCount 的 Sort Shuffle 操作
object WordCount_Sort_Partition {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("9- WordCount 的 Sort Shuffle 操作").setMaster("local[10]")
    val sc: SparkContext = new SparkContext(conf)

    // 1.测试一
    //    val rdd: RDD[String] = sc.textFile("C:\\Users\\wxf\\Desktop\\4.测试数据\\输入")
    //    val mapRdd: RDD[(String, Int)] = rdd.flatMap(_.split(" ")).map((_,1))
    //    // hash partition
    //    val reduceByKeyRdd: RDD[(String, Int)] = mapRdd.reduceByKey(_ + _)
    //    // range partition
    //    // 按照值降序排序
    //    val sortByRdd: RDD[(String, Int)] = reduceByKeyRdd.sortBy(_._2, false)
    //
    //    val arr: Array[(String, Int)] = sortByRdd.collect()
    //    println(arr.toBuffer)
    //    println(sortByRdd.toDebugString)

    // 1.测试二
    val rdd: RDD[(Int, Int)] = sc.parallelize(List((1, 3), (9, 2), (7, 4), (3, 3), (5, 5), (3, 4)), 2)
    // 按照rangepartition重新分区，分区数是2
    //    val rdd2: RDD[(Int, Int)] = rdd.partitionBy(new RangePartitioner(2,rdd))
    // 调用自定义的 partition
    val rdd2: RDD[(Int, Int)] = rdd.partitionBy(new MyPartition)
    val rdd3: RDD[String] = rdd2.mapPartitionsWithIndex((index, it) => {
      it.map(f => {
        s"index:${index}, f:${f}"
      })
    })
    //    val arr: Array[String] = rdd3.collect()
    //    println(arr.toBuffer)
    val buffer: mutable.Buffer[String] = rdd3.collect().toBuffer
    for (elem <- buffer) {
      println(elem)
    }

  }
}

// 自定义 partition
class MyPartition extends Partitioner {
  override def numPartitions: Int = 2

  override def getPartition(key: Any): Int = {
    val key_new: Int = key.asInstanceOf[Int]
    if (key_new <= 5) {
      0
    } else {
      1
    }
  }
}
