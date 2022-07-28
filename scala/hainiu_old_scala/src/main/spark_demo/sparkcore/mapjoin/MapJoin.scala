package sparkcore.mapjoin

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hive.ql.io.orc.{OrcNewInputFormat, OrcNewOutputFormat, OrcStruct}
import org.apache.hadoop.io.{IntWritable, NullWritable, Writable}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}
import utils._

import scala.collection.mutable.ListBuffer
import scala.io.Source

// 伴生类
case class MapJoin()

object MapJoin {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("mapjoin")
    val sc = new SparkContext(conf)

    val path = "/tmp/spark/mapjoin_input"
    val orcPairRdd: RDD[(NullWritable, OrcStruct)] = sc.newAPIHadoopFile(path,classOf[OrcNewInputFormat],classOf[NullWritable],classOf[OrcStruct])

    // 定义累加器匹配的
    val hasCountryAcc: LongAccumulator = sc.longAccumulator

    // 定义累加器不匹配的
    val notHasCountryAcc: LongAccumulator = sc.longAccumulator

    // 读取字典文件到map
    val dictPath: String = "/tmp/spark/country_dict.dat"
    val lines: List[String] = Source.fromFile(dictPath).getLines().toList
    val countryMap: Map[String, String] = lines.map(_.split("\t")).map(f => (f(0),f(1))).toMap
    // 封装广播变量
    val broad: Broadcast[Map[String, String]] = sc.broadcast(countryMap)

    val orcWriteRdd: RDD[(NullWritable, Writable)] = orcPairRdd.mapPartitions(it => {
      val orcUtil = new OrcUtil
      orcUtil.setOrcTypeReadSchema(OrcFormat.SCHEMA)
      orcUtil.setOrcTypeWriteSchema("struct<code:string,name:string>")

      // 获取countryMap
      val countryMap: Map[String, String] = broad.value

      // f 代表：(NullWritable, OrcStruct)
      //(NullWritable, OrcStruct)  ---> (NullWritable, Writable)
      //      it.map(f => {
      //        val countryCode: String = orcUtil.getOrcData(f._2, "country")
      //        val option: Option[String] = countryMap.get(countryCode)
      //        var countryName: String = ""
      //        if (option == None) {
      //          notHasCountryAcc.add(1)
      //          countryName = "not match"
      //        } else {
      //          hasCountryAcc.add(1)
      //          countryName = option.get
      //        }
      //
      //        // 写orc
      //        orcUtil.addAttr(countryCode, countryName)
      //        val w: Writable = orcUtil.serialize()
      //        (NullWritable.get(), w)
      //      })
      val list = new ListBuffer[(NullWritable,Writable)]
      // 遍历每个分区的每个元素，将 (NullWritable, OrcStruct)  ---> (NullWritable, Writable)
      // 装到list里并返回
      it.foreach(f =>{
        val countryCode: String = orcUtil.getOrcData(f._2, "country")
        val option: Option[String] = countryMap.get(countryCode)
        var countryName: String = ""
        if (option == None) {
          notHasCountryAcc.add(1)

        } else {
          hasCountryAcc.add(1)
          countryName = option.get
          orcUtil.addAttr(countryCode, countryName)
          val w: Writable = orcUtil.serialize()
          list += ((NullWritable.get(), w))
        }
      })
      list.iterator
    })
    val outputPath:String = "/tmp/spark/mapjoin_output"
    import utils.MyPredef.string2HdfsDelete
    outputPath.deletePath

    orcWriteRdd.saveAsNewAPIHadoopFile(outputPath,classOf[NullWritable],classOf[Writable],classOf[OrcNewOutputFormat])

    println(s"not match:${notHasCountryAcc.value}")
    println(s"match:${hasCountryAcc.value}")

  }
}