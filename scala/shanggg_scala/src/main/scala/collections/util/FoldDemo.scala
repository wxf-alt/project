package collections.util

// fold 折叠
object FoldDemo {
  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(30, 50, 60, 70, 90, 20)
    //    val foldList: Int = list.fold(0)((x, y) => x + y)
    val foldList: Int = list.fold(0)(_ + _)
    println(foldList)

    println(list.foldLeft("")((x, y) => x + y))
  }
}
