package for_if_tield

// ① if 条件判断测试
object If_Test {
  def main(args: Array[String]): Unit = {

    val x = 3

    val result: Boolean = if (x > 2) true else false
    val y = if(x > 1) 1 else "hello"

    println(result)
    println(y)

  }
}
