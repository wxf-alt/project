package function_method

// 名传递与值传递
object NameValueDemo {
  def main(args: Array[String]): Unit = {
//    val f = () => {
//      println("f .......")
//      10
//    }
//    foo(f())
//    println("---------------")
//    foo2(f())

    foo2{
      println("abc")
      println("111")
      20
    }

  }

  // 值传递
  def foo(a:Int): Unit ={
    println(a)
    println(a)
    println(a)
  }

  // 名传递
  // 传递的是代码  -> 用一次执行一次;不用就不会执行
  def foo2(a: => Int): Unit ={
    println(a)
    println(a)
    println(a)
  }

}
