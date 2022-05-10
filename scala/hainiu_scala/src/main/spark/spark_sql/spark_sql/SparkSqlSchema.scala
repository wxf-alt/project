package spark_sql.spark_sql

import org.apache.hadoop.conf.Configuration
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

object SparkSqlSchema {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkSqlSchema")
      .setMaster("local[*]")

    // 设置本地文件系统
    val sc: SparkContext = new SparkContext(conf)
    val configuration: Configuration = sc.hadoopConfiguration
    configuration.set("fs.defaultFS", "file:///")

    val textPath: String = """E:\A_data\4.测试数据\spark-sql数据\input_text.txt"""
    val rdd: RDD[String] = sc.textFile(textPath)
    val rowRdd: RDD[Row] = rdd.map(f => {
      val arr: Array[String] = f.split("\t")
      val country: String = arr(0)
      val gpcategory: String = arr(1)
      val pkgName: String = arr(2)
      val num: Int = arr(3).toInt
      Row(country, gpcategory, pkgName, num)
    })

    val sqlc: SQLContext = new SQLContext(sc)

    val fields: ArrayBuffer[StructField] = ArrayBuffer[StructField]()
    //                 字段名     字段类型          是否可以为null
    fields += StructField("country",DataTypes.StringType,true)
    fields += StructField("gpcategory",DataTypes.StringType,true)
    fields += StructField("pkgname",DataTypes.StringType,true)
    fields += StructField("num",DataTypes.IntegerType,true)

    val structType: StructType = StructType(fields)
    val df: DataFrame = sqlc.createDataFrame(rowRdd, structType)
    df.printSchema()
    df.show()

    //筛选这个表中带有CN的数据
    val filter: Dataset[Row] = df.filter(df("country").like("%CN%"))
    filter.show()
    // 并统计记录数
    val n: Long = filter.count()
    println(s"表中country带有CN的记录数：${n}")
  }
}