package match_case

// match case 匹配泛型
object MatchCaseDemo4 {
  def main(args: Array[String]): Unit = {
    /**
     * scala中 除了数组，其他的所有泛型都是真正的泛型，
     * 泛型模式匹配是无法匹配除了泛型的类型的
     */
    val array: Any = Array[Double](1, 5, 6)

    // 匹配泛型
    array match {
      case a: Array[_] => println(a.toBuffer) // ArrayBuffer(1.0, 5.0, 6.0)
    }
  }
}
