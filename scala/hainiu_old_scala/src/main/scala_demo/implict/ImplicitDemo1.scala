package implict

object ImplicitDemo1 {

//   // 定义隐式转换方法 double --> string
//    implicit def double2stringm(a:Double) = {
//      println("do double2stringm")
//      a.toString
//    }

  // 定义隐式转换函数
  implicit val double2stringf = (a:Double) => {
    println("do double2stringf")
    a.toString
  }

  def main(args: Array[String]): Unit = {
    // 调用隐式转换函数将3.14转成字符串3.14
    // 当当前环境有一个隐式方法，一个隐式函数，那会优先使用隐式函数，这也体现了函数是一等公民
//    val a:Double = 3.14
    val a:String = 3.14
    println(a.length)
  }

}

