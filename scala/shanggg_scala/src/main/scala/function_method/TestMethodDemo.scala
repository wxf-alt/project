package function_method

object TestMethodDemo {
  def main(args: Array[String]): Unit = {
    test("a","b","c")                   // WrappedArray(a, b, c)
    test2("hh","hi")           // hh,WrappedArray(hi)
    // 如果参数有默认值，在调用的时候，可以省略这个参数
    test3("hh")                // hh, 30
    // 如果参数传递了值，那么会覆盖默认值
    test3("hh",20)     // hh, 20
    // 带名参数 进行传递
    test4(name = "hh")                 // hh, 男
  }

  // (1)可变参数
  def test( s : String* ): Unit = {
    println(s)
  }

  // (2)可变参数一般放置在最后
  def test2( name : String,s: String* ): Unit = {
    println(name + "," + s)
  }

  // (3)参数默认值
  // scala函数中参数传递是，从左到右
  // 一般情况下，将有默认值的参数放置在参数列表的后面
  def test3( name : String, age : Int = 30 ): Unit = {
    println(s"$name, $age")
  }

  def test4( sex : String = "男", name : String ): Unit = {
    println(s"$name, $sex")
  }

}
