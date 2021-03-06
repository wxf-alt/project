package sparkcore.spark_hbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, HTable, Put}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 2.一个分区创建一个连接，将一个分区的数据通过 put(list) 批量写入。
object SparkHbaseBatchPutTable {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkHbasePutsTable")
    val sc = new SparkContext(conf)

    val rdd: RDD[Int] = sc.parallelize(10 to 20, 2)

    // foreach 是个action算子,无返回值
    // it 代表rdd的每个分区的数据集
    rdd.foreachPartition(it =>{
      // 加载hbase的配置
      val hbaseConf: Configuration = HBaseConfiguration.create()
      // 创建hbase连接
      val conn: Connection = ConnectionFactory.createConnection(hbaseConf)
      // 获取表操作对象
      val htable: HTable = conn.getTable(TableName.valueOf("panniu:spark_user")).asInstanceOf[HTable]
      // f 代表每个分区数据集中的每个元素
      val puts: Iterator[Put] = it.map(f => {
        val put = new Put(Bytes.toBytes(s"spark_puts_${f}"))
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("count"), Bytes.toBytes(s"${f}"))
        put
      })
      // 通过scala的隐式转换将scala的序列转成 java的list
      import scala.collection.convert.wrapAsJava.seqAsJavaList
      // 写入一个分区的数据
      htable.put(puts.toList)
      htable.close()
      conn.close()
    })

  }
}
