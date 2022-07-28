package basics

object VarValDemo {
  /**
   * val 修饰的是常量,不可变
   * var 可变
   *
   * 在scala中尽量使用常量 -> 因为常量安全
   * 在声明常量和变量时,必须要赋值
   */
  def main(args: Array[String]): Unit = {
    var a: Int = 20
    val b:Int = 32
    a = 30
    // val 修饰的是常量,不可变
//    b = 30
    println(a)
    println(b)

    // 不定义类型,scala进行 类型推导
    var c = "as"

  }
}
