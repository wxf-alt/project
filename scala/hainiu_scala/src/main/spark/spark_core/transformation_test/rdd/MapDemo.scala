package spark_core.transformation_test.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// Map 测试
object MapDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("TransformationDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[String] = sc.parallelize(List("1 2 3", "4 5 6"))
    val rdd1: Array[Array[String]] = rdd.map(s => s.split(" ")).collect
    for (elem <- rdd1) {
      println(elem.toBuffer)
    }

    val rdd2: RDD[Int] = sc.parallelize(List(1, 2, 3, 4))
    val rdd3: Array[Int] = rdd2.map(s => s + 1).collect
    val fun: Int => Unit = (x:Int) => {
      print(x + "\t")
    }
    rdd3.toBuffer.foreach(fun)


  }
}
