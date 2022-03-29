package collections

import scala.collection.mutable

// 队列
object QueueDemo {
  def main(args: Array[String]): Unit = {
    val q1: mutable.Queue[Int] = mutable.Queue(10, 20, 30)
    // 添加元素
    q1.enqueue(100)
    // 出队  删除第一个元素
    q1.dequeue()
    // Queue(20, 30, 100)
    println(q1)
  }
}
