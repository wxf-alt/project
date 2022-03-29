package operation

//逻辑运算符 案例
object LogicalDemo {
  def main(args: Array[String]): Unit = {
    // 测试：&&、||、!
    var a: Boolean = true
    var b: Boolean = false

    println("a&&b=" + (a && b)) // a&&b=false
    println("a||b=" + (a || b)) // a||b=true
    println("!(a&&b)=" + (!(a && b))) // !(a&&b)=true
  }
}
