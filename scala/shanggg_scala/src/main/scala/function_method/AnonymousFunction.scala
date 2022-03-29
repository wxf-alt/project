package function_method

// 匿名函数
object AnonymousFunction {
  def main(args: Array[String]): Unit = {
    // 匿名函数 使用
    println(calculator(2, 3, (x, y) => x + y))
    println(calculator(2, 3, _ * _))
  }

  //高阶函数————函数作为参数
  def calculator(a: Int, b: Int, operator: (Int, Int) => Int): Int = {
    operator(a, b)
  }

}
