package function_method

// 闭包
object ClosePacketDemo {

  var x: Int = 10

  def f1(y: Int): Int = {
    // 闭包 -> 在方法内 外部变量
    x + y
  }

  def main(args: Array[String]): Unit = {
    println(f1(20))
  }

}
