package saprk_sql.custom_function

import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

import scala.collection.immutable.Nil

//noinspection DuplicatedCode
object UDAFDemo2 {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("UDAFDemo2")
      .getOrCreate()

    val df: DataFrame = session.read.json("")
    // 注册聚合函数
    session.udf.register("mvAvg",new MyAvg())
    df.createOrReplaceTempView("user")
      session.sql("select mvAvg(age) from user").show()

    session.close()
  }
}

class MyAvg extends UserDefinedAggregateFunction{

  // 用来定义输入的数据类型
  override def inputSchema: StructType = StructType(StructField("ele",DoubleType)::Nil)

  // 缓冲区的类型
  override def bufferSchema: StructType =
    StructType(StructField("sum",DoubleType)::StructField("count",LongType)::Nil)

  // 最终聚合结果的数据类型
  override def dataType: DataType = DoubleType

  // 相同的输入是否返回相同的输出
  override def deterministic: Boolean = true

  // 对缓冲区初始化
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    // 在缓冲集合中初始化和
    buffer(0) = 0D
    buffer(1) = 0L
  }

  // 分区内聚合
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    // input 是只 在使用聚合函数的时候,传过来的参数封装成的 Row
    if (!input.isNullAt(0)) { // 判断传进来的值是否为 null
      val v: Double = input.getAs[Double](0)
      buffer(0) = buffer.getDouble(0) + v
      buffer(1) = buffer.getLong(1) + 1
    }
  }

  // 分区间聚合
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getDouble(0) + buffer2.getDouble(0)
    buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
  }

  // 最终的输出值
  override def evaluate(buffer: Row): Any = buffer.getDouble(0) / buffer.getLong(1)
}
