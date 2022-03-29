package genericity

// 泛型的上限和下限
object GenericDemo2 {
  def main(args: Array[String]): Unit = {
    val s: String = compare("a", "b")
    println(s)
  }

  // 设置 泛型的上限
  def compare[T <: String](x: T, y: T): T = {
    if(x > y) x else y
  }

}
