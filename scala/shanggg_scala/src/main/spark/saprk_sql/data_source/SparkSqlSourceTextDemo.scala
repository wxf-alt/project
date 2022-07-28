package saprk_sql.data_source

import org.apache.spark.sql.{DataFrame, SparkSession}

// 修改默认数据源
object SparkSqlSourceTextDemo {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder()
      .master("local[*]")
      // 默认数据源是 parquet, 可以在配置中修改
      .config("spark.sql.sources.default","text")
      .appName("SparkSqlDemo1")
      .getOrCreate()

    val df1: DataFrame = session.read.load("E:\\A_data\\4.测试数据\\spark-sql数据\\city_info.txt")
    df1.write.parquet("")
    df1.show()


    session.stop()
  }
}
