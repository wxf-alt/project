package collections

import scala.collection.mutable.ListBuffer

// 可变 List
object ListDemo2 {
  def main(args: Array[String]): Unit = {
    val listBuffer: ListBuffer[Int] = ListBuffer(1, 2, 3)

    // 添加数据
    listBuffer += 4
    // 在头部添加
    100 +=: listBuffer
    println(listBuffer)
  }
}
