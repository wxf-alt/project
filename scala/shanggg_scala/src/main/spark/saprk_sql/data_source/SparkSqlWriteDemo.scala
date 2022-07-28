package saprk_sql.data_source

import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSqlWriteDemo {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("SparkSqlDemo1")
      .getOrCreate()

    import session.implicits._
    val df: DataFrame = session.read.text("E:\\A_data\\4.测试数据\\spark-sql数据\\city_info.txt")
    df.write.mode("append").text("./city_info")


  }
}
