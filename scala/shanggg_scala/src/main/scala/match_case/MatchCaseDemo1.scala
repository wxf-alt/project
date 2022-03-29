package match_case

import scala.io.StdIn

// match case 简单案例
object MatchCaseDemo1 {
  def main(args: Array[String]): Unit = {
    val a: Int = 10
    val b: Int = 20
    val input: String = StdIn.readLine("输入一个运算符：")
    val result: Any = input match {
      case "+" => a + b
      case "-" => a - b
      case "*" => a * b
      case "/" => a / b
      case _ => "输入错误"
    }
    println(s"结果为：${result}")
  }
}
