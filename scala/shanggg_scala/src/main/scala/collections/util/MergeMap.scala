package collections.util

// 合并两个 map
object MergeMap {
  def main(args: Array[String]): Unit = {
    val map1: Map[String, Int] = Map("a" -> 2, "b" -> 5, "d" -> 8)
    val map2: Map[String, Int] = Map("a" -> 10, "c" -> 6, "d" -> 7)

    val foldLeftMap: Map[String, Int] = map1.foldLeft(map2)((map, kv) => {
      map + (kv._1 -> (map.getOrElse(kv._1, 0) + kv._2))
    })
  }
}
