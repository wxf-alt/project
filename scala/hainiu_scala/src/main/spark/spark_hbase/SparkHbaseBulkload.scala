package spark_hbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, HTable, Table}
import org.apache.hadoop.hbase.{HBaseConfiguration, KeyValue, TableName}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{HFileOutputFormat, HFileOutputFormat2, LoadIncrementalHFiles}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.serializer.KryoSerializer

object SparkHbaseBulkload {
  def main(args: Array[String]): Unit = {
    // 设置 hadoop 的代理
    System.setProperty("HADOOP_USER_NAME","hadoop")

    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("SparkHbaseBulkload")
    // 配置Kryo序列化
    conf.set("spark.serializer", classOf[KryoSerializer].getName)
    val sc: SparkContext = new SparkContext(conf)

    val outputPath: String = "/tmp/spark/hbase_bulk_output"
    val tableName: String = "idea_spark:spark_user"
//        val outputPath = args(0)
//        val tableName: String = args(1)
    import utils.MyPredef.string2HdfsDelete
    outputPath.deletePath()

    val rdd: RDD[Int] = sc.parallelize(10 to 20, 2)
    val pairRdd: RDD[(ImmutableBytesWritable, KeyValue)] = rdd.map(f => {
      // 封装 rowKey
      val rowKey: ImmutableBytesWritable = new ImmutableBytesWritable(Bytes.toBytes(s"spark_load_${f}"))
      // 封装 value
      val keyValue: KeyValue = new KeyValue(rowKey.get(), Bytes.toBytes("cf1"), Bytes.toBytes("count"), Bytes.toBytes(s"${f}"))
      (rowKey, keyValue)
    })
    // 排序
    val sortRdd: RDD[(ImmutableBytesWritable, KeyValue)] = pairRdd.sortByKey()

    // 配置相关参数
    val hbaseConf: Configuration = HBaseConfiguration.create()
    val job: Job = Job.getInstance(hbaseConf)
    val conn: Connection = ConnectionFactory.createConnection(hbaseConf)
    val hTable: HTable = conn.getTable(TableName.valueOf(tableName)).asInstanceOf[HTable]
    HFileOutputFormat2.configureIncrementalLoad(job,hTable.getTableDescriptor,hTable.getRegionLocator)

    // 写入 HFile 文件
    sortRdd.saveAsNewAPIHadoopFile(outputPath,classOf[ImmutableBytesWritable],classOf[KeyValue],
      classOf[HFileOutputFormat2],job.getConfiguration)

    // 集成hbase导入到hbase表
    // 注意：导入代码需要在集群上执行
    //		hadoop jar /usr/local/hbase/lib/hbase-shell-1.3.1.jar completebulkload
    //		/user/hadoop/hbase/output/orc2hfile_0725 user_install_status
    val inputParams: Array[String] = Array(outputPath, tableName)
    // 将ArrayBuffer 转成 ArrayList
//    import scala.collection.JavaConversions._
    LoadIncrementalHFiles.main(inputParams)

//    val arr: ArrayBuffer[String] = ArrayBuffer[String](outputPath, tableName)
//    // 将ArrayBuffer 转成 ArrayList
//    import scala.collection.JavaConversions.bufferAsJavaList
//    // java 列表转数组
//    LoadIncrementalHFiles.main(arr.toArray[String]);
  }
}