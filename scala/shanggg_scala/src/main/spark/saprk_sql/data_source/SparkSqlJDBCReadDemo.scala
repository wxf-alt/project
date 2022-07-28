package saprk_sql.data_source

import java.util.Properties

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, DataFrameReader, SparkSession}

//noinspection ScalaCustomHdfsFormat
object SparkSqlJDBCReadDemo {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("SparkSqlDemo1")
      .getOrCreate()
    import session.implicits._

    val url: String = "jdbc:mysql://localhost:3306/db"
    val user: String = "root"
    val password: String = "root"

//    // 通用写法
//    val df: DataFrame = session.read
//      .option("url", url)
//      .option("user", user)
//      .option("password", password)
//      .option("dbtable", "student1")
//      .format("jdbc")
//      .load()


    // 专用写法
    val properties: Properties = new Properties()
    properties.setProperty("user",user)
    properties.setProperty("password",password)
    val df: DataFrame = session.read.jdbc(url, "student1", properties)
      .withColumn("rn",row_number().over(Window.orderBy("s_name")))

    df.show()
  }
}
