package operation

// 位运算符 案例
object BitwiseDemo {
  def main(args: Array[String]): Unit = {
    // 测试：1000 << 1 =>10000
    var n1 :Int =8

    n1 = n1 << 1
    println(n1)
  }
}
