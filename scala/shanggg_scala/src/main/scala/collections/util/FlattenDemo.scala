package collections.util

// flatten 操作
// flatten 的 集合里的元素 也必须是集合
object FlattenDemo {
  def main(args: Array[String]): Unit = {
    val list: List[List[Int]] = List(List(1, 2, 3), List(8, 1, 6), List(9, 7))
    val flattenList: List[Int] = list.flatten
    println(flattenList)

    val list1: List[String] = List("hello as", "hello hello", "afds aa bbb")
    val flattenList1: List[String] = list1.map(_.split(" ")).flatten
    println(flattenList1)

    val flaMapList: List[String] = list1.flatMap(_.split(" "))
    println(flaMapList)
  }
}
