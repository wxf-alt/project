package sparksql.sparkSession

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

// 3.spark-sql的UDF
object SparkSqlUDF {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]")
    conf.set("spark.sql.shuffle.partitions","1")
    // 创建sparksession 对象
    val sparkSession: SparkSession = SparkSession.builder().config(conf).appName("SparkSqlUDF").getOrCreate()
    import sparkSession.implicits._
    var props:Properties = new Properties
    props.setProperty("user","root")
    props.setProperty("password","root")
    // 将MySQL表转成DataFrame
    val df: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db","student1",props)


    // 方案一
//    df.createOrReplaceTempView("student")
//    // 注册udf函数
//    //                         函数名      你要执行的函数逻辑
//    sparkSession.udf.register("len",(str:String) => str.length)
    // 使用len 函数
//    val resDF: DataFrame = sparkSession.sql("select s_id,len(s_id) as sidlen from student")


    // 方案二
    // 注册自定义函数（通过匿名函数）
    val strLen = udf((str: String) => str.length())
    //注册自定义函数（通过实名函数）
    val udf_isAdult = udf(isAdult _)

    val resDF: DataFrame = df.select("s_id")
      .withColumn("sidlen",strLen($"s_id"))

    resDF.printSchema()
    resDF.show()
  }

  def isAdult(age: Int) = {
    if (age < 18) {
      false
    } else {
      true
    }
  }
}