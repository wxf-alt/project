import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
 * @Auther: wxf
 * @Date: 2022/6/27 10:14:23
 * @Description: CreateDataFrameTest
 * @Version 1.0.0
 */
object CreateDataFrameTest {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("!!!").setMaster("local[*]")
    val sparkSession: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    val seq = Seq(Row("32401", "河北省", "01", "1111", "03", "18226893545", "50G", "20G"),
      Row("32401", "河北省", "01", "2222", "03", "11111111111", "50G", "10G"),
      Row("3240101", "石家庄", "02", "1111", "02", "22222222222", "50G", "5G"))

    val structFields: Array[StructField] = Array[StructField](
      StructField("ORG_NO", StringType),
      StructField("ORG_NAME", StringType),
      StructField("ORG_TYPE", StringType),
      StructField("SIM_SEGMENT_CODE", StringType),
      StructField("SIM_NO", StringType),
      StructField("PACKAGE_FLOW", StringType),
      StructField("USED_FLOW", StringType),
      StructField("USED_FLOW_BAK", StringType))
    val data: StructType = StructType(structFields)

    val rdd: RDD[Row] = sparkSession.sparkContext.parallelize(seq)

    val frame: DataFrame = sparkSession.createDataFrame(rdd, data)
    frame.show()
  }
}
