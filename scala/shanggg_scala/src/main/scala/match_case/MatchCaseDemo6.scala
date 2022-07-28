package match_case

// match case 匹配元组
object MatchCaseDemo6 {
  def main(args: Array[String]): Unit = {
    val tuple: (String, Int) = ("aa", 20)
    tuple match {
      case (name:String,age:Int) => println(name)
    }
  }
}
