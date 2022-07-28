package sparkcore.spark_hbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, HTable}
import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration, KeyValue, TableName}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{HFileOutputFormat2, LoadIncrementalHFiles}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.serializer.KryoSerializer

// 7.参考着mapreduce 生成hfile文件，导入到hbase表的逻辑。
object SparkHbaseBulkload {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
      //      .setMaster("local[*]")
      .setAppName("SparkHbaseBulkload")

    // 配置Kryo序列化
    conf.set("spark.serializer",classOf[KryoSerializer].getName)

    //    val tableName:String = "panniu:spark_user"
    val tableName:String = args(1)
    //    val outputPath = "/tmp/spark/hbase_bulk_output"
    val outputPath = args(0)
    import utils.MyPredef.string2HdfsDelete
    outputPath.deletePath

    val sc = new SparkContext(conf)

    val rdd: RDD[Int] = sc.parallelize(10 to 20, 2)
    // RDD[Int] ---> RDD[ImmutableBytesWritable,KeyValue]
    val pairRdd: RDD[(ImmutableBytesWritable, KeyValue)] = rdd.map(f => {
      // rowkey
      val rowkey = new ImmutableBytesWritable(Bytes.toBytes(s"spark_load_${f}"))

      val keyValue = new KeyValue(rowkey.get(), Bytes.toBytes("cf"), Bytes.toBytes("count"), Bytes.toBytes(s"${f}"))
      (rowkey, keyValue)
    })

    // 再排序
    val sortRdd: RDD[(ImmutableBytesWritable, KeyValue)] = pairRdd.sortByKey()



    val hbaseConf: Configuration = HBaseConfiguration.create()
    val job: Job = Job.getInstance(hbaseConf)
    val conn: Connection = ConnectionFactory.createConnection(hbaseConf)
    val table: HTable = conn.getTable(TableName.valueOf(tableName)).asInstanceOf[HTable]

    // 配置相关的参数，配置完后，job对象里就有那些配置了
    HFileOutputFormat2.configureIncrementalLoad(job,table.getTableDescriptor,table.getRegionLocator)

    // 写入hfile文件
    sortRdd.saveAsNewAPIHadoopFile(outputPath,classOf[ImmutableBytesWritable],classOf[KeyValue],
      classOf[HFileOutputFormat2],job.getConfiguration)

    // 集成hbase导入到hbase表
    // 注意：导入代码需要在集群上执行
    //		hadoop jar /usr/local/hbase/lib/hbase-shell-1.3.1.jar completebulkload
    //		/user/hadoop/hbase/output/orc2hfile_0725 user_install_status
    val inputParams: Array[String] = Array[String](outputPath, tableName)
    LoadIncrementalHFiles.main(inputParams)

  }
}

class SparkHbaseBulkload

