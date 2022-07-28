package saprk_sql.data_source

import org.apache.spark.sql.SparkSession

//noinspection DuplicatedCode
object SparkSqlHiveReadDemo {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder()
      .master("local[*]")
      // 添加 hive 支持
      .enableHiveSupport()
      .appName("SparkSqlDemo1")
      .getOrCreate()
    import session.implicits._

    session.sql("show databases")
    session.sql("use test")
    session.sql("select count(*) from test.table").show()

    session.stop()
  }
}
