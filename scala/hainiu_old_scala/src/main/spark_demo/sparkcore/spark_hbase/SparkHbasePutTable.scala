package sparkcore.spark_hbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HBaseConfiguration, HTableDescriptor, NamespaceDescriptor, TableName}
import org.apache.hadoop.hbase.client.{Admin, Connection, ConnectionFactory, HTable, Put, Table}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 1.rdd的一个元素创建一个连接，效率低
object SparkHbasePutTable {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkHbasePutTable")
    val sc = new SparkContext(conf)

    val rdd: RDD[Int] = sc.parallelize(10 to 20, 2)

    // foreach 是个action算子,无返回值
    // f 代表rdd的每个元素
    rdd.foreach(f =>{
      // 加载hbase的配置
      val hbaseConf: Configuration = HBaseConfiguration.create()
      // 创建hbase连接
      val conn: Connection = ConnectionFactory.createConnection(hbaseConf)
      // 获取表操作对象
//      val table: Table = conn.getTable(TableName.valueOf("panniu:spark_user"))
      val table: HTable = conn.getTable(TableName.valueOf("panniu:spark_user")).asInstanceOf[HTable]

      // 行键
      val put = new Put(Bytes.toBytes(s"spark_put_${f}"))
      put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("count"), Bytes.toBytes(s"${f}"))
      // 写入一行数据
      table.put(put)
      table.close()
      conn.close()
    })
  }
}
