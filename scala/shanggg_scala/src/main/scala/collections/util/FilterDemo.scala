package collections.util

// filter 操作
object FilterDemo {
  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(30, 5, 60, 7, 1, 20)
    println(list.filter(x => x % 2 == 0))
    println(list.filter(_ % 2 == 0))
  }
}
