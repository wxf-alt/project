package spark_core.jdbc

import java.sql.DriverManager

import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

//noinspection DuplicatedCode
// 读取 Mysql 数据
object JDBCDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("JDBCDemo")
    val sc: SparkContext = new SparkContext(conf)
    // 定义连接mysql的参数
    val driver: String = "com.mysql.jdbc.Driver"
    val url: String = "jdbc:mysql://localhost:3306/db"
    val userName: String = "root"
    val passWd: String = "root"
    val jdbcRdd: JdbcRDD[(Int, String)] = new JdbcRDD(sc, () => {
      Class.forName(driver)
      DriverManager.getConnection(url, userName, passWd)
    },
      """select s_id,s_name from student2 where s_id >= ? and s_id < ?""", 1, 20, 2,
      result => (result.getInt(1), result.getString(2))
    )
    jdbcRdd.collect.foreach(println)
  }
}
