package spark_core.hbase

import java.util

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization

import scala.collection.mutable
import scala.collection.mutable.Map

// 读取 HBase 数据
object HBaseDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("HBaseDemo").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(conf)

    val hbaseConf: Configuration = HBaseConfiguration.create()
    hbaseConf.set("hbase.zookeeper.quorum", "nn1.hadoop,nn2.hadoop,s1.hadoop")
    hbaseConf.set(TableInputFormat.INPUT_TABLE, "student")
    val hbaseRdd: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hbaseConf,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result])
    val rdd2 = hbaseRdd.map {
      case (rw, result) => {
        val map: mutable.Map[String, Any] = mutable.Map[String,Any]()
        map += "rowkey" -> Bytes.toString(rw.get())
        val cells: util.List[Cell] = result.listCells()
        import scala.collection.JavaConversions._
        for(cell <- cells){
          val quali: String = Bytes.toString(CellUtil.cloneQualifier(cell))
          val value: String = Bytes.toString(CellUtil.cloneValue(cell))
          map += quali -> value
        }
//        map
        // 把 map 拼装成 Json
        implicit val mapToJson: DefaultFormats.type = org.json4s.DefaultFormats
        Serialization.write(map)
      }
    }

    rdd2.collect.foreach(println)
    sc.stop()
  }
}
