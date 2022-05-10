package spark_sql.spark_sql

import org.apache.spark.sql.{DataFrame, SparkSession}

object sparkToHive {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("toHive").master("local[*]")
      //后面是连接自己hive的地址
//      .config("hive.metastore.uris", "thrift://192.168.150.100:9083")
      .enableHiveSupport() // 连接hive的时候就加上这条
      .getOrCreate()

    val df: DataFrame = spark.sql("select * from test.demo1")
    df.show()
  }
}
