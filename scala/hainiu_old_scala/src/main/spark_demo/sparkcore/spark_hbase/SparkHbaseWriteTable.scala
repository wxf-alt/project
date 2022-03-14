package sparkcore.spark_hbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, HTable, Put}
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.{Job, MRJobConfig}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

// 3.用TableOutputFormat 来实现写入数据
object SparkHbaseWriteTable {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkHbaseWriteTable")
    val sc = new SparkContext(conf)

    val rdd: RDD[Int] = sc.parallelize(10 to 20, 2)

    // RDD[Int] --->  RDD[(NullWritable, Put)]
    val pairrdd: RDD[(NullWritable, Put)] = rdd.map(f => {
      val put = new Put(Bytes.toBytes(s"spark_write_${f}"))
      put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("count"), Bytes.toBytes(s"${f}"))
      (NullWritable.get(), put)
    })

    val hbaseConf: Configuration = HBaseConfiguration.create()
    // 设置写入hbase表的表名称
    // import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
    hbaseConf.set(TableOutputFormat.OUTPUT_TABLE, "panniu:spark_user")
    // 以TableOutputFormat格式写入hbase
    hbaseConf.set(MRJobConfig.OUTPUT_FORMAT_CLASS_ATTR, classOf[TableOutputFormat[NullWritable]].getName)
    // 这两个可以不写
    //hbaseConf.set(MRJobConfig.OUTPUT_KEY_CLASS, classOf[NullWritable].getName)
    //hbaseConf.set(MRJobConfig.OUTPUT_VALUE_CLASS, classOf[Put].getName)

    // 执行park任务写入hbase表
    // 内部执行的逻辑，每个分区创建TableOutputFormat对象，调用getRecordWriter 来获取对应写入对象，
    // 初始化 RecordWriter 对象时，会创建hbase连接， 一行一行写入数据
    pairrdd.saveAsNewAPIHadoopDataset(hbaseConf)

  }
}
