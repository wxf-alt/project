package spark_sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @Auther: wxf
 * @Date: 2022/10/13 16:27:05
 * @Description: TestJoin  Spark测试代码验证 join：Seq(字段)与 $"字段" 的区别
 * @Version 1.0.0
 */
object TestJoin {
  def main(args: Array[String]): Unit = {
    // 创建sparksession 对象
    val sparkSession: SparkSession = SparkSession
      .builder()
      .config(new SparkConf().setMaster("local[*]"))
      .appName("SparkSqlSparkSessionMysql")
      .getOrCreate()

    import sparkSession.implicits._

    val fcGc: DataFrame = sparkSession.sql("select ''as gc_id, '' as volt_code, '' as status_code ")
    fcGc.show()

    val fcGcTypeCode: DataFrame = sparkSession.sql("select ''as gc_id, '' as gc_type_code ").as("fr")
    fcGcTypeCode.show()

    val cMeterMpRela: DataFrame = sparkSession.sql("select ''as CONS_ID, '' as METER_ID, '' as CONS_NO ").as("cr")
    cMeterMpRela.show()

    val cCons: DataFrame = sparkSession.sql("select ''as CONS_ID, '' as VOLT_CODE ").as("c")
    cCons.show()

    val rDataMp: DataFrame = sparkSession.sql("select '' as ORG_NO, '' as METER_ID, '' as METER_ASSET_NO, '' as TG_ID, '' as CONS_NO, '' as TMNL_ASSET_NO")
    rDataMp.show()

    println("低压光伏用户")
    val lowVolPhotoVoltaicResult: DataFrame = fcGc.filter($"volt_code".isin("AC02202", "AC03802")).as("f")
      .join(fcGcTypeCode, $"f.gc_id" === $"fr.gc_id", "left_outer")
      .join(cMeterMpRela, $"fr.gc_id" === $"cr.cons_id", "left_outer")
      .join(rDataMp.as("r"), $"cr.meter_id" === $"r.meter_id", "left_outer")
    //      .select($"r.ORG_NO", $"r.METER_ID", $"r.METER_ASSET_NO", $"r.TG_ID", $"r.CONS_NO", $"r.TMNL_ASSET_NO", lit("03") as "CONS_TYPE")
    lowVolPhotoVoltaicResult.printSchema()

    println("低压光伏用户1")
    val lowVolPhotoVoltaicResult1: DataFrame = fcGc.filter($"volt_code".isin("AC02202", "AC03802")).as("f")
      .join(fcGcTypeCode, Seq("gc_id"), "left_outer")
      .join(cMeterMpRela, $"f.gc_id" === $"cr.cons_id", "left_outer")
      .join(rDataMp.as("r"), Seq("meter_id"), "left_outer")
      .select($"meter_id", $"gc_id")
    lowVolPhotoVoltaicResult1.printSchema()

    //    println("高压光伏用户")
    //    val highVolPhotoVoltaicResult: DataFrame = fcGc.filter(!$"volt_code".isin("AC02202", "AC03802")).as("f1")
    //      .join(fcGcTypeCode, $"f1.gc_id" === $"fr.gc_id")
    //      .join(cMeterMpRela, $"fr.gc_id" === $"cr.cons_id")
    //      .join(rDataMp.as("r"), $"cr.meter_id" === $"r.meter_id")
    //      .select($"r.ORG_NO", $"r.METER_ID", $"r.METER_ASSET_NO", $"r.TG_ID", $"r.CONS_NO", $"r.TMNL_ASSET_NO", lit("04") as "CONS_TYPE")
    //    highVolPhotoVoltaicResult.printSchema()
    //
    //    println("低压用户")
    //    val lowVolResult: DataFrame = cCons.join(cMeterMpRela, $"c.CONS_ID" === $"cr.CONS_ID")
    //      .join(rDataMp.as("r"), $"cr.CONS_NO" === $"r.CONS_NO" && $"cr.METER_ID" === $"r.METER_ID")
    //      .select($"ORG_NO", $"r.METER_ID", $"r.METER_ASSET_NO", $"r.TG_ID", $"r.CONS_NO", $"r.TMNL_ASSET_NO", lit("06") as "CONS_TYPE")
    //    lowVolResult.printSchema()

  }
}
