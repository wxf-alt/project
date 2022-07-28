package spark_sql.spark_session

import java.util.Properties
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object SparkSqlUDF {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]")
      .set("spark.sql.shuffle.partitions", "1")
    // 创建sparksession 对象
    val sparkSession: SparkSession = SparkSession.builder()
      .config(conf)
      .appName("SparkSqlUDF")
      .getOrCreate()

    // 方案1
    val props: Properties = new Properties
    props.setProperty("user", "root")
    props.setProperty("password", "root")
    // 将MySQL表转成DataFrame
    //    val df: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db","student1",props)
    val df: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student2", props)

    df.createOrReplaceTempView("student")
    // 注册udf函数
    //          函数名      你要执行的函数逻辑
    sparkSession.udf.register("len", (str: String) => str.length)
    // 使用len 函数
    val resDF: DataFrame = sparkSession.sql("select s_id,len(s_id) as sidlen from student")
    resDF.printSchema()
    resDF.show()
  }
}