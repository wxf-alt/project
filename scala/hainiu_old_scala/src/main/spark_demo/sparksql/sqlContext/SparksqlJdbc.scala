package sparksql.sqlContext

import java.sql.{Connection, DriverManager, ResultSet, Statement}

import org.apache.hive.jdbc.HiveDriver

object SparksqlJdbc {
  def main(args: Array[String]): Unit = {
    // 加载driver
    Class.forName(classOf[HiveDriver].getName)
    val conn: Connection = DriverManager.getConnection("jdbc:hive2://op.hadoop:20000/panniu","","")
    val stmt: Statement = conn.createStatement()
    // 设置 SparkSQL 的 shuffle 分区数
    stmt.execute("set spark .sql.shuffle.partitions=20")
    // 重新设置sparkSQL shuffle分区数
    //    stmt.execute("set spark.sql.shuffle.partitions=20")
    val rs: ResultSet = stmt.executeQuery(
      """
        |select sum(t1.num) as sumaid from
        |(select aid, count(aid) as num from user_txt group by aid) t1
      """.stripMargin)
    while(rs.next()){
      val sum: Long = rs.getLong("sumaid")
      println(s"sum:${sum}")
    }
    conn.close()
  }
}
