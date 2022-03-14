package function_method

// ⑥  在scala中，函数是一等公民。可以在任何地方定义，在函数内或函数外，可以作为函数的参数和返回值；函数还可以赋给变量
// 一 函数声明：
/*
  ------------定义函数
   val 变量名:[变量类型1,变量类型2 => 函数体返回类型 ] = ([变量：变量类型，变量：变量类型]) => 函数体
   val add1=(a:Int,b:Int) => a + b
  ------------定义方法
   def 方法名([变量：变量类型，变量：变量类型]):返回值类型={方法体}
   def add(a:Int,b:Int) = a + b
*/
// 二 方法转换成函数
//  1）用空格下划线的方式
//  2）也可以把方法当参数使用，这也因为scala会隐式的把方法转换成函数，但并不是直接支持方法当参数的模式，只是做了隐式的转换，这种函数的转换分两种显示用<空格>_和隐式的，这也体现了scala灵活的地方。
object Function_Test {

  def main(args: Array[String]): Unit = {
    val perimeter: (Int, Int) => Int = (a:Int, b:Int) => (a+b) *2
    val area: (Int, Int) => Int = (a:Int, b:Int) => a*b
    //  函数可以做为方法的参数使用，这也是scala比较灵活的地方;
    println(add(perimeter(1, 2), area(3, 4)))

    //  方法转换成函数
    //  定义方法
    def add_1(a:Int,b:Int): Int = a + b
    //  方法转函数，用空格下划线的方式
    val add_1_func: (Int, Int) => Int = add_1 _
  }
  def add(a:Int,b:Int): Int ={
    a + b
  }

}
