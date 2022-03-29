package function_method

// 高阶函数：参数为函数的函数称
object HigherOrderFunction {
  def main(args: Array[String]): Unit = {
    // 函数作为参数 调用
    println(calculator(1, 2, plus))
    println(calculator(1, 2, multiply))
  }

  // 高阶函数————函数作为参数
  def calculator(a: Int, b: Int, operater: (Int, Int) => Int): Int = {
    operater(a, b)
  }

  // 函数————求和
  def plus(x: Int, y: Int): Int = {
    x + y
  }

  // 方法————求积
  def multiply(x: Int, y: Int): Int = {
    x * y
  }

}
