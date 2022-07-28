package collections.util

// 测试 sortWith
object SortWithDemo {
  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(30, 5, 60, 7, 1, 20)
    // 降序
    println(list.sortWith((a, b) => a > b)) // List(60, 30, 20, 7, 5, 1)
    println(list.sortWith(_ > _))
    // 升序
    println(list.sortWith((a, b) => a < b)) // List(1, 5, 7, 20, 30, 60)
    println(list.sortWith(_ < _))
  }
}
