
import java.text.SimpleDateFormat
import java.util
import java.util.Calendar

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.streaming.api.functions.sink.TwoPhaseCommitSinkFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.runtime.operators.GenericWriteAheadSink
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DecimalType, IntegerType, StringType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Auther: wxf
 * @Date: 2022/5/31 09:35:03
 * @Description: Flink 流处理
 * @Version 1.0.0
 */
object FlinkStreamText1 {
  def main(args: Array[String]): Unit = {
    //生成配置对象
    val conf: Configuration = new Configuration()
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    // web 查看 http://localhost:8081/
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)
    environment.setParallelism(1)

  }

}