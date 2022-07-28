package spark_core.accumulators

import java.util
import java.util.Collections

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

//noinspection DuplicatedCode
// 自定义累加器
object AccumulatorDemo2 {
  def main(args: Array[String]): Unit = {
    val pattern: String = """^\d+$"""
    val conf: SparkConf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(conf)
    // 统计出来非纯数字, 并计算纯数字元素的和
    val rdd1: RDD[String] = sc.parallelize(Array("abc", "a30b", "aaabb2", "60", "20"))

    val acc: MyAcc = MyAcc()
    // 注册
    sc.register(acc)
    val rdd2: RDD[Int] = rdd1.filter(x => {
      val flag: Boolean = x.matches(pattern)
      if (!flag) acc.add(x)
      flag
    }).map(_.toInt)
    println(rdd2.reduce(_ + _)) // 80
    println(acc.value)  // [abc, a30b, aaabb2]
  }
}

case class MyAcc() extends AccumulatorV2[String, java.util.List[String]] {
  private val _list: java.util.List[String] = Collections.synchronizedList(new util.ArrayList[String]())

  // 判零 对缓冲区的值进行判零
  override def isZero: Boolean = _list.isEmpty

  // 复制累加器
  override def copy(): AccumulatorV2[String, util.List[String]] = {
    val newAcc: MyAcc = new MyAcc
    _list.synchronized {
      newAcc._list.addAll(_list)
    }
    newAcc
  }

  // 重置累加器
  override def reset(): Unit = _list.clear()

  // 分区内累加器 累加
  override def add(v: String): Unit = _list.add(v)

  // 分区间合并累加器的值
  // 将 other 的值合并到当前 属性中
  override def merge(other: AccumulatorV2[String, util.List[String]]): Unit =
    other match {
      case o: MyAcc => _list.addAll(o.value)
      case _ => throw new UnsupportedOperationException(
        s"Cannot merge ${this.getClass.getName} with ${other.getClass.getName}")
    }

  // 获取累加器的值
  override def value: util.List[String] =
    java.util.Collections.unmodifiableList(new util.ArrayList[String](_list))
}
