package spark_sql.spark_session

import java.util.Properties

import com.mysql.jdbc.Driver
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.functions._

object SparkSessionToMysql {
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

    val resultCount: DataFrame = sdf.groupBy($"s_birthday".substr(1, 4).as("birthday"))
      .agg(count($"s_name") as "count_num")

    resultCount.show()

    resultCount.write.mode(SaveMode.Append).jdbc("jdbc:mysql://localhost:3306/db", "resultCount", props)

  }
}
