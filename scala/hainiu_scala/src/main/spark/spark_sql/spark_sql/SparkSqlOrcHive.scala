package spark_sql.spark_sql

import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.{DataFrame, Dataset, Row, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}

object SparkSqlOrcHive {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkSqlOrcHive").setMaster("local[*]")
    conf.set("spark.sql.shuffle.partitions", "1")
    val sc: SparkContext = new SparkContext(conf)
    val configuration: Configuration = sc.hadoopConfiguration
    configuration.set("fs.defaultFS", "file:///")
    val orcPath: String = """E:\A_data\4.测试数据\spark-sql数据\hive_orc"""

    // 创建 hiveContext
    val hiveContext: HiveContext = new HiveContext(sc)
    val df: DataFrame = hiveContext.read.orc(orcPath)
    df.show()

    val groupByDF: DataFrame = df.groupBy("name").count()
    val filterDs: Dataset[Row] = groupByDF.filter(groupByDF("count") > 5)
    //    filterDs.printSchema()
    //    filterDs.show()

    // 将ds的数据缓存 sparksql 默认缓存级别是 MEMORY_AND_DISK
    val dsCache: filterDs.type = filterDs.cache()
    //    val rdd: RDD[Row] = dsCache.rdd
    //    val rdd2: RDD[String] = rdd.map(f => f.getString(0))
    //    println(rdd2.collect.toList)

    // 设置输出目录
    val orcOutputPath: String = s"""E:\\A_data\\4.测试数据\\输出\\${sc.appName}\\output_ds2orc"""
    val jsonOutputPath: String = s"""E:\\A_data\\4.测试数据\\输出\\${sc.appName}\\output_ds2json"""

    // 写出
    dsCache.write.mode(SaveMode.Overwrite).format("orc").save(orcOutputPath)
    dsCache.write.mode(SaveMode.Overwrite).format("json").save(jsonOutputPath)
  }
}
