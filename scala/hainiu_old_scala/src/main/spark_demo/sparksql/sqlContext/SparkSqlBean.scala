package sparksql.sqlContext

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

// 3.spark-sql用对象自定义schema
object SparkSqlBean {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkSqlBean").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val rdd: RDD[String] = sc.textFile("/tmp/sparksql/input_text")

    // RDD[String] ---> RDD[DFBean]
    // CN	game	cn.gameloft.aa	1
    val beanRdd: RDD[DFBean] = rdd.map(line => {
      val arr: Array[String] = line.split("\t")
      val country: String = arr(0)
      val gpcategory: String = arr(1)
      val pkgName: String = arr(2)
      val num: Int = arr(3).toInt
      new DFBean(country, gpcategory, pkgName, num)
    })



    val sqlc = new SQLContext(sc)
    // 通过rdd 和 DFBean 来创建DataFrame对象
    val df: DataFrame = sqlc.createDataFrame(beanRdd,classOf[DFBean])
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

class DFBean(val country:String, val gpcategory:String, val pkgname:String, val num:Int){
  // 通过反射的方式来调用这些方法获取数据
  // country ---> getCountry
  def getCountry: String = country

  def getGpcategory: String = gpcategory

  def getPkgname: String = pkgname

  def getNum: Int = num
}