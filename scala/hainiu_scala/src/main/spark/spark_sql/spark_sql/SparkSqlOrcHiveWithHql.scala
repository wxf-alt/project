package spark_sql.spark_sql

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

object SparkSqlOrcHiveWithHql {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkSqlOrcHiveWithHiveConfig").setMaster("local[*]")
    conf.set("spark.sql.shuffle.partitions", "2")
    val sc: SparkContext = new SparkContext(conf)
    val orcPath: String = "/tmp/sparksql/input_orc"

    // 创建hivecontext
    val hivec: HiveContext = new HiveContext(sc)

    // 建hive数据库
    hivec.sql("create database if not exists hivebase")

    // 进入数据库
    hivec.sql("use hivebase")

    // 基于orc文件创建表
    hivec.sql(
      """
        |CREATE TABLE `user_orc`(
        |  `aid` string COMMENT 'from deserializer',
        |  `pkgname` string COMMENT 'from deserializer',
        |  `uptime` bigint COMMENT 'from deserializer',
        |  `type` int COMMENT 'from deserializer',
        |  `country` string COMMENT 'from deserializer')
        |ROW FORMAT SERDE
        |  'org.apache.hadoop.hive.ql.io.orc.OrcSerde'
        |STORED AS INPUTFORMAT
        |  'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'
        |OUTPUTFORMAT
        |  'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'
      """.stripMargin)

    // 将orcPath的文件数据导入到hive表中
    hivec.sql(s"load data local inpath '${orcPath}' overwrite into table user_orc")

    //  select country, num from
    // (select country,count(*) as num from xxx group by country) t
    // where t.num > 5
    val queryDF: DataFrame = hivec.sql(
      """
select concat(t.country, '\t',t.num) from
(select country,count(*) as num from user_orc group by country) t
where t.num > 5
      """.stripMargin)

    queryDF.printSchema()
    queryDF.show(5)
  }
}