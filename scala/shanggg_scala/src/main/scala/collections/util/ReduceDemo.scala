package collections.util

// reduce 聚合操作
object ReduceDemo {
  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(30, 50, 60, 70, 90, 20)
    //    val reduceList: Int = list.reduce((x, y) => x + y)
    val reduceList: Int = list.reduce(_ + _)
    println(reduceList)


    // 从左向右进行聚合
    //    list.redusceLeft
    // 从右向左进行聚合
    //    list.reduceRight
  }
}
