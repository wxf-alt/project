package sparksql.sqlContext

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

// 2.spark-sql自定义schema
object SparkSqlSchema {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkSqlSchema").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val rdd: RDD[String] = sc.textFile("/tmp/sparksql/input_text")

    // RDD[String] ---> RDD[test.Row]
    // CN	game	cn.gameloft.aa	1
    val rowRdd: RDD[Row] = rdd.map(line => {
      val arr: Array[String] = line.split("\t")
      val country: String = arr(0)
      val gpcategory: String = arr(1)
      val pkgName: String = arr(2)
      val num: Int = arr(3).toInt
      Row(country, gpcategory, pkgName, num)
    })


    val sqlc = new SQLContext(sc)
    val fields: ArrayBuffer[StructField] = ArrayBuffer[StructField]()
    //                     字段名     字段类型          是否可以为null
    fields += StructField("country",DataTypes.StringType,true)
    fields += StructField("gpcategory",DataTypes.StringType,true)
    fields += StructField("pkgname",DataTypes.StringType,true)
    fields += StructField("num",DataTypes.IntegerType,true)

    // StructType里面包着每个字段元数据信息
    val structType: StructType = StructType(fields)
    // 通过rdd 和 structType 来创建DataFrame对象
    val df: DataFrame = sqlc.createDataFrame(rowRdd,structType)
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
