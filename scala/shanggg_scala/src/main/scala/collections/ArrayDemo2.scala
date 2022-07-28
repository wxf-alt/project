package collections

import scala.collection.mutable.ArrayBuffer

// 可变数组 ArrayBuffer
object ArrayDemo2 {
  def main(args: Array[String]): Unit = {
    val arrayBuffer: ArrayBuffer[Int] = new ArrayBuffer[Int]()
    val arrayBuffer2: ArrayBuffer[Int] = ArrayBuffer(1, 2, 3, 4)

    // 尾部追加
    arrayBuffer2 += 100
    // 头部添加
    200 +=: arrayBuffer2
    println(arrayBuffer2)

    // 合并两个数组
    println(arrayBuffer)
    arrayBuffer ++= arrayBuffer2
    println(arrayBuffer)

    // 删除元素
    arrayBuffer2 -= 1   // 只删除符合的第一个元素
    println(arrayBuffer2)

  }
}
