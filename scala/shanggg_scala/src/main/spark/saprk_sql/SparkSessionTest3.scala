package saprk_sql

import java.text.SimpleDateFormat
import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, Row, SaveMode, SparkSession}

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
    properties.setProperty("user", "root")
    properties.setProperty("password", "root")

    val mySqlDF1: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student1", properties).repartition(5)
    println("mySqlDF1 分区数：" + mySqlDF1.rdd.getNumPartitions)
    //    val mySqlDF2: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student2", properties).repartition(2)
    //    println("mySqlDF1 分区数：" + mySqlDF2.rdd.getNumPartitions)

    mySqlDF1.show()
    val result: DataFrame = mySqlDF1.withColumn("test", $"s_id".substr(3, 1))
      .withColumn("length_test", length($"test"))

    result.show()
    result.coalesce(1)
      .orderBy($"s_id".desc)
      .write
      .format("csv")
      .options(Map("header" -> "true", "sep" -> "\\t"))
      .mode(SaveMode.Overwrite)
      .save("C:\\Users\\wxf\\Desktop\\桌面下载文件夹\\SparkSessionTest3")

    //    mySqlDF1.filter($"s_id".substr(3, 1).isNotNull).show()

    //    val value: Dataset[Row] = mySqlDF1.union(mySqlDF2)
    //    println("mySqlDF1 分区数：" + value.rdd.getNumPartitions)

    //    mySqlDF1.show()
    //    val value: String = "1 = 0 or s_sex = 0 or s_name = '喻文'"
    //    println(value)
    //    //    mySqlDF1.filter(value).show()
    //    mySqlDF1.withColumn("", when($"s_name" === "喻文", 1).otherwise("")).show()

    //    mySqlDF1.show()
    //    mySqlDF1.select($"s_name", $"s_birthday".cast("string"))
    //      .mapPartitions(it => {
    //        val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM")
    //        it.map(x => {
    //          val name: String = x.getString(0)
    //          val birthdayTmp: String = x.getString(1)
    //          val birthday: String = dateFormat.format(dateFormat.parse(birthdayTmp))
    //          (name, birthday)
    //        })
    //      }).show()

  }
}
