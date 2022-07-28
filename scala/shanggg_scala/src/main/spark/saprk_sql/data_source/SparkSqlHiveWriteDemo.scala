package saprk_sql.data_source

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

// 写 hive
object SparkSqlHiveWriteDemo {
  def main(args: Array[String]): Unit = {
     // 使用 这个用户名 进行写数据
    System.setProperty("HADOOP_USER_NAME","hadoop")

    val session: SparkSession = SparkSession.builder()
      .master("local[*]")
      // 添加 hive 支持
      .enableHiveSupport()
      // 添加hive元数据所在的位置
      .config("spark.sql.warehouse.dir","hdfs://nn1.hadoop/user/hive/warehouse")
      .appName("SparkSqlDemo1")
      .getOrCreate()
    import session.implicits._

//    // 方式一 insert 语句
//    session.sql("create database idea_sparkOnHive")
//    session.sql("use idea_sparkOnHive")
//    session.sql("create table table(id int,name string)")
//    session.sql("insert into table values(10,'lisi')").show()

    val df1: DataFrame = session.read.load("E:\\A_data\\4.测试数据\\spark-sql数据\\city_info.txt")
    df1.write.mode(SaveMode.Overwrite).saveAsTable("table")
    df1.write.insertInto("table")



  }
}
