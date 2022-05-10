package spark_sql.spark_session

import java.util.Properties

import com.mysql.jdbc.Driver
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSqlSparkSessionMysql {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]")
    // 创建sparksession 对象
    val sparkSession: SparkSession = SparkSession
      .builder()
      .config(conf)
      .appName("SparkSqlSparkSessionMysql")
      .getOrCreate()

    // 方案1
    val props: Properties = new Properties
    props.setProperty("user", "root")
    props.setProperty("password", "root")
    // 将MySQL表转成DataFrame
    val sdf: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student1", props)

    //方案2
    val scdf: DataFrame = sparkSession.read.format("jdbc")
      .option("driver", classOf[Driver].getName)
      .option("url", "jdbc:mysql://localhost:3306/db")
      .option("dbtable", "school")
      .option("user", "root")
      .option("password", "root")
      .load()
//    sdf.show(10)
//    scdf.show(10)

    sdf.createOrReplaceTempView("s")
    scdf.createOrReplaceTempView("sc")
    // 通过DataFrame实现了两个表的join
    val joinDF: DataFrame = sparkSession.sql("select * from s inner join sc on s.sc_id=sc.sc_id")
    joinDF.printSchema()
    joinDF.show()
  }
}
