package collections.util

// map -> 转换/映射
// map ---> 一进一出
object MapDemo {
  def main(args: Array[String]): Unit = {
//    val x:Int => Int = (a:Int) => a * 2
    val list: List[Int] = List(30, 50, 60, 70, 90, 20)
//    println(list.map(x))
    println(list.map(x => x * x))
    println(list.map(x => x + 2))
    println(list.map(_ * 2))
  }
}
