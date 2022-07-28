package sparkcore

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 1.scala版的spark的wordCount
object Word_Count {
  def main(args: Array[String]): Unit = {
    // 1.获取 SparkConf，并可以添加配置
    val conf: SparkConf = new SparkConf().setAppName("1- wordcount").setMaster("local[*]")
    // 2.利用SparkConf 获取 sparkcore 的操作对象
    val sc: SparkContext = SparkContext.getOrCreate(conf)
    // 3.读取文件 生成 rdd
    val file_Rdd: RDD[String] = sc.textFile("C:\\Users\\wxf\\Desktop\\4.测试数据\\输入")

    // 4.使用算子 进行逻辑操作
    val word_count_Rdd: RDD[(String, Int)] = file_Rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).sortBy(_._2, false, 1)

    // 将 key 和 value 拼接到一起 作为key进行输出 (变相指定 key与value的分隔符)
    val mapRdd: RDD[String] = word_count_Rdd.map(f => (f._1 + " " + f._2))

//    val out_path: Path = new Path("C:\\Users\\wxf\\Desktop\\4.测试数据\\输出")
//    val hadoop_conf: Configuration = new Configuration()
//    val fs: FileSystem = FileSystem.get(hadoop_conf)
//    if (fs.exists(out_path)) {
//      fs.delete(out_path, true)
//      println("已删除输出目录")
//    }

    val out_path: String = "C:\\Users\\wxf\\Desktop\\4.测试数据\\输出"
    // 引入隐式转换
    import utils.MyPredef.string2HdfsDelete
    // 删除目录
    out_path.deletePath
    // 输出到文件
    mapRdd.saveAsTextFile(out_path)

    // 在控制台显示
    val result = mapRdd.collect()
    println(result.toBuffer)

  }
}

case class Word_Count()