package sparkcore.spark_hbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableMapReduceUtil}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{Cell, HBaseConfiguration, TableName}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 6.通过scan定义范围读取hbase表
object SparkHbaseScanRangeTable {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkHbaseScanTable")
    val sc = new SparkContext(conf)

    val scan = new Scan()
    // 设置范围
    scan.setStartRow(Bytes.toBytes("spark_write_p_40"))
    scan.setStopRow(Bytes.toBytes("spark_write_p_500"))

    val hbaseConf: Configuration = HBaseConfiguration.create()
    // 设置表名称
    hbaseConf.set(TableInputFormat.INPUT_TABLE, "panniu:spark_user")
    // 通过将scan对象转成对应的字符串，来设置查询范围
    hbaseConf.set(TableInputFormat.SCAN, TableMapReduceUtil.convertScanToString(scan))

    val rdd: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat],classOf[ImmutableBytesWritable],classOf[Result])
    // rdd 的分区数是由 hbase表查询结果数据的region数量决定。
    // 因为只有一个region，所以只有一个分区
    println(s"rdd.size:${rdd.getNumPartitions}")
    rdd.foreach(f =>{
      val rowkey: String = Bytes.toString(f._1.get())
      val result: Result = f._2
      val value: String = Bytes.toString(result.getValue(Bytes.toBytes("cf"), Bytes.toBytes("count")))
      //      spark_write_p_84              column=cf:count, timestamp=1578988887742, value=84
      println(s"${rowkey}\tcolumn=cf:count,value=${value}")

    })
  }
}

