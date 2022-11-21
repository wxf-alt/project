package saprk_sql

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
 * @Auther: wxf
 * @Date: 2022/6/28 09:48:46
 * @Description: SparkSessionTest
 * @Version 1.0.0
 */
object SparkSessionTest {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
    val sparkSession: SparkSession = SparkSession.builder().appName("SparkSessionTest").config(conf).master("local[*]").getOrCreate()

    val properties: Properties = new Properties()
    properties.setProperty("user", "root")
    properties.setProperty("password", "root")

    val mySqlDF: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student2", properties)
    val mySqlDF1: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student1", properties)

    // 笛卡尔积
//    val dataFrame: DataFrame = mySqlDF.join(mySqlDF1,Seq(""),"cross")
    val dataFrame: DataFrame = mySqlDF.crossJoin(mySqlDF1)
    println(dataFrame.count())


    //    val dataFrame: DataFrame = mySqlDF.join(mySqlDF1,Seq("s_id"),"left_outer")
    //    dataFrame.show(20)
    //    println("join 后的分区数：" + dataFrame.rdd.getNumPartitions)

    //    println("mySqlDF Partition:" + mySqlDF.rdd.getNumPartitions)
    //    println("mySqlDF1 Partition:" + mySqlDF1.rdd.getNumPartitions)
    //
    //    val mySqlDFS: Dataset[Row] = mySqlDF.union(mySqlDF1)
    //    println("mySqlDFS Partition:" + mySqlDFS.rdd.getNumPartitions)
    //
    //    val mySqlDFS1: Dataset[Row] = mySqlDFS.repartition(128)
    //    println("mySqlDFS1 Partition   1:" + mySqlDFS1.rdd.getNumPartitions)


    //    Thread.sleep(3600000)

  }
}
