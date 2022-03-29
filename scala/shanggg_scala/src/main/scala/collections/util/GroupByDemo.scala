package collections.util

object GroupByDemo {
  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(30, 5, 60, 7, 9, 20)
    val groupByList: Map[Boolean, List[Int]] = list.groupBy(s => s % 2 == 0)
    println(groupByList)

    val list1: List[String] = List("asa", "ased", "dad", "asa", "ased", "red", "reqd")
    val mapList1: Map[String, List[String]] = list1.groupBy(x => x)
    println(mapList1)
    val result: Map[String, Int] = mapList1.map(x => {
      (x._1, x._2.length)
    })
    println(result)
  }
}
