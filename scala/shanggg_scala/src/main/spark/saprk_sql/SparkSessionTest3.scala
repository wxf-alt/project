package saprk_sql

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

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

    val mySqlDF1: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student1", properties).repartition(5)
    println("mySqlDF1 分区数：" + mySqlDF1.rdd.getNumPartitions)
    val mySqlDF2: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student2", properties).repartition(2)
    println("mySqlDF1 分区数：" + mySqlDF2.rdd.getNumPartitions)

    val value: Dataset[Row] = mySqlDF1.union(mySqlDF2)
    println("mySqlDF1 分区数：" + value.rdd.getNumPartitions)

  }
}
