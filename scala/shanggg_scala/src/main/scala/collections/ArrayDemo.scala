package collections

//noinspection DuplicatedCode
// 不可变数组
object ArrayDemo {
  def main(args: Array[String]): Unit = {

    // 创建长度为 10 的数组
    val array: Array[Int] = new Array[Int](10)
    println(array.mkString(","))

    val arr: Array[Int] = Array[Int](1, 5, 4, 7, 9, 2)
    println(arr(4))

    // 遍历数组  for , while

    // 添加元素 生成新的数组
    // 只要运算符是以 : 结尾 就是右结合(以右边为主)
    // 在后面添加 --> :+
    val arr2: Array[Int] = arr :+ 5
    println(arr2.mkString(","))
    // 再前面添加 --> +:
    val arr3: Array[Int] = 100 +: arr2
    println(arr3.mkString(","))
  }
}
