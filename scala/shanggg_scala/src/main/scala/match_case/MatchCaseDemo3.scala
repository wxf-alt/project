package match_case

// match case 匹配类型
object MatchCaseDemo3 {
  def main(args: Array[String]): Unit = {
    val a: Any = 10

    val result: Any = a match {
      // 设置 匹配守卫 if
      case a: Int if a > 10 => a * 10
      case s: String => s.toUpperCase
      case x: Boolean => x
      case _ => 0
    }
    println(result)

  }
}
