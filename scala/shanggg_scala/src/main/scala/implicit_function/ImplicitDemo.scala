package implicit_function

//noinspection LanguageFeature
// 隐式函数
// double 转为 int
object ImplicitDemo {
  def main(args: Array[String]): Unit = {

    implicit def doubleToInt(d:Double): Int = d.toInt

    val a:Int = 10.2
    println(a)
  }
}
