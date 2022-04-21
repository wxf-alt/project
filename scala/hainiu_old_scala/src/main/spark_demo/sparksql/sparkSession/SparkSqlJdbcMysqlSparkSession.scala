package sparksql.sparkSession

import java.util.Properties

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame, SparkSession}

// 1.sparkSession使用 连接 mysql 获取数据
object SparkSqlJdbcMysqlSparkSession {
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf: SparkConf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("SparkSqlJdbcMysqlSparkSession")

    //      .set("spark.sql.shuffle.partitions","2")

    // 创建sparksession 对象
    val sparkSession: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    import sparkSession.implicits._

    // 方案1
    var props: Properties = new Properties
    props.setProperty("user", "root")
    props.setProperty("password", "root")
    // 将MySQL表转成DataFrame
    val sdf: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student", props).select("s_id", "s_name", "s_examnum").as("s")
//    val scdf: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db", "student1", props).select("s_id", "s_sex", "s_birthday", "sc_id").as("sc")

    val frame1: DataFrame = sdf.withColumn("s_examnum", $"s_examnum".cast("int"))

    val array: Array[Column] = Array(
      expr("s_id"),
      expr("s_name"),
      expr("s_examnum"),
      expr("sum(s_examnum / 0.537) over(partition by s_id) as s_examnum1").cast("float")
    )
//    val strings: Array[String] = array ++ Array("s_id", "s_name", "s_examnum")
//    val array1: Array[String] = strings ++ Array("a")
//    println(array1.toBuffer)
    val frame: DataFrame = frame1.select(array:_*)
    frame.show(10)
    frame.printSchema()

//    val frame: DataFrame = sdf.join(scdf, Seq("s_id"))
//    frame.show()
//    val frame1: DataFrame = sdf.join(scdf, Seq("s_id"),"left_anti").select("s_id","s_name","s_examnum")
//    frame1.show()
//    val result1: DataFrame = sdf.as("s").join(scdf.as("sc"), $"s.s_id" === $"sc.s_id", "left_outer")
//      .withColumn("LATE_PEAK_LOAD_GAP", lit(0))
//      .select($"s.s_id",
//        $"s.s_name",
//        $"s.s_examnum",
//        $"LATE_PEAK_LOAD_GAP"
//      )
//    result1.show()


//    // group by 内的字段 会输出 。 --》 输出 两个 s_name
//    val result: DataFrame = sdf
//      .select($"s.s_id", $"s.s_name")
//      .groupBy("s_id", "s_name")
//      .agg(
//        max($"s_name").as("s_name")
//      ).as("result")
//
//    val result2: DataFrame = result.join(scdf, $"result.s_id" === $"sc.s_id", "left_outer")
//      .select($"result.s_id", $"result.s_name")
//
//    result.printSchema()
//    result2.printSchema()

    //    sdf.printSchema()
    //    scdf.printSchema()


    //    //方案2
    //    val scdf: DataFrame = sparkSession.read.format("jdbc")
    //      .option("driver", classOf[Driver].getName)
    //      .option("url", "jdbc:mysql://localhost:3306/db")
    //      .option("dbtable", "student2")
    //      .option("user", "root")
    //      .option("password", "root").load()
    ////    val joinDF: Dataset[test.Row] = sdf.join(scdf, "s_id").coalesce(5)
    //
    //    sdf.createOrReplaceTempView("s")
    //    scdf.createOrReplaceTempView("sc")
    //    // 通过DataFrame实现了两个表的join
    //    val joinDF: DataFrame = sparkSession.sql("select * from s inner join sc on s.S_ID=sc.S_ID").coalesce(5)
    //    joinDF.printSchema()
    //    joinDF.show()
    //    println(joinDF.rdd.getNumPartitions)


  }
}
