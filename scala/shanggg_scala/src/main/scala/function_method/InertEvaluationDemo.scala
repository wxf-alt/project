package function_method

object InertEvaluationDemo {

  def main(args: Array[String]): Unit = {
    lazy val res: Int = sum(10, 30)
    println("----------------")
    println("res=" + res)
  }

  def sum(n1: Int, n2: Int): Int = {
    println("sum被执行。。。")
    return n1 + n2
  }

}
