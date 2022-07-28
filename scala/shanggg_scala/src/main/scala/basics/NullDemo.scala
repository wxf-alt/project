package basics

object NullDemo {
  def main(args: Array[String]): Unit = {
    //null可以赋值给任意引用类型（AnyRef），但是不能赋值给值类型（AnyVal）
//    var n1: Int = null // 错误
//    println("n1:" + n1)

    var cat: Cat = new Cat();
    cat = null	// 正确
  }
}

case class Cat()
