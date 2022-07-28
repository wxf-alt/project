package spark_core.map_join

import java.net.URL

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.hive.ql.io.orc.{OrcInputFormat, OrcNewInputFormat, OrcNewOutputFormat, OrcStruct}
import org.apache.hadoop.io.{NullWritable, Writable}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}
import utils.{OrcFormat, OrcUtil}

import scala.io.Source

//noinspection SourceNotClosed
// 用spark读取orc文件，并与字典文件join，输出10条的join结果
object MapJoinDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("MapJoinDemo")
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

    // 解析 orcRdd
    val orcWritableRdd: RDD[(NullWritable, Writable)] = orcInputRdd.mapPartitions(it => {
      // 工具类
      val orcUtil: OrcUtil = new OrcUtil
      // 设置读取 orc 格式
      orcUtil.setOrcTypeReadSchema(OrcFormat.SCHEMA)
      // 设置 输出 orc 格式
      orcUtil.setOrcTypeWriteSchema(OrcFormat.OUT_SCHEMA)
      // 解析 广播变量
      val countryMap: Map[String, String] = broadCastCountryMap.value

      // 获取 Iterator 中的每一个数据
      it.map(f => {
        // 获取字段数据
        val country: String = orcUtil.getOrcData(f._2, OrcFormat.COUNTRY)
        val option: Option[String] = countryMap.get(country)
        var countryName: String = null
        if (option.isEmpty) {
          notHasCountryAcc.add(1)
          countryName = "not match"
        } else {
          hasCountryAcc.add(1)
          countryName = option.get
        }

        // 封装输出数据
        orcUtil.addAttr(country, countryName)
        val writable: Writable = orcUtil.serialize()
        (NullWritable.get(), writable)
      })
    })

    // 设置输出目录
    val outPath: String = """E:\A_data\4.测试数据\输出\mapjoin"""
    outPath.deletePath()
    // 引入隐式转换
//    import utils.MyPredef.string2HdfsDelete
//    outPath.deletePath()

    // 输出 orc文件
    orcWritableRdd.saveAsNewAPIHadoopFile(outPath,classOf[NullWritable],classOf[Writable],classOf[OrcNewOutputFormat])
  }

  // 隐式类  用于删除输出文件
  implicit class HdfsDelete(val path:String) {
    def deletePath():Unit = {
      val conf: Configuration = new Configuration
      val fs: FileSystem = FileSystem.get(conf)
      val outputPath: Path = new Path(path)
      if(fs.exists(outputPath)){
        fs.delete(outputPath,true)
        println(s"delete outputPath:${path} success")
      }
    }
  }

}
