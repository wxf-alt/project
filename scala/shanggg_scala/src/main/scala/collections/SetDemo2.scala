package collections

import scala.collection.mutable

// 可变 Set
object SetDemo2 {
  def main(args: Array[String]): Unit = {
    val set1: mutable.Set[Int] = mutable.Set(10, 30, 50)
    set1 += 20
    println(set1)

    // 其他集合 利用Set 进行去重
    val list1: List[Int] = List(10, 50, 46, 12, 32, 10, 50)
    val list2Set: List[Int] = list1.toSet.toList
    println(list2Set)
  }
}
