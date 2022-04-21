package spark_core.accumulators

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

// 使用累加器
//  需求:计算文件中空行的数量
object AccumulatorDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("AccumulatorDemo").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[String] = sc.textFile("E:\\A_data\\4.测试数据\\输入\\wordcount\\*.txt")
    // 得到一个 Long 类型的累加器.  将从 0 开始累加
    val accumulator: LongAccumulator = sc.longAccumulator
    rdd.foreach(s => if(s.trim.length == 0) accumulator.add(1))
    println(accumulator) // LongAccumulator(id: 0, name: None, value: 4)
    println(accumulator.value) // 4
  }
}
