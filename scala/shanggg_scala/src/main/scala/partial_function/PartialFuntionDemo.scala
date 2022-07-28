package partial_function

// 偏函数使用
object PartialFuntionDemo {
  def main(args: Array[String]): Unit = {
    val list: List[Any] = List(10, 20, 30, "aa", false)
    val f: PartialFunction[Any, Int] = new PartialFunction[Any, Int] {
      override def isDefinedAt(x: Any): Boolean = x.isInstanceOf[Int]

      override def apply(v1: Any): Int = v1.asInstanceOf[Int]
    }

    val list1: List[Int] = list.collect(f)
    println(list1)

  }
}
