package spark_hbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Put, Table}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// scan 'idea_spark:spark_user',{COLUMNS => ['cf1:count'],STARTROW => 'spark_put_20'}
// 一组数据创建一个链接，输入
object SparkHbaseBatchPutTable {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkHbaseBatchPutTable")
    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.parallelize(21 to 41, 2)
    rdd.foreachPartition(it => {
      // 加载 HBase 配置
      val hbaseConf: Configuration = HBaseConfiguration.create()
      // 创建 HBase 连接
      val conn: Connection = ConnectionFactory.createConnection(hbaseConf)
      // 创建 Table 操作对象
      val table: Table = conn.getTable(TableName.valueOf("idea_spark:spark_user"))
      // f 代表每个分区数据集中的每个元素
      val puts: Iterator[Put] = it.map(f => {
        val put = new Put(Bytes.toBytes(s"spark_puts_${f}"))
        put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("count"), Bytes.toBytes(s"${f}"))
        put
      })

      // 将 scala 集合转为 java 集合
      import scala.collection.JavaConversions._
      // 写入一个分区的数据
      table.put(puts.toList)
      table.close()
      conn.close()
    })

  }
}
