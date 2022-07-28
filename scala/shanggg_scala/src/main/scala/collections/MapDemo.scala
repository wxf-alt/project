package collections

// 不可变 Map
object MapDemo {
  def main(args: Array[String]): Unit = {
    // 创建 Map
    var myMap: Map[String, Int] = Map("a" -> 97, "b" -> 97)
    var map1: Map[String, Int] = Map(("a", 97), ("b", 98), ("c", 99), ("d", 98))

    // 遍历
    for (elem <- map1) {
      println(elem)
    }
    println("=================")
    map1 += "f" -> 63
    for (elem <- map1) {
      println(elem)
    }

//    // 只获取 值为98的key
//    for ((k, 98) <- map1) {
//      println(k)
//    }
//
//    // 添加元素  如果key存在，值就会更新
//    val map2: Map[String, Int] = map1 + ("d" -> 20)
//    // 遍历
//    for (elem <- map2) {
//      println(elem)
//    }
//
//    // 根据key 获取value
//    //    val v1: Int = map2("d")
////    val v1: Int = if (map2.get("f").eq(None)) 0 else map2("f")
//    val v1: Int = map2.getOrElse("d", 0)
//    println(v1)

  }
}
