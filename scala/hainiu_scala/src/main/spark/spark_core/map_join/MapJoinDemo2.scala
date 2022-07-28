package spark_core.map_join

import java.net.URL

import org.apache.hadoop.hive.ql.io.orc.{OrcNewInputFormat, OrcNewOutputFormat, OrcStruct}
import org.apache.hadoop.io.{NullWritable, Writable}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import utils.{OrcFormat, OrcUtil}

import scala.collection.mutable.ListBuffer
import scala.io.Source

class MapJoinDemo2

// 用spark读取orc文件，并与字典文件join，输出结果成orc文件
object MapJoinDemo2 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("MapJoinDemo2")
    val sc: SparkContext = new SparkContext(conf)

    // orc 文件
    val orcPath: String = """E:\A_data\4.测试数据\输入\mapjoin"""
    val orcInputRdd: RDD[(NullWritable, OrcStruct)] = sc.newAPIHadoopFile(orcPath, classOf[OrcNewInputFormat], classOf[NullWritable], classOf[OrcStruct])

    // 加载resource下的 字典文件
    val url: URL = ClassLoader.getSystemResource("country_dict.dat")
    val path: String = url.getPath
    // 读取 字典文件
    val list: List[String] = Source.fromFile(path).getLines().toList
    // 将字典文件 转成 需要的 map
    val map: Map[String, String] = list.map(_.split("\t"))
      .map(f => (f(0), f(1)))
      .toMap

    // 封装广播过变量 和 累加器
    val broadCastCountryMap: Broadcast[Map[String, String]] = sc.broadcast(map)
    val hasCountryAcc: LongAccumulator = sc.longAccumulator
    val notHasCountryAcc: LongAccumulator = sc.longAccumulator

    // 处理 orcInputRdd
    val orcWritableRdd: RDD[(NullWritable, Writable)] = orcInputRdd.mapPartitions(it => {
      // orc 工具类
      val orcUtil: OrcUtil = new OrcUtil
      // 设置读取数据格式
      orcUtil.setOrcTypeReadSchema(OrcFormat.SCHEMA)
      // 设置写出数据格式
      orcUtil.setOrcTypeWriteSchema(OrcFormat.OUT_SCHEMA)
      // 获取 countryMap
      val countryMap: Map[String, String] = broadCastCountryMap.value

      // 创建 输出的缓冲区
      val list: ListBuffer[(NullWritable, Writable)] = new ListBuffer[(NullWritable, Writable)]()

      // 不需要返回值  所以不使用 map
      it.foreach(x => {
        // 获取数据
        val countryCode: String = orcUtil.getOrcData(x._2, OrcFormat.COUNTRY)
        val option: Option[String] = countryMap.get(countryCode)
        var countryName: String = null
        if (option.isEmpty) {
          notHasCountryAcc.add(1)
        } else {
          hasCountryAcc.add(1)
          countryName = option.get
          // 添加输出
          orcUtil.addAttr(countryCode, countryName)
          val writable: Writable = orcUtil.serialize()
          list += ((NullWritable.get(), writable))
        }
      })
      list.iterator
    })

    // 设置输出目录
    val outPath: String = """E:\A_data\4.测试数据\输出\mapjoin"""
    //     引入隐式转换
    import utils.MyPredef.string2HdfsDelete
    outPath.deletePath()
    orcWritableRdd.saveAsNewAPIHadoopFile(outPath,classOf[NullWritable],classOf[Writable],classOf[OrcNewOutputFormat])

  }
}
