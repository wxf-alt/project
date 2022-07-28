package spark_core.transformation_test.Important_operator

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// checkpoint 测试
object CheckpointRDD {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("CreateRDD").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)
    sc.setCheckpointDir("""E:\A_data\4.测试数据\检查点\CheckpointRDD""")
    val path: String = """E:\A_data\4.测试数据\输入\wordcount\a.txt"""
    // 文件加载
    val rdd: RDD[String] = sc.textFile(path)
    val mapRdd: RDD[(String, Int)] = rdd.map(x => {
      val str: Array[String] = x.split("\t")
      println(s"====> ${str.toBuffer} : ${str.length}")
      (str(0), 1)
    })
    val result: RDD[(String, Int)] = mapRdd.reduceByKey(_ + _)
    // 因为 reduceBykey 自动做了持久化，所以上面map里面的输出不会执行
    result.checkpoint()

    println(result.count())
    Thread.sleep(100000)
  }
}
