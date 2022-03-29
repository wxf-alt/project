package collections

import scala.collection.mutable.HashSet

// 集合
object Collection_Set_Test {
  def main(args: Array[String]): Unit = {

    val set: scala.collection.Set[Int] = Set(1, 9, 4)

    val set1: HashSet[Int] = HashSet(1, 7, 5)
    // 添加元素
    set1 += 1
    set1.add(9)
    // 添加集合
    set1 ++= Set(2,3)
    //删除元素
    set1 -= 2
    set1.remove(3)
    println(set1.toBuffer)


  }
}
