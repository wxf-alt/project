package spark_sql

import java.util

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.sql.functions._

/**
 * @Auther: wxf
 * @Date: 2022/7/20 12:40:31
 * @Description: RowToColumn
 * @Version 1.0.0
 */
object RowToColumn {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder.appName("TestAPP")
      .master("local[*]")
      //      .enableHiveSupport()
      .getOrCreate()

    import spark.implicits._

    val list: List[Row] = List(Row("math", "alice", 88), Row("chinese", "alice", 92), Row("english", "alice", 77),
      Row("math", "bob", 65), Row("chinese", "bob", 87), Row("english", "bob", 90),
      Row("math", "cary", 67), Row("chinese", "cary", 33), Row("english", "cary", 24),
      Row("math", "josh", 77), Row("chinese", "josh", 87), Row("english", "josh", 90))

    val structFields: util.ArrayList[StructField] = new util.ArrayList[StructField]()
    structFields.add(StructField("CONS_NO", StringType))
    structFields.add(StructField("DATA_TIME", StringType))
    structFields.add(StructField("P", IntegerType))
    val structType: StructType = StructType(structFields)

    val value: RDD[Row] = spark.sparkContext.parallelize(list)
    val df: Dataset[Row] = spark.createDataFrame(value, structType)

    df.orderBy("CONS_NO").show(20)

    val df1: DataFrame = df.select($"CONS_NO",
      when($"DATA_TIME" === "bob", $"P") as "P1",
      when($"DATA_TIME" === "cary", $"P") as "P2",
      when($"DATA_TIME" === "alice", $"P") as "P3",
      when($"DATA_TIME" === "josh", $"P") as "P4")
      .groupBy("CONS_NO")
      .agg(sum($"P1") as "P1",
        sum($"P2") as "P2",
        sum($"P3") as "P3",
        sum($"P4") as "P4")

    df1.show(20)


  }

}
