package case_class

// 变量声明中的模式匹配
object Pattern {
  def main(args: Array[String]): Unit = {
    val (a, b): (Int, String) = foo()
    println(a)
    println(b)

    println("===========================")
    val list: List[(Int, Int)] = List((1, 2), (10, 20), (100, 200))
    for ((k,v) <- list) {
      println(k + "->" + v)
    }
  }

  def foo(): (Int, String) = (10, "lisi")
}
