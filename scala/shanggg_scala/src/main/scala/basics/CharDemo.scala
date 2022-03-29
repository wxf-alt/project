package basics

object CharDemo {
  def main(args: Array[String]): Unit = {
//    //（1）字符常量是用单引号 ' ' 括起来的单个字符。
//    var c1: Char = 'a'
//
//    println("c1=" + c1)
//
//    //（2）可以直接给Char赋一个整数，然后输出时，会按照对应的unicode字符输出
//    println("c1码值=" + c1.toInt)

    var c2: Char = 98 // 正确，因为直接将一个数值给char，编译器只判断是否越界
//    var c3: Char = 'a' + 1 // 错误，Int高->char低，编译器判断类型

    var c4: Char = ('a' + 1).toChar

  }
}
