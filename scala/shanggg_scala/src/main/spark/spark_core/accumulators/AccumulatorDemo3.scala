package spark_core.accumulators

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.util.AccumulatorV2

// 自定义 Map 类型累加器
object AccumulatorDemo3 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("AccumulatorDemo3").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(conf)
    val list: List[Int] = List(30, 50, 60, 70, 10, 20)
    val rdd: RDD[Int] = sc.parallelize(list)
    val acc: MapAcc = MapAcc()
    sc.register(acc)
//    val numPartitions: Int = rdd.getNumPartitions
//    val value: RDD[Map[String, AnyVal]] = rdd.map(r => {
//      acc.add(r)
//      acc.value
//    })
//    println(numPartitions)
//    println(value.collect().toBuffer)
    rdd.foreach(x => acc.add(x))
    println(acc.value)
  }
}

case class MapAcc() extends AccumulatorV2[Double,Map[String,AnyVal]]{
  private var map: Map[String, AnyVal] = Map[String, AnyVal]()
  override def isZero: Boolean = map.isEmpty

  override def copy(): AccumulatorV2[Double, Map[String, AnyVal]] = {
    val acc: MapAcc = MapAcc()
    acc.map = this.map
    acc
  }

  // 不可变集合 直接赋一个空的集合
  override def reset(): Unit = map = Map[String, AnyVal]()

  override def add(v: Double): Unit = {
    map += "sum" -> ((map.getOrElse("sum",0D)).asInstanceOf[Double] + v)
    map += "count" -> ((map.getOrElse("count",0L)).asInstanceOf[Long] + 1)

  }

  override def merge(other: AccumulatorV2[Double, Map[String, AnyVal]]): Unit = {
    other match {
      case a:MapAcc =>
        map += "sum" -> ((map.getOrElse("sum",0D)).asInstanceOf[Double] + (a.map.getOrElse("sum",0D)).asInstanceOf[Double])
        map += "count" -> ((map.getOrElse("count",0L)).asInstanceOf[Long] + (a.map.getOrElse("count",0L)).asInstanceOf[Long])
      case _ => throw new UnsupportedOperationException
    }
  }

  override def value: Map[String, AnyVal] = {
    val sum: Double = map.getOrElse("sum", 0D).asInstanceOf[Double]
    val count: Long = map.getOrElse("count", 0L).asInstanceOf[Long]
    map += "avg" -> (sum / count)
    map
  }
}
