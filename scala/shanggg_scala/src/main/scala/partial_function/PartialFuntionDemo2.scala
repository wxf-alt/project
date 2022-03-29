package partial_function

// 模式匹配 偏函数
object PartialFuntionDemo2 {
  def main(args: Array[String]): Unit = {
    val list: List[Any] = List(10, 20, 30, "aa", false)

    val list1: List[Int] = list.collect({
      case a: Int => a
    })

    println(list1)

  }
}
