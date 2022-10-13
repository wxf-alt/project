package spark_sql

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

/**
 * @Auther: wxf
 * @Date: 2022/6/9 17:26:27
 * @Description: TestDemo
 * @Version 1.0.0
 */
object TestDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]")
    // 创建sparksession 对象
    val sparkSession: SparkSession = SparkSession
      .builder()
      .config(conf)
      .appName("SparkSqlSparkSessionMysql")
      .getOrCreate()

    import sparkSession.implicits._

    // 方案1
    val props: Properties = new Properties
    props.setProperty("user", "root")
    props.setProperty("password", "root")
    // 将MySQL表转成DataFrame
    val sdf: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student1", props)

    sdf.show(20)
  }
}
