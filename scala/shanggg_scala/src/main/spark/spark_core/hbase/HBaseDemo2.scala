package spark_core.hbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

//noinspection DuplicatedCode
// 向 HBase 写数据
object HBaseDemo2 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("HBaseDemo2").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(conf)

    val hbaseConf: Configuration = HBaseConfiguration.create()
    hbaseConf.set("hbase.zookeeper.quorum", "nn1.hadoop,nn2.hadoop,s1.hadoop")
    hbaseConf.set(TableOutputFormat.OUTPUT_TABLE, "student")
    // 通过job来设置输出的格式的类
    val job: Job = Job.getInstance(hbaseConf)
    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Put])

    val initialRDD: RDD[(String, String, String)] = sc.parallelize(List(("100", "apple", "11"), ("200", "banana", "12"), ("300", "pear", "13")))
    val hbaseRDD: RDD[(ImmutableBytesWritable, Put)] = initialRDD.map(x => {
      val put: Put = new Put(Bytes.toBytes(x._1))
      put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(x._2))
      put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("weight"), Bytes.toBytes(x._3))
      (new ImmutableBytesWritable(Bytes.toBytes(x._1)), put)
    })
    hbaseRDD.saveAsNewAPIHadoopDataset(job.getConfiguration)
  }
}
