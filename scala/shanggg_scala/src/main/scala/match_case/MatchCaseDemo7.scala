package match_case

// match case 匹配列表
object MatchCaseDemo7 {
  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(10, 50, 60, 20)
    list match {
//      case List(_*) => println(list)
//      case List(50, b, c, d) => println(list)
//      case List(10,abc@_*) => println(list)
//      case a::b::c::d::Nil => println(d)
      case a::rest => println(rest)
    }
  }
}
