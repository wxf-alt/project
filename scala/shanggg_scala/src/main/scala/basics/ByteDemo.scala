package basics

/**
 * Scala各整数类型有固定的表数范围和字段长度
 */
object ByteDemo {
  def main(args: Array[String]): Unit = {
    // 正确
    var n1:Byte = 127
    var n2:Byte = -128

    // 错误
//     var n3:Byte = 128
//     var n4:Byte = -129
  }
}
