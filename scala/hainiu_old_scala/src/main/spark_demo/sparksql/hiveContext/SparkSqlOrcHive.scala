package sparksql.hiveContext

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.{DataFrame, Dataset, Row, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}

// 1.读取orc文件，写入orc和json文件。
object SparkSqlOrcHive {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkSqlOrcHive").setMaster("local[*]")
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
    val groupByDF: DataFrame = df.groupBy("country").count()

    val ds: Dataset[Row] = groupByDF.filter(groupByDF("count") > 5)
    ds.printSchema()
    ds.show()

    // 将ds的数据缓存 sparksql 默认缓存级别是MEMORY_AND_DISK
    val dsCache: Dataset[Row] = ds.cache()

    val orcOutputPath:String = "/tmp/sparksql/output_ds2orc"
    val jsonOutputPath:String = "/tmp/sparksql/output_ds2json"

    // 以覆盖写的方式写入orc文件
    dsCache.write.mode(SaveMode.Overwrite).format("orc").save(orcOutputPath)
    // 以覆盖写的方式写入json文件
    dsCache.write.mode(SaveMode.Overwrite).format("json").save(jsonOutputPath)
  }
}