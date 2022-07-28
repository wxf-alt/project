package saprk_sql

import java.util.Properties

import org.apache.spark.sql.SparkSession

object CreateSparkSessionDemo1 {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("SparkSqlDemo1")
      .getOrCreate()


  }
}
