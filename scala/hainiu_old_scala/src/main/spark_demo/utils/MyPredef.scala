package utils

// 定义隐式转换函数，将string赋予 删除hdfs目录的功能
object MyPredef {
  implicit def string2HdfsDelete(path: String) = new HdfsDelete(path)
}
