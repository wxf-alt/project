package sparksql.sqlContext

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

// 1.spark-sql的json
object SparkSqlJson {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkSqlJson").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val jsonPath = "/tmp/sparksql/input_json/data_json.txt"

    // 创建SQLContext
    val sqlc = new SQLContext(sc)

    // 读取json数据
    val df: DataFrame = sqlc.read.json(jsonPath)

    // 查看DataFrame中的内容
    df.show()

    // 查看schema信息
    df.printSchema()

    // 查看country列中的内容
    df.select(df.col("country")).show()
    df.select(df("country")).show()
    df.select("country").show()

    // 查询所有country和num，并把num+1
    val df2: DataFrame = df.select(df("country"), (df("num") + 1).as("num1"))
    df2.printSchema()

    // 查询num < 2 的数据
    // type DataFrame = Dataset[test.Row]
    val filter: Dataset[Row] = df.filter(df("num") < 2)
    filter.show()

    // 按照country 统计相同country的数量
    // select country,count(*) from xxxx group by country
    val count: DataFrame = df.groupBy("country").count()
    count.show()
    count.printSchema()
    // 将统计后的结果保存到hdfs上
    // DataFrame ---> rdd
    // 因为shuffle会产生200个分区，所以重新分区减少文件的输出
    val rdd: RDD[Row] = count.rdd.coalesce(2)
    val outputPath:String = "/tmp/sparksql/output_json"
    import utils.MyPredef.string2HdfsDelete
    outputPath.deletePath
    rdd.saveAsTextFile(outputPath)
  }
}