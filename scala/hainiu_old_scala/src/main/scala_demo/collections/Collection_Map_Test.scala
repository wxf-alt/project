package collections

import scala.collection.mutable

// 映射 -> 映射也就是一个hash表，相当于java里的map
object Collection_Map_Test {
  def main(args: Array[String]): Unit = {
    /*// 不可变map对象
    val map: Map[String, Int] = Map(("a", 3), ("c", 4), ("g", 9))
    // 遍历
    map.foreach(println)
    // 不能修改元素的值
//    map("a") = 155
    println(map.getOrElse("d", "不存在"))

    // 创建不可变map对象
    val map1 = Map("a" -> 1,"b" -> 2,"c" -> 3)       //scala中通常用->来表示键值对
    val map2 = Map(("a",1),("b",2),("c",3))          //使用元组的方式来声明一个映射
    map2("d")     //取指定key的value的方法，如果key不存在抛异常（和python的字典很像）
    map2.getOrElse("d", 4)  // 取指定key的value的方法，如果key不存在返回设定的默认值（和python的字典很像）
    val map3 = map1 + ("d" -> 4)
*/
    val map2 = mutable.Map(("a",1),("b",2),("c",3))
    map2("a")=5

    //添加
    map2("a") = 1
    map2 += (("b",1))
    map2 += (("c",1),("d",2),"e" -> 2)
    map2.put("f",4)

    //删除
    map2 -= "a"
    map2.remove("b")

    // 使用for循环进行遍历
    val map = Map("a" -> 1, "b" -> 2, "c" -> 3)
    // 遍历kv
    for ((k, v) <- map) {
      println(k + ", " + v)
    }
    // 遍历k
    for (k <- map.keys) {
      println(k + ", " + map(k))
    }
    // 遍历v
    for (v <- map.values) {
      println(v)
    }


  }
}
