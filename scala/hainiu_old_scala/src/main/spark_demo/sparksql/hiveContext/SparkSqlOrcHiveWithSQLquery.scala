package sparksql.hiveContext

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.{SparkConf, SparkContext}

// 3.使用SQL形式实现第4点的功能
object SparkSqlOrcHiveWithSQLquery {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkSqlOrcHiveWithSQLquery").setMaster("local[*]")
    conf.set("spark.sql.shuffle.partitions","1")
    val sc = new SparkContext(conf)
    val orcPath = "/tmp/sparksql/input_orc"

    // 创建hivecontext
    val hivec = new HiveContext(sc)
    val df: DataFrame = hivec.read.orc(orcPath)
    df.printSchema()
    df.show(5)

    //  select country, num from
    // (select country,count(*) as num from xxx group by country) t
    // where t.num > 5
    df.createOrReplaceTempView("user_orc")

    val queryDF: DataFrame = hivec.sql(
      """
select concat(t.country, '\t',t.num) from
(select country,count(*) as num from user_orc group by country) t
where t.num > 5
      """.stripMargin)

    val rdd: RDD[Row] = queryDF.rdd
    val rdd2: RDD[String] = rdd.map(row => row.getString(0))
    val outputPath:String = "/tmp/sparksql/output_orc2txt"
    import utils.MyPredef.string2HdfsDelete
    outputPath.deletePath
    rdd2.saveAsTextFile(outputPath)

  }
}