package spark_core.word_count

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 二次排序测试
// 筛选带有括号的数据，按照单词降序，按照数值升序
// 按照单词降序，按照数值升序
object WordCountSecondarySort {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCountSecondarySort")
    val sc: SparkContext = new SparkContext(conf)

    val path: String = """E:\A_data\4.测试数据\输入\二次排序测试.txt"""
    val inputRdd: RDD[String] = sc.textFile(path)
    val filterRdd: RDD[String] = inputRdd.filter(f => {
      if (f.contains("(") && f.contains(")")) true else false
    })
    val mapRdd: RDD[(SecondarySortBean, String)] = filterRdd.map(x => {
      val str: String = x.substring(1, x.length - 1)
      val arr: Array[String] = str.split(",")
      val word: String = arr(0)
      val num: Int = arr(1).toInt
      (new SecondarySortBean(word, num), s"${word}\t${num}")
    })
    val sortByKeyRdd: RDD[(SecondarySortBean, String)] = mapRdd.sortByKey()
    val array: Array[(SecondarySortBean, String)] = sortByKeyRdd.collect()
    for (elem <- array) {
      println(elem._1)
    }
  }
}

case class SecondarySortBean(word: String, num: Int) extends Ordered[SecondarySortBean]{
  override def compare(that: SecondarySortBean): Int = {
    if(this.word.compareTo(that.word) == 0){
      // 数值升序
      this.num - that.num
    }else{
      // 单词降序
      that.word.compareTo(this.word)
    }
  }
  override def toString: String = s"${word}\t${num}"
}
