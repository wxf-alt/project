package spark_sql.spark_sql

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

object SparkSqlJdbcMysql {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkSqlJdbcMysql").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)

    val sqlc: SQLContext = new SQLContext(sc)

    // 这种方式已经被抛弃了
//    val df: DataFrame = sqlc.jdbc("jdbc:mysql://localhost:3306/db?user=root&password=root", "student")
//    df.show(20)

    val properties: Properties = new Properties()
    properties.setProperty("user","root")
    properties.setProperty("password","root")
    val sdf: DataFrame = sqlc.read.jdbc("jdbc:mysql://localhost:3306/db", "student1", properties)
    sdf.createOrReplaceTempView("s")
    val resultDf: DataFrame = sqlc.sql("select * from s")
    resultDf.printSchema()
    resultDf.show(20)
  }
}