package collections.util

// 集合基本操作
object Opt1 {
  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(30, 50, 60, 70, 90, 20)
    val list1: List[Int] = List(3, 50, 6, 70, 9, 20)

    // 1.获取头部元素
    println(list.head)
    // 2.获取尾部元素
    println(list.last)
    // 3.获取除头部元素的其他元素(去掉第一个元素后的，其他数据)
    println(list.tail)
    // 4.去掉最后一个元素
    println(list.init)
    // 5.获取长度  size 是 length 的别名
    println(list.size)
    println(list.length)
    // 6.集合转为字符串
    println(list.toString())
    println(list.mkString(","))
    println(list.mkString("List:(", ",", ")"))

    // 7.迭代器
    val iterator: Iterator[Int] = list.iterator
    val iterator1: Iterator[Int] = list.toIterator
    val iterable: Iterable[Int] = list.toIterable
    // 遍历迭代器
    while (iterator.hasNext) {
      val e: Int = iterator.next()
      println(e)
    }
    // 遍历迭代器
    for (elem <- iterator) {
      println(elem)
    }

    // 8.是否包含
    println(list.contains(1))

    // 9.反转
    val reverse: List[Int] = list.reverse
    println(reverse)

    // 10.获取前几个数据
    val takeList: List[Int] = list.take(3)
    println(takeList)

    // 11.抛弃前几个
    val dropList: List[Int] = list.drop(3)
    println(dropList)

    // 12.获取满足条件的数据
    // 只要碰到不符合条件 就不会在运行 将前面的数据全部返回
    val takeWhileList: List[Int] = list.takeWhile(x => x <= 50) // List(30, 50)
    // 和 takeWhile 相反，碰到(函数为true) 继续运行 (函数为false) 返回后面的全部结果
    val dropWhileList: List[Int] = list.dropWhile(x => x <= 50) // List(60, 70, 90, 20)
    println(takeWhileList)
    println(dropWhileList)

    // 13.获取后几个数据
    val takeRightList: List[Int] = list.takeRight(3)
    println(takeRightList)

    // 并集
    val unionList: List[Int] = list union list1
    println(unionList)
    // 交集
    val intersectList: List[Int] = list intersect list1
    println(intersectList)
    // 差集
    val diffList: List[Int] = list diff list1
    println(diffList)

    // 拉链
    // 注:如果两个集合的元素个数不相等，那么会将同等数量的数据进行拉链，多余的数据省略不用
    val list2: List[Int] = List(3, 50, 6, 70, 9, 20, 300)
    val zipList: List[(Int, Int)] = list.zip(list1)
    // 可以给默认值  -1 给前面的List -2给后面的List
    val zipList2: List[(Int, Int)] = list2.zipAll(list, -1, -2)
    println(zipList2)
    // 和自己的索引进行 zip
    val zipWithIndexList: List[(Int, Int)] = list2.zipWithIndex
    println(zipWithIndexList)

    val list3: List[(String, Int)] = List(("a" -> 1), ("b" -> 2), ("c" -> 3))
    val unzipList: (List[String], List[Int]) = list3.unzip
    println(unzipList)

    // 滑窗
    val slidingList: Iterator[List[Int]] = list.sliding(2, 1)
    println(slidingList.toList)

  }
}
