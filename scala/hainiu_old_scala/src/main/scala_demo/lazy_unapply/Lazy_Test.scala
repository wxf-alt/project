package lazy_unapply

// 惰性变量用法放在不可变变量之前
object Lazy_Test {

  def init():Unit = {
    println("init")
  }

  def main(args: Array[String]): Unit = {
    val p = init()       //没有lzay关键字的时候
    println("init after")
    println(p)
    println(p)
//    init
//    init after
//      ()
//    ()
    println("======================================")
    //有lzay关键字的时候，在调用的时候才去初化它
    lazy val p1 = init()
    println("init after")
    println(p1)
    println(p1)
//    init after
//      init
//    ()
//    ()
  }
}
