package saprk_sql.custom_function

import org.apache.spark.sql.{DataFrame, SparkSession}

// UDF 函数 使用
object UDFDemo {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("UDFDemo")
      .getOrCreate()
    import session.implicits._
    val list: List[(String, Int)] = List(("aa", 12), ("br", 21), ("cv", 20))
    val df1: DataFrame = list.toDF("name", "age")
    // 注册一个 udf 函数
    session.udf.register("toUppper",(str:String) => str.toUpperCase)
    df1.createOrReplaceTempView("tmp_table")
    session.sql("select toUppper(name),age from tmp_table").show()
  }
}
