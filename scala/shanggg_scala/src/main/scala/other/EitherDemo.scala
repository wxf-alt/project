package other

// Either 类的使用
object EitherDemo {
  def main(args: Array[String]): Unit = {
    val result: Either[String, Double] = foo(10)
    result match {
      case Right(r) => println(r)
      case Left(l) => println(l)
    }
  }

  def foo(d:Double): Either[String, Double] = {
    d match {
      case d if d >= 0 => Right[String,Double](math.sqrt(d))
      case _ => Left[String,Double]("负数没有平方根")
    }
  }
}
