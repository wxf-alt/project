import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions.{col, explode, expr, split}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, HashSet, ListBuffer}
import scala.util.control.Breaks

object ForTest {

  def main(args: Array[String]): Unit = {
    val buffer: ArrayBuffer[String] = fieldToArray()
    println(buffer(0))
    println(buffer(1))
    println(buffer(2))
  }


//  def maximumLoad(df:DataFrame): DataFrame ={
//    val fieldBuffer: ArrayBuffer[String] = fieldToArray()
//    val loadDf: DataFrame = df.select(
//      expr("MGT_ORG_CODE"),
//      expr(s"CONCAT_WS(',',${fieldBuffer(0)}"),
//      expr(s"CONCAT_WS(',',${fieldBuffer(1)}"),
//      expr(s"CONCAT_WS(',',${fieldBuffer(2)}")
////      expr("CONCAT_WS(','," + EnumerationData.pNum + ") as p1_96")
//    )
//    val loadResultTmp: DataFrame = loadDf.withColumn("max_p45", explode(split(col("max_p45"), ",")).cast("int"))
//      .withColumn("max_p69", explode(split(col("max_p69"), ",")).cast("int"))
//      .withColumn("max_p93", explode(split(col("max_p93"), ",")).cast("int"))
//      .withColumn("p1_96", explode(split(col("p1_96"), ",")).cast("int"))
//
//    val aggSeq1: Seq[Column] = Seq(
//      expr("max(max_p45)") as "EARLY_PEAK_BASE_LOAD",
//      expr("max(max_p69)") as "WAIST_PEAK_BASE_LOAD",
//      expr("max(max_p93)") as "LATE_PEAK_BASE_LOAD",
//      expr("max(p1_96)") as "MAX_BASE_p1_96",
//      expr("sum(p1_96)") as "SUM_BASE_p1_96",
//      expr("avg(p1_96)") as "AVG_BASE_p1_96"
//    )
////    val result: DataFrame = OrgStat2Utils.calcOrgStat(loadResultTmp, aggSeq1, 64, Array("MGT_ORG_CODE"))
////    return result
//  }


  def fieldToArray(): ArrayBuffer[String] = {
    val breaks: Breaks = new Breaks
    var field: String = ""
    val buffer: ArrayBuffer[String] = ArrayBuffer[String]()
    val buffer2: ArrayBuffer[String] = ArrayBuffer[String]()
    for (i <- 29 to 93) {
      breaks.breakable(
        if(i == 45 || i == 69 || i == 93) {
          field += s",p${i}) as max_p${i}"
          buffer.append(field)
          field = ""
          breaks.break()
        }
        else {
          field = field + ",p" + i
        }
      )
    }
    for(i <- buffer.indices){
      buffer2.append(buffer(i).substring(1,buffer(i).length))
    }
    buffer2
  }

}
