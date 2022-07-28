package spark_core.jdbc

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

//noinspection SqlDialectInspection,SqlNoDataSourceInspection
// 向 MySql 写数据
object JDBCDemo2 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(conf)
    //定义连接mysql的参数
    val driver: String = "com.mysql.jdbc.Driver"
    val url: String = "jdbc:mysql://localhost:3306/db"
    val userName: String = "root"
    val passWd: String = "root"

    val rdd: RDD[(Int, String)] = sc.parallelize(Array((110, "police"), (119, "fire")))
    // 对每个分区执行 参数函数
    rdd.foreachPartition(it => {
      Class.forName(driver)
      val conn: Connection = DriverManager.getConnection(url, userName, passWd)
      it.foreach(x => {
        val statement: PreparedStatement = conn.prepareStatement(
          """insert into student2(s_id,s_name) values(?, ?)""")
        statement.setInt(1, x._1)
        statement.setString(2, x._2)
        statement.executeUpdate()
      })
    })

  }
}
