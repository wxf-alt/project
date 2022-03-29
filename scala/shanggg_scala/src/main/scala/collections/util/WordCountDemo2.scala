package collections.util

object WordCountDemo2 {
  def main(args: Array[String]): Unit = {
    val list: List[String] = List("a b c d", "a d e s", "a b d e", "a a a b")
    val flatMapList: List[String] = list.flatMap(x => x.split(" "))
    println(flatMapList)
    val mapList: List[(String, Int)] = flatMapList.map((_, 1))
    println(mapList)
    val groupByList: Map[String, List[(String, Int)]] = mapList.groupBy(_._1)
    println(groupByList)
    val result: List[(String, Int)] = groupByList.map(x => {
      (x._1, x._2.size)
    }).toList
    val wordCount: List[(String, Int)] = groupByList.mapValues(_.length).toList.sortBy(_._2).reverse
    println(result)
    println(wordCount)
  }
}
