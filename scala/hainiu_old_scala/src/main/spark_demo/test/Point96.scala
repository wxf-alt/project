package test

import org.apache.spark.sql.Column
import org.apache.spark.sql.functions._

object Point96 {
  def main(args: Array[String]): Unit = {
    //       concat_ws(",", (for (i <- Range(1, 97, 1)) yield expr(s"CASE WHEN p${i} IS NULL THEN 0 ELSE 1 END")): _*) as "p_96")
//    val list: List[Column] = (for (i <- 1 to 96) yield expr(s"case when P${i} is null then 1 else 0 end") as s"P${i}" ).toList ::: List(expr("METER_ID"))
//    println(list.toBuffer)
    val sumArray: Array[Column] = (for (i <- 1 to 96) yield sum(s"P${i}")).toArray
    println(sumArray.toBuffer)

  }
}
