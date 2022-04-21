package saprk_sql.custom_function

import org.apache.spark.sql.catalyst.plans.logical.Aggregate
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, Encoders, SparkSession, TypedColumn}

case class Dog(name: String, age: Int)

case class AgeAvg(sum: Int, count: Int) {
  val avg: Double = sum.toDouble / count
}

object UDAFDemo3 {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("UDAFDemo3")
      .getOrCreate()
    import session.implicits._
    val ds: Dataset[Dog] = List(Dog("大黄", 6), Dog("小黄", 2), Dog("小白", 5)).toDS()
    // 强类型的聚合函数使用方式
    val avg: TypedColumn[Dog, Double] = new MyAvg2().toColumn.name("avg")
    val result: Dataset[Double] = ds.select(avg)
    result.show()

    session.close()
  }
}

class MyAvg2 extends Aggregator[Dog, AgeAvg, Double] {

  // 零值 对缓冲区初始化
  override def zero: AgeAvg = AgeAvg(0, 0)

  // 聚合 (分区内聚合)
  override def reduce(b: AgeAvg, a: Dog): AgeAvg = {
    a match {
      case Dog(name, age) => AgeAvg(b.sum + age, b.count + 1)
      case _ => b
    }
  }

  // 聚合 (分区间聚合)
  override def merge(b1: AgeAvg, b2: AgeAvg): AgeAvg = {
    AgeAvg(b1.sum + b2.sum, b1.count + b2.count)
  }

  // 返回最终的值
  override def finish(reduction: AgeAvg): Double = reduction.avg

  // 对缓冲区进行编码
  // 如果是样例类 就返回这个编码器就行了
  override def bufferEncoder: Encoder[AgeAvg] = Encoders.product

  // 对返回值进行编码
  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}