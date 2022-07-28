package sparksql.sqlContext

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions._

// 3.使用spark-sql的JDBC访问MYSQL
object SparkSqlJdbcMysql {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkSqlJdbcMysql").setMaster("local[*]")
    val sc = new SparkContext(conf)

    // 创建SQLContext
    val sqlc = new SQLContext(sc)

    // 这种方式已经被抛弃了
    //sqlc.jdbc("jdbc:mysql://localhost:3306/hainiu_test?user=root&password=111111","student")

    var props:Properties = new Properties
    props.setProperty("user","root")
    props.setProperty("password","root")
    // 将MySQL表转成DataFrame
    val sdf: DataFrame = sqlc.read.jdbc("jdbc:mysql://localhost:3306/db","student1",props)
    val scdf: DataFrame = sqlc.read.jdbc("jdbc:mysql://localhost:3306/db","student2",props)

//    sdf.createOrReplaceTempView("s")
//    scdf.createOrReplaceTempView("sc")
//    // 通过DataFrame实现了两个表的join
//    val joinDF: DataFrame = sqlc.sql("select * from s inner join sc on s.S_ID=sc.S_ID order by s.s_id")
//    joinDF.printSchema()
//    joinDF.show(40)

    sdf.show(10)
    scdf.show(10)

  }
}