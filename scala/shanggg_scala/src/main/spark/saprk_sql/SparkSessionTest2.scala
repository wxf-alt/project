package saprk_sql

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * @Auther: wxf
 * @Date: 2022/6/28 09:48:46
 * @Description: SparkSessionTest
 * @Version 1.0.0
 */
object SparkSessionTest2 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
    val sparkSession: SparkSession = SparkSession.builder().appName("SparkSessionTest").config(conf).master("local[*]").getOrCreate()

    import sparkSession.implicits._

    val properties: Properties = new Properties()
    properties.setProperty("user","root")
    properties.setProperty("password","root")

    val mySqlDF1: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student1", properties)
      .repartition(64)

    // 写入 MySql
//    val resultTmp: DataFrame = mySqlDF1.limit(200).select($"s_id", $"s_name", $"s_sex", $"s_birthday".cast("string"), $"s_examnum")
//    resultTmp.printSchema()
//
//    val result: DataFrame = resultTmp.withColumn("s_birthday", $"s_birthday".cast(DateType))
//    result.printSchema()
//    result.show(20)
//
//    // 专用写法
//    resultTmp.write.mode(SaveMode.Append).jdbc("jdbc:mysql://localhost:3306/db","student2",properties)

    val mySqlDF2: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student2", properties)

    mySqlDF2.select($"s_id",$"s_sex",$"sc_id")//.groupBy("")
      .agg(count(when($"s_sex" === 1,$"s_id")) as "男生人数",
        count(when($"s_sex" === 0,$"s_id")) as "女人数",
        count(when($"sc_id" >= 90 ,$"s_id")) as "优秀学生人数",
        count(when($"sc_id" >= 60 and  $"sc_id" < 90,$"s_id")) as "及格学生认数",
        count(when($"sc_id" > 0 and $"sc_id" < 60,$"s_id")) as "未及格学生认数",
        count(when($"sc_id".isNull,$"s_id")) as "缺考学生认数")
      .show(50)

    /*+----+---+------+------+-------+------+
    |男生人数|女人数|优秀学生人数|及格学生认数|未及格学生认数|缺考学生认数|
    +----+---+------+------+-------+------+
    |  50| 50|    78|     0|     20|     2|
      +----+---+------+------+-------+------+*/

  }
}
