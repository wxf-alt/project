package spark_core.transformation_test.rdd

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object MapPartitionsDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("MapPartitionsDemo")
    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.parallelize(Array[String]("20221114140632", "20221114121523", "20221114124710", "202211141103224"), 2)

    val result: RDD[(String, Long)] = rdd.mapPartitions(it => {
      println("++++++++++++++++++++++++++++++++++++++++++++++++++")
      lazy val date: Date = new Date()
      lazy val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss")
      it.map(x => {
        val timeStamp: Long = TestMapPartition.getEMpCurve(x, date, dateFormat)
        (x, timeStamp)
      })
    })
    result.foreach(println)


    //    import scala.collection.convert.wrapAll._
    //    val rdd: RDD[Int] = sc.parallelize(Array[Int](1, 2, 3, 4), 2)
    //    val rdd1: RDD[Int] = rdd.mapPartitions(it => {
    //      println("*********************")
    //      it.map(x => {
    //        println("=====================")
    //        x
    //      })
    //    })
    //    println(rdd1.collect.toList)

    //    val rdd1: RDD[Int] = rdd.mapPartitions(it => {
    //      val arrayList: util.ArrayList[Int] = new util.ArrayList[Int]()
    //      println(s"===================")
    //      while (it.hasNext) {
    //        val i: Int = it.next()
    //        println(i)
    //        if (i % 2 == 0) {
    //          arrayList.add(i)
    //        }
    //      }
    //      val value: util.Iterator[Int] = arrayList.iterator()
    //      value
    //    })
    //    println(rdd1.collect.toList)

  }
}
