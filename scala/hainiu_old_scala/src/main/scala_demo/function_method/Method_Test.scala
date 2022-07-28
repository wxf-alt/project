package function_method

// ⑤ 方法声明与使用  与 函数的区别
// 方法声明与使用  def 方法名([变量：变量类型，变量：变量类型]):返回值类型={方法体}
object Method_Test {

  def main(args: Array[String]): Unit = {
    say("asd")
    println()
    println(add1(1, 2))
    println(add2(2, 3))
  }

  // 不带有返回值的方法
  def say(str:String):Unit = {print(str)}
  // 带有返回值的方法
  def add1(a:Int, b:Int):Int={a+b}
  // 等号后面的{}可以省略
  def add2(a:Int, b:Int) : Int = a + b
  // 等号左面的返回类型可以省略
  def add3(a:Int, b:Int) = a + b



}
