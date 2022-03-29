package collections

import scala.collection.mutable

// 可变 Map
object MapDemo2 {
  def main(args: Array[String]): Unit = {
    val map: mutable.Map[String, Int] = mutable.Map(
      "a" -> 12,
      "b" -> 65,
      "c" -> 57
    )

    map += ("a" -> 100)
    println(map)

    // 如果 key 不存在，则会组合成一个新的kv，添加到可变的map中
    val c: Int = map.getOrElseUpdate("f", 46)
    println(c)
    println(map)

    // 更新 key 值
    map("a") = 20
//    map.update("a", 20)
    println(map)

  }
}
