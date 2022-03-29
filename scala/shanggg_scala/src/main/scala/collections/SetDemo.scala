package collections

// 不可变 Set
object SetDemo {
  def main(args: Array[String]): Unit = {
    val set: Set[Int] = Set(4, 3, 1, 2, 3, 4, 2, 1, 4)
    println(set)

    val set1: Set[Int] = set + 10
    println(set1)

    val mySet1: Set[Int] = Set(30, 50, 70, 60, 10, 20)
    val mySet2: Set[Int] = Set(30, 5, 70, 6, 10, 2)

    // 并集 union
//    val mySet3: Set[Int] = mySet1 ++ mySet2
    val mySet3: Set[Int] = mySet1 | mySet2
//    val mySet3: Set[Int] = mySet1 union mySet2
    println(mySet3)

    // 交集 intersect
    val mySet4: Set[Int] = mySet1 & mySet2
//    val mySet4: Set[Int] = mySet1 intersect mySet2
    println(mySet4)

    // 差集 diff
    val mySet5: Set[Int] = mySet1 &~ mySet2
//    val mySet5: Set[Int] = mySet1 -- mySet2
//    val mySet5: Set[Int] = mySet1 diff mySet2
    println(mySet5)

  }
}
