package collections.util

// foreach ---> 一进不出
object ForeachDemo {
  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(30, 50, 60, 70, 90, 20)
//    list.foreach(s => println(s))

    // 使用的是部分应用函数
    // _ 占位符
    list.foreach(println(_))
    list.foreach(println)

//    val function: Any => Unit = println(_)
//    list.foreach(function)


    println("-----------------------部分应用函数-------------")
    val square: Double => Double = math.pow(_, 2)
    println(square(20))

  }
}
