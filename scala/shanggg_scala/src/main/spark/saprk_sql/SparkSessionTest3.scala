package saprk_sql

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @Auther: wxf
 * @Date: 2022/6/28 09:48:46
 * @Description: SparkSessionTest
 * @Version 1.0.0
 */
object SparkSessionTest3 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
    val sparkSession: SparkSession = SparkSession.builder().appName("SparkSessionTest").config(conf).master("local[*]").getOrCreate()

    import sparkSession.implicits._

    val properties: Properties = new Properties()
    properties.setProperty("user","root")
    properties.setProperty("password","root")

    val mySqlDF1: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student1", properties)

    mySqlDF1.withColumn("性别", when($"s_sex" === 1,"男").when($"s_sex" === 0,"女"))
      .show(20)

  }
}
