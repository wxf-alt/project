package sparksql.sparkSession

import java.{lang, util}
import java.util.Properties

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.util.LongAccumulator
import redis.clients.jedis.{HostAndPort, JedisCluster}

// 1.sparkSession使用 连接 mysql 获取数据
object SparkSqlMysqlToRedis {
  //noinspection UnitInMap
  def main(args: Array[String]): Unit = {

    //1.连接redis
    //    val pool: JedisPool = new JedisPool(new GenericObjectPoolConfig, "qianfeng01", 6379)
    //    val jedis: Jedis = pool.getResource
    //    jedis.auth("123456")

    val conf: SparkConf = new SparkConf()
      .setMaster("local[*]")

    // 创建sparksession 对象
    val sparkSession: SparkSession = SparkSession.builder().config(conf).appName("SparkSqlJdbcMysqlSparkSession").getOrCreate()
    import sparkSession.implicits._

    var props: Properties = new Properties
    props.setProperty("user", "root")
    props.setProperty("password", "root")

    // 连接 Mysql
    val sdf: DataFrame = sparkSession.read.jdbc("""jdbc:mysql://localhost:3306/db""", "student1", props)


    // 获取数据
    val value: Dataset[Row] = sdf.select($"s_id".as("READ_TYPE_CODE"), $"s_name".as("name"), $"s_examnum".as("meterId")).as("s")
    //    value.withColumn("meterId",expr())
    val frame: DataFrame = value.select(
      $"READ_TYPE_CODE".as("id"),
      expr("case when meterId = '11480630' then 'PAP_R' when meterId = '11720510' then 'PAP_R1' when  meterId = '11391225' then 'PAP_R2' else meterId end").as("READ_TYPE_CODE"),
      $"name"
    )
    frame.show(20)

    // 连接redis配置
    val hostAndPort: HostAndPort = new HostAndPort("nn1.hadoop", 6379)
    val redis_host_port: util.HashSet[HostAndPort] = new util.HashSet[HostAndPort]()
    redis_host_port.add(hostAndPort)

    // df -> rdd 向redis中写数据
    val sc: SparkContext = frame.rdd.sparkContext
    val insert_num: LongAccumulator = sc.longAccumulator("insert_num")
    val value1: RDD[Row] = frame.rdd.repartition(5)
    val partitions2: Int = value1.getNumPartitions

    value1.foreachPartition(row => {
      // redis 连接
      val cluster: JedisCluster = new JedisCluster(redis_host_port)
      row.foreach(f => {
        val id: Int = f.getInt(0)
        val READ_TYPE_CODE: String = f.getString(1)
        val name: String = f.getString(2)
//        val demo: demo1 = demo1(id, name, examnum)
        // 插入redis
        for(i <- 0 until 6){
          // 拼接key 插入redis
          val long: lang.Long = cluster.hsetnx("YX:REFRESH:METER:" + READ_TYPE_CODE, READ_TYPE_CODE, "INTERFACE:" + name + ":I" + i)
          // 设置 key 的过期时间
          cluster.expire("YX:REFRESH:METER:" + READ_TYPE_CODE,7200)
//          val str: String = cluster.get("hainiu")
//          println(str)
          insert_num.add(long)
        }
      })
    })

    // 插入数据量
    println("插入：" + insert_num.value)

  }
}

//case class demo1(id: Int, name: String, examnum: String)