package collections

//noinspection TypeAnnotation
object TupleDemo {
  def main(args: Array[String]): Unit = {

    // 创建 Tuple
    val tuple1: (Int, Boolean) = (1, true)
    val tuple2: (Int, Boolean) = 1 -> true

    //    val result: (Int, Int) = /%(20, 6)
    //    println(result._1)
    //    println(result._2)

    // 利用 隐式类 使Int具有 /% 方法
    val result: (Int, Int) = 5 /% 3
    println(result._1)
    println(result._2)
  }

  //  def /%(a: Int, b: Int): (Int, Int) = {
  //    (a / b, a % b)
  //  }

  implicit class RichInt(a: Int) {
    def /%(b: Int): (Int, Int) = (a / b, a % b)
  }

}
