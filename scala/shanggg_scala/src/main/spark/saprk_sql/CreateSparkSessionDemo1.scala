package saprk_sql

import java.util.Properties

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{ColumnName, DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.sql.functions._

object CreateSparkSessionDemo1 {
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("SparkSqlDemo1")
      .getOrCreate()

    import sparkSession.implicits._

    val properties: Properties = new Properties()
    properties.setProperty("user", "root")
    properties.setProperty("password", "root")

    val mySqlDF1: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student1", properties).repartition(5).select($"s_id",$"s_name",$"s_sex")
    val mySqlDF2: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student2", properties).repartition(2).select($"s_id",$"s_name",$"s_sex")

    val seqJoin: DataFrame = mySqlDF1.as("df1")
      .join(mySqlDF2.as("df2"), Seq("s_id"), "left_outer")
    seqJoin.printSchema()

    val equalJoin: DataFrame = mySqlDF1.as("df1")
      .join(mySqlDF2.as("df2"), $"df1.s_id" === $"df2.s_id", "left_outer")
    equalJoin.printSchema()

    println("================== inner =================")

    val seqInnerJoin: DataFrame = mySqlDF1.as("df1")
      .join(mySqlDF2.as("df2"), Seq("s_id"))
    seqInnerJoin.printSchema()

    val equalInnerJoin: DataFrame = mySqlDF1.as("df1")
      .join(mySqlDF2.as("df2"), $"df1.s_id" === $"df2.s_id")
    equalInnerJoin.printSchema()





    //    val frame: DataFrame = session.sql("select '' as t1, '' as u0000, '' as u0015, '' as u0030")
    //    val schema: StructType = frame.schema
    //    for (elem <- schema.fieldNames) {
    //      println("elem1:" + elem.toString())
    //      if (elem.toString().contains("u") && elem.toString().length == 5) {
    //        println("elem2:" + elem.toString())
    //        concat_ws("@", lit(elem.toString()))
    //      }
    //    }

  }
}
