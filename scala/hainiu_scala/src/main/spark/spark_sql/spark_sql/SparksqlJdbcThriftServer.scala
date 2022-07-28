package spark_sql.spark_sql

import java.sql.{Connection, DriverManager, ResultSet, Statement}

import org.apache.hive.jdbc.HiveDriver

object SparksqlJdbcThriftServer {
  def main(args: Array[String]): Unit = {
    // 加载 Driver
    Class.forName(classOf[HiveDriver].getName)
    // 创建连接
    val connection: Connection = DriverManager.getConnection("jdbc:hive2://op.hadoop:20000/panniu", "", "")
    val statement: Statement = connection.createStatement()
    // 重新设置sparkSQL shuffle分区数
    statement.execute("set spark.sql.shuffle.partitions=20")
    val rs: ResultSet = statement.executeQuery(
      """
        |select sum(t1.num) as sumaid from
        |(select aid, count(aid) as num from user_txt group by aid) t1
      """.stripMargin)
    while (rs.next()) {
      val sum: Long = rs.getLong("sumaid")
      println(s"sum:${sum}")
    }
    connection.close()
  }
}
