package utils

object MyPredef {

  // 定义隐式转换函数，将string赋予 删除hdfs目录的功能
  implicit def string2HdfsDelete(path:String): HdfsDelete = new HdfsDelete(path)
}
