package collections

import scala.collection.mutable

// 栈
object StackDemo {
  def main(args: Array[String]): Unit = {
    // 栈顶  栈底
    val stack: mutable.Stack[Int] = mutable.Stack(10, 20, 30)
    val p: Int = stack.pop()
    stack.push(100)
    val p1: Int = stack.pop()
    println(p)  // 10
    println(p1)  // 100
  }
}
