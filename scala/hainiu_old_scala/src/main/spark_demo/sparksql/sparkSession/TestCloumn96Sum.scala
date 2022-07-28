package sparksql.sparkSession

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.{UserDefinedFunction, Window}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object TestCloumn96Sum {

  val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSqlJdbcMysqlSparkSession")
  // 创建sparksession 对象
  val sparkSession: SparkSession = SparkSession.builder().config(conf).getOrCreate()

  import sparkSession.implicits._

  case class curCnt(ORG_NO: String, METER_ID: String, p96Cnt: Int, p48Cnt: Int, p24Cnt: Int)

  def main(args: Array[String]): Unit = {

    // 一行转多行
    val df: DataFrame = sparkSession.sparkContext.parallelize(List(("aa", "18520", 1.4, 2.5, 35.6, 7.7, 9.58, 145.569, 6.23, 2.5), ("bb", "18521", 4.5, 2.5, 3.654, 5.5, 9.5, 645.8753, 2.5, 8.5), ("aa", "18522", 3.5, 5.5, 6.5, 7.5, 1.5, 1.5, 1.5, 4.5)))
      .toDF("ORG_NO", "METER_ID", "p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8")
    val df1: DataFrame = df.select(
      expr("ORG_NO"),
      expr("METER_ID"),
      expr("p1"), expr("p2"), expr("p3"), expr("p4"), expr("p5"),
      expr("p6"), expr("p7"), expr("p8"), expr(" 16 as p9 "), expr(" 16 as p10 "),
      expr(" 16 as p11 "), expr(" 16 as p12 "), expr(" 16 as p13 "), expr(" 16 as p14 "),
      expr(" 16 as p15 "), expr(" 16 as p16 "), expr(" 16 as p17 "), expr(" 16 as p18 "),
      expr(" 16 as p19 "), expr(" 16 as p20 "), expr(" 16 as p21 "), expr(" 16 as p22 "),
      expr(" 16 as p23 "), expr(" 16 as p24 "), expr(" 16 as p25 "), expr(" 16 as p26 "),
      expr(" 16 as p27 "), expr(" 16 as p28 "), expr(" 16 as p29 "), expr(" 16 as p30 "),
      expr(" 16 as p31 "), expr(" 16 as p32 "), expr(" 16 as p33 "), expr(" 16 as p34 "),
      expr(" 16 as p35 "), expr(" 16 as p36 "), expr(" 16 as p37 "), expr(" 16 as p38 "),
      expr(" 16 as p39 "), expr(" 16 as p40 "), expr("Null as p41 "), expr(" 16 as p42 "),
      expr(" 16 as p43 "), expr(" 16 as p44 "), expr(" 16 as p45 "), expr(" 16 as p46 "),
      expr(" 16 as p47 "), expr(" 16 as p48 "), expr(" 16 as p49 "), expr(" 16 as p50 "),
      expr(" 16 as p51 "), expr(" 16 as p52 "), expr(" 16 as p53 "), expr(" 16 as p54 "),
      expr(" 16 as p55 "), expr(" 16 as p56 "), expr(" 16 as p57 "), expr(" 16 as p58 "),
      expr(" 16 as p59 "), expr(" 16 as p60 "), expr(" 16 as p61 "), expr(" 16 as p62 "),
      expr(" 16 as p63 "), expr("Null as p64 "), expr(" 16 as p65 "),
      expr(" 16 as p66 "), expr(" 16 as p67 "), expr(" 16 as p68 "), expr(" 16 as p69 "),
      expr(" 16 as p70 "), expr(" 16 as p71 "), expr(" 16 as p72 "),
      expr(" 16 as p73 "), expr(" 16 as p74 "), expr(" 16 as p75 "), expr(" 16 as p76 "),
      expr(" 16 as p77 "), expr(" 16 as p78 "), expr(" 16 as p79 "), expr(" 16 as p80 "),
      expr(" 16 as p81 "), expr(" 16 as p82 "), expr(" 16 as p83 "), expr(" 16 as p84 "),
      expr(" 16 as p85 "), expr(" 16 as p86 "), expr(" 16 as p87 "), expr(" 16 as p88 "),
      expr(" 16 as p89 "), expr(" 16 as p90 "), expr(" 16 as p91 "), expr(" 16 as p92 "),
      expr(" 16 as p93 "), expr(" 16 as p94 "), expr(" 16 as p95 "), expr(" 16 as p96 "))

    val cntSumUdf: UserDefinedFunction = udf((cnt: String) => {
      val cntArray: Array[String] = cnt.split("_")
      var p96sum: Int = 0
      var p48sum: Int = 0
      var p24sum: Int = 0
      for (i <- 0 to cntArray.length - 1) {
        p96sum += cntArray(i).toInt
      }
      for (i <- 0.to(cntArray.length - 1, 2)) {
        p48sum += cntArray(i).toInt
      }
      for (i <- 0.to(cntArray.length - 1, 4)) {
        p24sum += cntArray(i).toInt
      }
      (p96sum + "_" + p48sum + "_" + p24sum)
    })

    val df2: DataFrame = df1.select($"ORG_NO", $"METER_ID", concat_ws("_", (for (i <- 1 to 96) yield expr(s"CASE WHEN p${i} IS NULL THEN 0 ELSE 1 END")): _*) as "p1_96")
    val df3: DataFrame = df2.select($"ORG_NO", $"METER_ID", cntSumUdf($"p1_96") as "p1_96")
      .withColumn("tmp_96", split($"p1_96", "_"))

    val df4: DataFrame = df3.select($"METER_ID",
      $"tmp_96".getItem(0).cast("int").as(s"sum_p96"),
      $"tmp_96".getItem(1).cast("int").as(s"sum_p48"),
      $"tmp_96".getItem(2).cast("int").as(s"sum_p24"))

    df2.printSchema()
    df3.printSchema()
    df4.printSchema()
    df4.show(100, false)


    //    val result1: DataFrame =
    //      df1.select($"ORG_NO",$"METER_ID",
    //      concat_ws(",", (for (i <- Range(1, 97, 1)) yield expr(s"CASE WHEN p${i} IS NULL THEN 0 ELSE 1 END")): _*) as "p_96")
    //
    ////    result1.show()
    //
    //    val result2: DataFrame = result1.withColumn("p1_96", explode(split($"p_96", ",")))
    //        .withColumn("SUCC_CNT",sum($"p1_96").over(Window.partitionBy("ORG_NO","METER_ID")))
    //
    //    result2.show()
    ////      .filter(sum($"p1_96") >= 80)
    ////      .groupBy($"ORG_NO", $"METER_ID")
    ////      .agg(sum($"p1_96") as "SUCC_CNT").filter($"SUCC_CNT".cast("int") >= 80)
    //
    //    result2
    //      .filter($"SUCC_CNT" >= 80)
    //      .groupBy($"ORG_NO")
    //      .agg(
    //        countDistinct($"METER_ID") as "SUCC_CNT"
    //      ).show(false)

  }

  val EMpDayDataCloumn: Set[String] = Set("ORG_NO", "METER_ID", "p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9", "p10", "p11", "p12", "p13", "p14", "p15", "p16", "p17", "p18", "p19", "p20",
    "p21", "p22", "p23", "p24", "p25", "p26", "p27", "p28", "p29", "p30", "p31", "p32", "p33", "p34", "p35", "p36", "p37", "p38", "p39", "p40", "p41",
    "p42", "p43", "p44", "p45", "p46", "p47", "p48", "p49", "p50", "p51", "p52", "p53", "p54", "p55", "p56", "p57", "p58", "p59", "p60", "p61", "p62",
    "p63", "p64", "p65", "p66", "p67", "p68", "p69", "p70", "p71", "p72", "p73", "p74", "p75", "p76", "p77", "p78", "p79", "p80", "p81", "p82", "p83",
    "p84", "p85", "p86", "p87", "p88", "p89", "p90", "p91", "p92", "p93", "p94", "p95", "p96")

}
