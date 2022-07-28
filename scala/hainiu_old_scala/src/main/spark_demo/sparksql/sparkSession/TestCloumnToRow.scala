package sparksql.sparkSession

import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame, SparkSession}
import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks

//noinspection DuplicatedCode
object TestCloumnToRow {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSqlJdbcMysqlSparkSession")
    // 创建sparksession 对象
    val sparkSession: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    import sparkSession.implicits._
    // 一行转多行
    val df: DataFrame = sparkSession.sparkContext.parallelize(List(("aa", 1.4, 2.5, 35.6, 7.7, 9.58, 145.569, 6.23, 2.5), ("bb", 4.5, 2.5, 3.654, 5.5, 9.5, 645.8753, 2.5, 8.5), ("aa", 3.5, 5.5, 6.5, 7.5, 1.5, 1.5, 1.5, 4.5)))
      .toDF("name", "p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8")
    val source: String = "test"
    val df1: DataFrame = df.select(
      expr("name") as ("MGT_ORG_CODE"),
      expr("p1"),
      expr("p2"),
      expr("p3"),
      expr("p4"),
      expr("p5"),
      expr("p6"),
      expr("p7"),
      expr("p8"),
      expr(" 16 as p9 "),
      expr(" 16 as p10 "),
      expr(" 16 as p11 "),
      expr(" 16 as p12 "),
      expr(" 16 as p13 "),
      expr(" 16 as p14 "),
      expr(" 16 as p15 "),
      expr(" 16 as p16 "),
      expr(" 16 as p17 "),
      expr(" 16 as p18 "),
      expr(" 16 as p19 "),
      expr(" 16 as p20 "),
      expr(" 16 as p21 "),
      expr(" 16 as p22 "),
      expr(" 16 as p23 "),
      expr(" 16 as p24 "),
      expr(" 16 as p25 "),
      expr(" 16 as p26 "),
      expr(" 16 as p27 "),
      expr(" 16 as p28 "),
      expr(" 16 as p29 "),
      expr(" 16 as p30 "),
      expr(" 16 as p31 "),
      expr(" 16 as p32 "),
      expr(" 16 as p33 "),
      expr(" 16 as p34 "),
      expr(" 16 as p35 "),
      expr(" 16 as p36 "),
      expr(" 16 as p37 "),
      expr(" 16 as p38 "),
      expr(" 16 as p39 "),
      expr(" 16 as p40 "),
      expr(" 16 as p41 "),
      expr(" 16 as p42 "),
      expr(" 16 as p43 "),
      expr(" 16 as p44 "),
      expr(" 16 as p45 "),
      expr(" 16 as p46 "),
      expr(" 16 as p47 "),
      expr(" 16 as p48 "),
      expr(" 16 as p49 "),
      expr(" 16 as p50 "),
      expr(" 16 as p51 "),
      expr(" 16 as p52 "),
      expr(" 16 as p53 "),
      expr(" 16 as p54 "),
      expr(" 16 as p55 "),
      expr(" 16 as p56 "),
      expr(" 16 as p57 "),
      expr(" 16 as p58 "),
      expr(" 16 as p59 "),
      expr(" 16 as p60 "),
      expr(" 16 as p61 "),
      expr(" 16 as p62 "),
      expr(" 16 as p63 "),
      expr(" 16 as p64 "),
      expr(" 16 as p65 "),
      expr(" 16 as p66 "),
      expr(" 16 as p67 "),
      expr(" 16 as p68 "),
      expr(" 16 as p69 "),
      expr(" 16 as p70 "),
      expr(" 16 as p71 "),
      expr(" 16 as p72 "),
      expr(" 16 as p73 "),
      expr(" 16 as p74 "),
      expr(" 16 as p75 "),
      expr(" 16 as p76 "),
      expr(" 16 as p77 "),
      expr(" 16 as p78 "),
      expr(" 16 as p79 "),
      expr(" 16 as p80 "),
      expr(" 16 as p81 "),
      expr(" 16 as p82 "),
      expr(" 16 as p83 "),
      expr(" 16 as p84 "),
      expr(" 16 as p85 "),
      expr(" 16 as p86 "),
      expr(" 16 as p87 "),
      expr(" 16 as p88 "),
      expr(" 16 as p89 "),
      expr(" 16 as p90 "),
      expr(" 16 as p91 "),
      expr(" 16 as p92 "),
      expr(" 16 as p93 "),
      expr(" 16 as p94 "),
      expr(" 16 as p95 "),
      expr(" 16 as p96 ")
    )

