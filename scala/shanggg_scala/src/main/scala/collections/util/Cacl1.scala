package collections.util

// 集合计算的初级函数
object Cacl1 {
  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(30, 50, 60, 70, 10, 20)
    // 求和
    println(list.sum)
    // 最大值
    println(list.max)
    // 最小值
    println(list.min)
    // 乘积
    println(list.product)

    // 排序
    println(list.sorted)
    println(list.sortBy(x => x))
    println(list.sortWith((x, y) => x > y))
  }
}
