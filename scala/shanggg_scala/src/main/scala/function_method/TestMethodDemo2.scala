package function_method

object TestMethodDemo2 {
  def main(args: Array[String]): Unit = {
    println(f4("方法1 -> "))

    // 若定时方法时未省略，则调用时，可省可不省
    println(f5())
    println(f5)
    // 若定义方法时省略小括号，则调用该方法时，也需省略小括号
    println(f6)

    // 至简原则：如果不关心名称，只关系逻辑处理，那么方法名（def）可以省略
    // 匿名方法
    ()->{println("xxxxx")}
  }

  // 方法的标准写法
  def f1(s: String): String = {
    return s"${s}f1"
  }

  // 至简原则：return 可以省略
  def f2(s: String): String = {
    s"${s}f2"
  }

  // 至简原则：返回值类型如果能够推断出来，那么可以省略
  def f3(s: String) = {
    s"${s}f3"
  }

  /*
    // 如果方法使用 return 那么必须加上返回值类型,不能使用类型推导
    def f3 (s:String) = {
    return s"${s}f1"
  }
   */

  // 至简原则：如果方法体只有一行代码，可以省略花括号
  def f4(s: String): String = s"${s}f4"

  // 至简原则：如果方法无参，则可以省略小括号
  def f5(): String = s"f5"
  def f6: String = "f6"

}
