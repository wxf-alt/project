package operation

// 关系运算符 案例
object RelationalDemo {
  def main(args: Array[String]): Unit = {
    // 测试：>、>=、<=、<、==、!=
    var a: Int = 2
    var b: Int = 1

    println(a > b)      // true
    println(a >= b)     // true
    println(a <= b)     // false
    println(a < b)      // false
    println("a==b" + (a == b))    // false
    println(a != b)     // true
  }
}
