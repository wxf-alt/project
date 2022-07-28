import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @Auther: wxf
 * @Date: 2022/6/27 11:02:31
 * @Description: CreateTableTest
 * @Version 1.0.0
 */
object CreateTableTest {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("!!!").setMaster("local[*]")
    val sparkSession: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import sparkSession.implicits._

    val frame = sparkSession.sparkContext.parallelize(Seq(Seq("", "", "", ""),Seq("", "", "", "")))
      .map(x => {
        val strings: Array[String] = x.toArray[String]
        (strings(0),strings(1),strings(2),strings(3))
      }).toDF("ORG_NO", "CONS_TYPE", "METER_TYPE", "R_MAX_SUCC_RATE")
    frame.show()

  }
}
