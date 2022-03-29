package function_method

object MethodAndFunctionDemo {

  // 定义方法  参数是一个函数
  def m1(f: (Int, Int) => Int): Int = {
    f(2, 6)
  }

  // 定义方法
  def m2(a: Int, b: Int): Int = {
    a + b
  }

  //定义一个函数f1,参数是两个Int类型，返回值是一个Int类型
  val f1: (Int, Int) => Int = (x: Int, y: Int) => x + y
  //再定义一个函数f2
  val f2 = (m: Int, n: Int) => m * n

  //main方法
  def main(args: Array[String]): Unit = {
    //调用m1方法，并传入f1函数
    val r1: Int = m1(f1)
    println(r1)

    //调用m1方法，并传入f2函数
    val r2: Int = m1(f2)
    println(r2)
  }
}
