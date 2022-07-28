package collections

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

// 可变数组和不可变数组之间转换
object ArrayDemo3 {
  def main(args: Array[String]): Unit = {
    // 定义不可变数组
    val array: Array[Int] = Array(1, 5, 6, 8, 7, 4)
    // 定长数组转为 可变数组
    val arrayBuffer: mutable.Buffer[Int] = array.toBuffer


    // 定义可变数组
    val arrayBuffer1: ArrayBuffer[Int] = ArrayBuffer(1, 6, 8, 7, 5, 2)
    // 可变转为 不可变
    val array1: Array[Int] = arrayBuffer1.toArray

  }
}
