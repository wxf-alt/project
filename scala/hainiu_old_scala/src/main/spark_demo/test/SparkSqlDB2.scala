package test

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame, Dataset, SparkSession}

//noinspection DuplicatedCode
object SparkSqlDB2 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]")
    // 创建sparksession 对象
    val sparkSession: SparkSession = SparkSession.builder().config(conf).appName("SparkSqlJdbcMysqlSparkSession").getOrCreate()
    import sparkSession.implicits._

    val props: Properties = new Properties
    props.setProperty("user", "root")
    props.setProperty("password", "root")
    // 将MySQL表转成DataFrame
    val df: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student2", props)

    val d2: Dataset[String] = df.map(f => {
      f(1).toString
    })
    d2.show(100000000)

  }
}
