package sparkcore

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


// 10.二次排序
//按照单词降序，按照数值升序
// 实现内部比较器Ordered，在比较逻辑里实现二次比较的逻辑
// 因为当执行sortByKey时，按照key排序，此时需要进行shuffle，所以key需要实现序列化
object SecondarySort{
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("10- WordCount 二次排序 操作")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[String] = sc.textFile("C:\\Users\\wxf\\Desktop\\4.测试数据\\输入\\二次排序测试.txt")
    val filter: RDD[String] = rdd.filter(f => {
      if (f.contains("(") && f.contains(")")) true else false
    })
    //"(hainiu,100)"  ----> (SecondarySort(word,num), "hainiu  100")
    val map: RDD[(SecondarySort, String)] = filter.map(f => {
      val str: String = f.substring(1, f.length - 1)
      val arr: Array[String] = str.split(",")
      val word: String = arr(0)
      val num: Int = arr(1).toInt
      (new SecondarySort(word, num), s"${word}\t${num}")
    })
    // 当执行sortByKey 按照key进行排序，key是SecondarySort的对象，就会调用 该对象的比较方法进行比较
    val sortByKey: RDD[(SecondarySort, String)] = map.sortByKey()
    val arr: Array[(SecondarySort, String)] = sortByKey.collect()
    for(a <- arr){
      println(a._1)
    }
  }
}

class SecondarySort(val word:String, val num:Int) extends Ordered[SecondarySort] with Serializable {
  def compare(that: SecondarySort): Int = {
    if(this.word.compareTo(that.word) == 0){
      // 数值升序
      this.num - that.num
      // 数值降序
      that.num - this.num
    }else{
      // 单词降序
//      -this.word.compareTo(that.word)
      // 单词升序
      this.word.compareTo(that.word)
    }
  }
  override def toString: String = s"${word}\t${num}"
}
