package function_method

// 柯里化
object CurryingDemo {

  def main(args: Array[String]): Unit = {
    println(sum(1, 2, 3))
    println(sum1(1)(2)(3))
    println(sum2(1)(2)(3))
    println(sum3(1)(2)(3))
  }

  val sum = (x: Int, y: Int, z: Int) => x + y + z

  val sum1 = (x: Int) => {
    y: Int => {
      z: Int => {
        x + y + z
      }
    }
  }

  val sum2 = (x: Int) => (y: Int) => (z: Int) => x + y + z

  def sum3(x: Int)(y: Int)(z: Int): Int = x + y + z

}
