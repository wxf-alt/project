package saprk_sql.data_source

import java.util.Properties
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

//noinspection ScalaCustomHdfsFormat,DuplicatedCode
object SparkSqlJDBCWriteDemo {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("SparkSqlDemo1")
      .getOrCreate()
    import session.implicits._

    val url: String = "jdbc:mysql://localhost:3306/db"
    val user: String = "root"
    val password: String = "root"

    val sc: SparkContext = session.sparkContext
    val rdd1: RDD[String] = sc.textFile("E:\\A_data\\4.测试数据\\spark-sql数据\\city_info.txt")

    val rdd2: RDD[(Int, String, String)] = rdd1
      .filter(f => f.split("\t").length == 3)
      .map(f => {
        val str: Array[String] = f.split("\t")
        (str(0).toInt, str(1), str(2))
      })

    val df1: DataFrame = rdd2.toDF("id", "city", "area").sort("id").coalesce(1)
    df1.printSchema()
    println("df1 分区数：" +  df1.rdd.getNumPartitions)

//    // 通用写法
//    // Table or view 'city_info' already exists. SaveMode: ErrorIfExists.;
//    // 表不存在就会自动创建; 存在就需要指定写入的 mode
//    df1.write.format("jdbc").mode(SaveMode.Overwrite)
//      .option("url", url)
//      .option("user", user)
//      .option("password", password)
//      .option("dbtable", "city_info")
//      .save()

    // 专用写法
    val properties: Properties = new Properties()
    properties.setProperty("user",user)
    properties.setProperty("password",password)
    df1.write.mode(SaveMode.Overwrite).jdbc(url,"city_info",properties)

  }
}
