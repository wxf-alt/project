package spark_hbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SparkHbaseTableInputFormatTable {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkHbaseTableInputFormatTable")
    val sc: SparkContext = new SparkContext(conf)

    val hbaseConf: Configuration = HBaseConfiguration.create()
    // 设置表名
    hbaseConf.set(TableInputFormat.INPUT_TABLE,"idea_spark:spark_user")

    //    fClass: Class[F],   inputformatclass
    //    kClass: Class[K],   keyin
    //    vClass: Class[V]    valuein
    val hbaseInputRdd: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])

    // rdd 的分区数是由 hbase表查询结果数据的region数量决定。
    // 因为只有一个region，所以只有一个分区
    println(s"rdd.size:${hbaseInputRdd.getNumPartitions}")
    hbaseInputRdd.foreach(f => {
      val rowKey: String = Bytes.toString(f._1.get())
      val result: Result = f._2
      val value: String = Bytes.toString(result.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("count")))
      // 打印
      println(s"${rowKey}\tcolumn=cf:count,value=${value}")
    })


  }
}
