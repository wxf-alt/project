package spark_core.transformation_test.Important_operator

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

// 持久化缓存
object CachePersistDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("CachePersistDemo").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)
    val path: String = """E:\A_data\4.测试数据\spark-core数据\agent.log"""
    // 文件加载
    val rdd: RDD[String] = sc.textFile(path)
    //    val rdd2: rdd.type = rdd.cache()  // 占用内存 431.9 KB  在Job的Web页面中Storage可以看到是否有缓存
    val rdd2: rdd.type = rdd.persist(StorageLevel.MEMORY_ONLY_SER) // 占用内存 118.9 KB
    println(rdd2.collect.toBuffer)

    Thread.sleep(10000)
    rdd2.unpersist() // 取消持久化占用的内存  Web页面中Storage消失
    println("rdd2 持久化数据删除成功")

    Thread.sleep(1000000)
  }
}
