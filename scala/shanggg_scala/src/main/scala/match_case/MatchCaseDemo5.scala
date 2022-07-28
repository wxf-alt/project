package match_case

// match case 匹配数组
/**
 * 模式匹配深入到集合的内部 -> 集合必须是有序的
 */
object MatchCaseDemo5 {
  def main(args: Array[String]): Unit = {
    val array: Array[Any] = Array(10, 20, "aa", 3.5)

    array match {
      // 根据元素值和数组长度进行匹配
      case Array(10, 20, a, b) => println(array.toBuffer)
      //      case Array(10, 20,_*) => println(array.toBuffer)
      //      case Array(10, 20,arr@_*) => println(arr)
    }
  }
}
