package sparksql.sparkSession

import java.util.Properties
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}

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
    val df: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student1", props).select("s_id", "s_name", "s_examnum").as("s")

    df.select(
      $"s_id",
      $"s_name",
      $"s_examnum",
      expr("size(collect_set(s_examnum) over(partition by s_id,s_name))") as "s_examnum1"
      //      count(expr("distinct s_examnum")).over(Window.partitionBy("s_id", "s_name")).as("s_examnum1")
    ).as("comm").withColumn("xa",sum("") over())

      .show(20)
  }
}
