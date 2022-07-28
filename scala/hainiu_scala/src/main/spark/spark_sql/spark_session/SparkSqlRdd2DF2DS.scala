package spark_sql.spark_session

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._

object SparkSqlRdd2DF2DS {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]")
    conf.set("spark.sql.shuffle.partitions","1")
    // 创建sparksession 对象
    val sparkSession: SparkSession = SparkSession.builder().config(conf).appName("SparkSqlRdd2DF2DS").getOrCreate()

    val rdd: RDD[(String, Int)] = sparkSession.sparkContext.parallelize(List(("aa",1),("bb",2),("aa",3)),2)
    // 引入隐式转换
    import sparkSession.implicits._
    // rdd ---> df
    // 通过 隐式转换函数rddToDatasetHolder,给rdd赋予 DatasetHolder
    val df: DataFrame = rdd.toDF("word","num")
    df.printSchema()
    df.show()

    // rdd ---> ds
    //    val ds: Dataset[(String, Int)] = rdd.toDS()
    //    ds.printSchema()
    //    ds.show()
    val dsRdd: RDD[DSBean] = rdd.map(f => {
      DSBean(f._1, f._2)
    })
    val beanDS: Dataset[DSBean] = dsRdd.toDS()
    beanDS.printSchema()
    beanDS.show()

    // df --> ds
    val beanDS2: Dataset[DSBean] = df.map(row => {
      DSBean(row.getString(0), row.getInt(1))
    })

    val beanDS3: Dataset[DSBean] = df.map({
      case Row(word: String, num: Int) => DSBean(word, num)
    })

    // ds ----> df
    val df3: DataFrame = beanDS3.toDF()
    val ds4: Dataset[DSBean] = df3.as[DSBean]
    df3.printSchema()
    df3.show()

    // df、ds --->rdd
    val dfRdd: RDD[Row] = df3.rdd
    val ds2Rdd: RDD[DSBean] = beanDS.rdd
    val beans: Array[DSBean] = ds2Rdd.collect()
    println(beans.toBuffer)
  }
}
// 样例类
case class DSBean(word:String,num:Int)
