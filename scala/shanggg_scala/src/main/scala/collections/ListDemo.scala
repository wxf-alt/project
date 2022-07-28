package collections

// 不可变 List
object ListDemo {
  def main(args: Array[String]): Unit = {
    // 创建有元素集合
    val list: List[Int] = List(1, 5, 6, 8, 7)
    val l7: ::[Int] = ::[Int](1, 1 :: Nil)
    println(list)

    // 创建 空集合
    val l2: List[Int] = List[Int]()
    val l3: Nil.type = Nil


    // 尾部添加
    val l4: List[Int] = 15 +: l2 :+ 12
    println(l4)

    // 不可变 List 专用
    // 再头部添加
    val l5: List[Int] = 100 :: l4
    println(l5)

    // 合并两个 List
//    val l6: List[Int] = l5 ++ l4
    val l6: List[Int] = l5 ::: l4
    println(l6)
  }
}
