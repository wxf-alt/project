package sparksql.sparkSession

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._


object ColumnTORow {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[*]")
    // 创建sparksession 对象
    val sparkSession: SparkSession = SparkSession.builder().config(conf).appName("SparkSqlJdbcMysqlSparkSession").getOrCreate()
    import sparkSession.implicits._

    // 一行转多行
    //    List(("aa", "1", "2" /*,35, 7, 9, 145, 6, 2*/), ("bb", "4" /*, 3, 5, 9, 645, 2, 8*/), ("aa", "3", "5"/*, 6, 7, 1, 1, 1, 4*/))
    val tuples: List[(String, String, String)] = List(("aa", "1", "2"), ("bb", "12", null), ("cc", "13", "3"))
    val frame: DataFrame = sparkSession.sparkContext.parallelize(tuples)
      .toDF("name", "p1", "p2" /*, "p3", "p4", "p5", "p6", "p7", "p8"*/)

    //    // 方式一：
    //    frame.createOrReplaceTempView("v_pivot")
    //    val sql_content =
    //      """
    //        |select `name`,
    //        |    stack(8, 'p1', `p1`, 'p2', `p2`, 'p3', `p3`, 'p4', `p4`, 'p5', `p5`, 'p6', `p6`, 'p7', `p7`, 'p8', `p8`) as (`时间`,`负荷`)
    //        |    from v_pivot
    //        |""".stripMargin
    //    val df_unpivot1 = sparkSession.sql(sql_content)
    //    df_unpivot1.show()

    frame.show(20)

    // 方式二：
    val result: DataFrame = frame.select(expr("name"),
      expr("CONCAT_WS(',',p1,p2) as p1_2")
      //      expr("CONCAT_WS(',',p3,p4) as p3_4"),
      //      expr("CONCAT_WS(',',p5,p6) as p5_6"),
      //      expr("CONCAT_WS(',',p7,p8) as p7_8"),
      //      expr("CONCAT_WS(',',p1,p2,p3,p4,p5,p6,p7,p8) as p1_8")
    )

    val result1 = result
      .withColumn("p1_2", explode(split($"p1_2", ",")))

    //      .withColumn("p1_2", explode(split(col("p1_2"), ",")))
    //      .withColumn("p3_4", explode(split(col("p3_4"), ",")))
    //      .withColumn("p5_6", explode(split(col("p5_6"), ",")))
    //      .withColumn("p7_8", explode(split(col("p7_8"), ",")))
    //      .withColumn("p1_8", explode(split(col("p1_8"), ",")))

    result1.printSchema()
    result1.show(20)

    //    val result2: DataFrame = result1
    //      .select($"name"
    //        , $"p1_2".cast("int")
    //        , $"p3_4".cast("int")
    //        , $"p5_6".cast("int")
    //        , $"p7_8".cast("int")
    //        , $"p1_8".cast("int")
    //      ).groupBy("name")
    //      .agg(
    //        max("p1_2"),
    //        max("p3_4"),
    //        max("p5_6"),
    //        max("p7_8"),
    //        max("p1_8")
    //      )
    //
    //    frame.show()
    //    result.show()
    //
    //    Thread.sleep(100000)
    //    result1.show()
    //    result2.show()

    //    val p1: DataFrame = frame.select(
    //      expr("name"),
    //      expr("p1"),
    //      expr("sum(p2) over(partition by name)").as("p2")
    //    )
    //    p1.show()


    //    val result: DataFrame = frame.select(
    //      expr("name"),
    //      expr("CONCAT_WS(',',p1,p2,p3,p4,p5,p6,p7,p8) as p1_8")
    //    )
    //      .withColumn("p1_8", explode(split(col("p1_8"), ",")))
    //      .select($"name", $"p1_8".cast("int"))
    //      .groupBy("name")
    //      .agg(
    //        max("p1_8").as("MAX_p1_8"),
    //        avg("p1_8").as("AVG_p1_8"),
    //        sum("p1_8").as("SUM_p1_8")
    //      )
  }
}
