package sparksql

import java.text.SimpleDateFormat
import java.util.{Date, Properties}

import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DateType
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

//noinspection DuplicatedCode
object SparkMySql {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]")
    // 创建sparksession 对象
    val sparkSession: SparkSession = SparkSession.builder().config(conf).appName("SparkSqlJdbcMysqlSparkSession").getOrCreate()
    import sparkSession.implicits._

    val date: Date = new Date()
    val format: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val statDate: String = format.format(date)

    val props: Properties = new Properties
    props.setProperty("user", "root")
    props.setProperty("password", "root")
    // 将MySQL表转成DataFrame
    val df: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student2", props)
      .select("s_id", "s_name", "s_examnum", "s_sex")

    val dataFrame: DataFrame = df.select($"s_id", $"s_name", $"s_examnum",
      when($"s_sex" === 1, "男")
        .when($"s_sex" === 0, "女").as("sex") as "s_sex")
      .withColumn("s_birthday", lit(statDate).cast(DateType))

    dataFrame.printSchema()
    dataFrame.show(20)

    dataFrame.write.mode(SaveMode.Overwrite)
      .jdbc("jdbc:mysql://localhost:3306/db", "student3", props)

  }
}