    df1.show()

    val column1: Array[String] = transform96Column("MGT_ORG_CODE")

    df1.selectExpr(column1:_*).show()



//    maximumLoad(source, df1, sparkSession)
//    Thread.sleep(1000000)
    //    val array: ArrayBuffer[String] = fieldToArray
    //    println(array(0).replace(")",""))
    //    println(array(1).replace(")",""))
    //    println(array(2).replace(")",""))
  }

  def transform96Column(columns:String*): Array[String] = {
    val buffer: ArrayBuffer[String] = ArrayBuffer[String]()
    for (elem <- columns) {
      buffer.append(elem)
    }
    for(i <- 1 to 96){
      buffer.append(s"case when p$i is null  then 0 else 1 end p$i")
    }
    buffer.toArray
  }


  val pNum = "p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19,p20,p21,p22,p23,p24,p25,p26,p27,p28,p29,p30,p31,p32,p33,p34,p35,p36,p37,p38,p39,p40,p41,p42,p43,p44,p45,p46,p47,p48,p49,p50,p51,p52,p53,p54,p55,p56,p57,p58,p59,p60,p61,p62,p63,p64,p65,p66,p67,p68,p69,p70,p71,p72,p73,p74,p75,p76,p77,p78,p79,p80,p81,p82,p83,p84,p85,p86,p87,p88,p89,p90,p91,p92,p93,p94,p95,p96"

  def fieldToArray(): ArrayBuffer[String] = {
    val breaks: Breaks = new Breaks
    var field: String = ""
    val buffer: ArrayBuffer[String] = ArrayBuffer[String]()
    val buffer2: ArrayBuffer[String] = ArrayBuffer[String]()
    for (i <- 29 to 93) {
      breaks.breakable(
        if (i == 45 || i == 69 || i == 93) {
          field += s",p${i}) "
          buffer.append(field)
          field = ""
          breaks.break()
        }
        else {
          field = field + ",p" + i
        }
      )
    }
    for (i <- buffer.indices) {
      buffer2.append(buffer(i).substring(1, buffer(i).length))
    }
    buffer2
  }

  // 计算 7：00-11：00最大负荷；11：00-17：00最大负荷；17：00-23：00最大负荷；P1到P96最大值
  def maximumLoad(source: String, df: DataFrame, sparkSession: SparkSession): Unit = {

    import sparkSession.implicits._

    val fieldBuffer: ArrayBuffer[String] = fieldToArray()
    val column1: Column = lit(fieldBuffer(0).replace(")", ""))
    val column2: Column = lit(fieldBuffer(1).replace(")", ""))
    val column3: Column = lit(fieldBuffer(2).replace(")", ""))
    val columnPnum: Column = lit(TestCloumnToRow.pNum)


    val loadDf: DataFrame = df.select(
      expr("MGT_ORG_CODE"),
      expr(s"CONCAT_WS(',',$column1) as max_p45"),
      expr(s"CONCAT_WS(',',$column2) as max_p69"),
      expr(s"CONCAT_WS(',',$column3) as max_p93"),
      expr(s"CONCAT_WS(',',$columnPnum) as max_p96")
    ).withColumn("p29_45", explode(split(col("max_p45"), ",")))
      .withColumn("p46_69", explode(split(col("max_p69"), ",")))
      .withColumn("p70_93", explode(split(col("max_p93"), ",")))
      .withColumn("p1_96", explode(split(col("max_p96"), ",")))
      .select($"MGT_ORG_CODE"
        , $"p29_45".cast("double")
        , $"p46_69".cast("double")
        , $"p70_93".cast("double")
        , $"p1_96".cast("double")
      )
    val result: DataFrame = loadDf.groupBy("MGT_ORG_CODE")
      .agg(
        max("p29_45"),
        max("p46_69"),
        max("p70_93"),
        max("p1_96")
      )


    loadDf.printSchema()
    result.printSchema()
    loadDf.show(300)
    result.show(300)
    println(loadDf.count())
    println(result.count())


  }


}
