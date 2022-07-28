package collections.util

// 测试 sortBy
object SortByDemo {
  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(30, 5, 60, 7, 1, 20)
    println(list.sortBy(x => x)(Ordering.Int.reverse))

    val list2: List[String] = List("hello", "world", "as", "aaa", "bb")
    println(list2.sortBy(x => x.length))
    // 先按照 字符串长度排，然后再按照字母表排序
    // 多个指标，就把每个指标放在元组中
    println(list2.sortBy(x => (x.length, x)))
    // 长度升序，字母表降序 排序
    println(list2.sortBy(x => (x.length, x))(Ordering.Tuple2(Ordering.Int, Ordering.String.reverse)))

    val list3: List[User] = List(User(12, "a"), User(20, "b"), User(9, "c"))
    // 年龄升序，姓名升序
    println(list3.sortBy(x => (x.age, x.name)))
  }
}
