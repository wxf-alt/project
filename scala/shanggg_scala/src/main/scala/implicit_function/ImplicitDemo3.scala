package implicit_function

// 隐式参数和隐式值
object ImplicitDemo3 {
  def main(args: Array[String]): Unit = {
    // 隐式值
    implicit  val aa:Int = 100
    implicit  val bb:Double = 20.5
    foo(10)
    // 调用函数时,如果不传参数并且省略括号
    // 找隐式值 只看类型
    foo2(10)
  }

  // 隐式参数
  def foo(implicit a:Int): Unit ={
    println(a)
  }

  // 可以使用柯里化  解决 参数列表都是隐式值的问题
  def foo2(a:Int)(implicit b:Int,c:Double): Unit ={
    println(a + b + c)
  }
}
