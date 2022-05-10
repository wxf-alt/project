package sparksql.sparkSession

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame, SparkSession}

//noinspection DuplicatedCode
object SparkSqlDB2 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]")
    // 创建sparksession 对象
    val sparkSession: SparkSession = SparkSession.builder().config(conf).appName("SparkSqlJdbcMysqlSparkSession").getOrCreate()
    import sparkSession.implicits._

    val props: Properties = new Properties
    props.setProperty("user", "root")
    props.setProperty("password", "root")
    // 将MySQL表转成DataFrame
    val df: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student2", props)
      .select("s_id", "s_name", "s_examnum").as("s")
      .withColumn("test", lit(""))
//    df.show(20)
//    df.printSchema()
    //    df.filter($"s_name".rlike("[0-9]")).show(1000)
    val df2: DataFrame = df.select($"s_id",
      expr("case when s_id > 40 then 1 when s_id < 41  then 0 end") as "caseWhen2",
      $"s_name"
      //      when($"s_id" > 30,"hh").otherwise("aa") as "caseWhen1",
      //      when($"s_id",10) or when($"s_id",20).otherwise("hh") as "caseWhen1",
    )
    df2.show(10000)
    df2.groupBy("caseWhen2").agg(
      when(expr("sum(case when s_id is not null then 1 else 0 end)") > 96,96).otherwise(expr("sum(case when s_id is not null then 1 else 0 end)")) as " ap_cnt",
      countDistinct($"s_id") as "s1"
    ).show(100000)

    //    df.filter($"s_name".rlike("^\\d*$")).show(20)
    //    var selctColumn: Array[Column] = Array(expr("s_id"), expr("s_name").substr(1,2),expr("s_examnum"))
    //    df.select(selctColumn:_*).show(20)

    //    df.withColumn("s_name1",$"s_name".substr(1,2)).show(20)

    //    df.select(
    //      $"s_id",
    //      $"s_name",
    //      $"s_examnum",
    //      expr("size(collect_set(s_examnum) over(partition by s_id,s_name))") as "s_examnum1"
    //      //      count(expr("distinct s_examnum")).over(Window.partitionBy("s_id", "s_name")).as("s_examnum1")
    //    ).as("comm").withColumn("xa", sum("") over())
    //      .show(20)


  }
}
