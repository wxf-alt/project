package genericity

// 泛型类 和 泛型函数
object GenericDemo {
  def main(args: Array[String]): Unit = {
    val p1: Point[Int] = Point[Int](11, 12)
    val p2: Point[Double] = Point[Double](11.5, 12.7)

    val d: Double = p1.dis[Double](10)
  }
}

// 泛型类
case class Point[T](x: T, y: T){
  var z:T = x
  def foo():T = x

  // 泛型函数
  def dis[A](a:A): A = a
}
