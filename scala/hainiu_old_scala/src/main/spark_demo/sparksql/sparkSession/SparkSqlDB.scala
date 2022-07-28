package sparksql.sparkSession

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSqlDB {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[*]")
    // 创建sparksession 对象
    val sparkSession: SparkSession = SparkSession.builder().config(conf).appName("SparkSqlJdbcMysqlSparkSession").getOrCreate()
    import sparkSession.implicits._

    val props: Properties = new Properties
    props.setProperty("user", "root")
    props.setProperty("password", "root")
    // 将MySQL表转成DataFrame
    val df: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student1", props).select("s_id", "s_name", "s_examnum").as("s")
    val df2: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student1", props).select("s_id").as("s1")

    val result: DataFrame = df.join(df2, $"s.s_id" === $"s1.s_id", "left_outer")
      .withColumn("s_examnum",$"s_examnum")
      .withColumn("s1_sid",$"s1.s_id")
      .select(
        "s.s_id",
        "s_name",
        "s_examnum",
        "s1_sid"
      )

    result.show(20)

  }
}
