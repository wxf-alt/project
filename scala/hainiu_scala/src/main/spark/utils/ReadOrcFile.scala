package utils

import java.util

import org.apache.hadoop.hive.ql.io.orc.{OrcNewInputFormat, OrcStruct}
import org.apache.hadoop.io.NullWritable
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object ReadOrcFile {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("ReadOrcFile")
    val sc: SparkContext = new SparkContext(conf)

    // orc 文件
    val orcPath: String = """E:\A_data\4.测试数据\输出\mapjoin\part-r-00000"""
    val orcInputRdd: RDD[(NullWritable, OrcStruct)] = sc.newAPIHadoopFile(orcPath, classOf[OrcNewInputFormat], classOf[NullWritable], classOf[OrcStruct])

    val mapRdd: RDD[String] = orcInputRdd.map(f => {
      // 工具类
      val orcUtil: OrcUtil = new OrcUtil
      // 设置读取 orc 格式
      orcUtil.setOrcTypeReadSchema(OrcFormat.OUT_SCHEMA)
      val code: String = orcUtil.getOrcData(f._2, "code")
      val name: String = orcUtil.getOrcData(f._2, "name")
      code + "\t" + name
    })
    val array: Array[String] = mapRdd.collect()
    for (elem <- array) {
      println(elem)
    }

  }
}
