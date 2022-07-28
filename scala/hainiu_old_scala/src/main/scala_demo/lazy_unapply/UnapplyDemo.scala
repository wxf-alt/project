package lazy_unapply

class UnapplyDemo(val name:String) {

}

object UnapplyDemo{

  // unapply是解构方法，也是隐式调用
  // unapply接收对象，返回对象的数据，一般是用在模式匹配，可以通过模式匹配来匹配出来对象的数据
  // 返回的Option[String] 类型，跟模式匹配的是一致的
  def unapply(arg: UnapplyDemo): Option[String] = {
    println("do unapply, return Option[String]")
    Some(arg.name)
  }

  // unapply 不能重载，因为模式匹配的时候，是按照 Option 里面的类型进行模式匹配
  //  def unapply(arg:UnapplyDemo):Option[(String,Int)] ={
  //    println("do unapply, return Option[(String,Int)]")
  //    Some(arg.name, 10)
  //  }


  def main(args: Array[String]): Unit = {
    val demo = new UnapplyDemo("hainiu")
//    demo match {
    demo match {
      // 匹配对象类型
      case x:UnapplyDemo => println(s"case1 => name=${x.name}")
      // 匹配元素
      case UnapplyDemo(x) => println(s"case2 => name= ${x}")
      // 匹配元素的具体数据
      case UnapplyDemo("hainiu") => println("case3 => name=hainiu")
    }
  }
}
