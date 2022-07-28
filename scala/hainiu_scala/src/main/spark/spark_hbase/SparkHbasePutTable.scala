package spark_hbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Put, Table}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// rdd的一个元素创建一个连接，效率低
// 一条数据创建一个连接，并且一条一条插入
object SparkHbasePutTable {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkHbasePutTable")
    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.parallelize(10 to 20, 2)
    rdd.foreach(f => {
      // 加载hbase的配置
      val hbaseConf: Configuration = HBaseConfiguration.create()
      // 创建hbase连接
      val conn: Connection = ConnectionFactory.createConnection(hbaseConf)
      // 获取表操作对象
      val table: Table = conn.getTable(TableName.valueOf("idea_spark:spark_user"))

      val put: Put = new Put(Bytes.toBytes(s"spark_put_${f}"))
      put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("count"), Bytes.toBytes(s"${f}"))
      table.put(put)
      table.close()
      conn.close()
    })

  }
}
