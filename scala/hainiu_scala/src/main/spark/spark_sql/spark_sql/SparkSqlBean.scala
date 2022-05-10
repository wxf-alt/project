package spark_sql.spark_sql

import org.apache.hadoop.conf.Configuration
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

object SparkSqlBean {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkSqlSchema")
      .setMaster("local[*]")

    // 设置本地文件系统
    val sc: SparkContext = new SparkContext(conf)
    val configuration: Configuration = sc.hadoopConfiguration
    configuration.set("fs.defaultFS", "file:///")

    val textPath: String = """E:\A_data\4.测试数据\spark-sql数据\input_text.txt"""
    val rdd: RDD[String] = sc.textFile(textPath)

    val beanRdd: RDD[DFBean] = rdd.map(f => {
      val arr: Array[String] = f.split("\t")
      val country: String = arr(0)
      val gpcategory: String = arr(1)
      val pkgName: String = arr(2)
      val num: Int = arr(3).toInt
      new DFBean(country, gpcategory, pkgName, num)
    })

    val sqlc: SQLContext = new SQLContext(sc)
    val df: DataFrame = sqlc.createDataFrame(beanRdd, classOf[DFBean])
    df.printSchema()
    df.show()

    // 给df 创建一个临时视图，beantable 是视图名称
    df.createOrReplaceTempView("beantable")

    // 通过SQL的方式直接写SQL来得到查询结果
    //筛选这个表中带有CN的数据
    val filter: DataFrame = sqlc.sql("select country,num from beantable where country like '%CN%'")
    filter.printSchema()
    filter.show()

    val filterRdd: RDD[Row] = filter.rdd
    val resRdd: RDD[String] = filterRdd.map(row => s"${row.getString(0)}\t${row.getInt(1)}")
    val arr: Array[String] = resRdd.collect()
    println(arr.toBuffer)
  }
}
case class DFBean(country:String, gpcategory:String, pkgname:String, num:Int){
  // 通过反射的方式来调用这些方法获取数据
  // country ---> getCountry
  def getCountry: String = country
  def getGpcategory: String = gpcategory
  def getPkgname: String = pkgname
  def getNum: Int = num
}