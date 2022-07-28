package saprk_sql.custom_function

import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

import scala.collection.immutable.Nil

object UDAFDemo {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("UDAFDemo")
      .getOrCreate()

    val df: DataFrame = session.read.json("")
    // 注册聚合函数
    session.udf.register("mySum",new MySum())
    df.createOrReplaceTempView("user")
    session.sql("select mySum(age) from user").show()

    session.close()
  }
}

class MySum extends UserDefinedAggregateFunction{

  // 用来定义输入的数据类型
  override def inputSchema: StructType = StructType(StructField("ele",DoubleType)::Nil)

  // 缓冲区的类型
  override def bufferSchema: StructType = StructType(StructField("sum",DoubleType)::Nil)

  // 最终聚合结果的数据类型
  override def dataType: DataType = DoubleType

  // 相同的输入是否返回相同的输出
  override def deterministic: Boolean = true

  // 对缓冲区初始化
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    // 在缓冲集合中初始化和
    buffer.update(0,0D)  // 等价于 buffer(0) = 0D
  }

  // 分区内聚合
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    // input 是只 在使用聚合函数的时候,传过来的参数封装成的 Row
    if (!input.isNullAt(0)) { // 判断传进来的值是否为 null
      val v: Double = input.getAs[Double](0)
      buffer(0) = buffer.getDouble(0) + v
    }
  }

  // 分区间聚合
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      val v: Double = buffer2.getAs[Double](0)
      buffer1(0) = buffer1.getDouble(0) + v
  }

  // 最终的输出值
  override def evaluate(buffer: Row): Any = buffer.getDouble(0)

}
