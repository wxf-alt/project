package match_case

// match case 匹配常量
object MatchCaseDemo2 {
  def main(args: Array[String]): Unit = {
    val a: Int = 30
    a match {
      case 20 => println(20)
      // aa 只能在这个 case 中使用
      case aa => println(aa)
      // 如果 case 后面是大写字母开头,scala任务这个变量是一个常量,必须是已经定义好的常量
//      case Ba => println(Ba)
    }
  }
}
