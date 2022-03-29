package branch_loop

// 三元运算符 案例
object IfElseDemo {
  def main(args: Array[String]): Unit = {

    var flag: Boolean = true

    // Java
    // int result = flag ? 1:0

    // Scala
    var result: Int = if (flag) 1 else 0
    println(result)
  }
}
