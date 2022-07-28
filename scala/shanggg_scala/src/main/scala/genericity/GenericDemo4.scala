package genericity

// 视图绑定
object GenericDemo4 {
  def main(args: Array[String]): Unit = {
    val value: Int = max(10, 20)
    println(value)
  }

  def max[T](x: T, y: T)(implicit ev$1: T => Ordered[T]): T = {
    if (x > y) x else y
  }
}
