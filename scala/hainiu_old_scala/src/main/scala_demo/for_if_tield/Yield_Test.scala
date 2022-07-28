package for_if_tield

import scala.collection.immutable

// ③ yield 关键字结合 for 循环使用
object Yield_Test {
  def main(args: Array[String]): Unit = {

    // yield的作用是把每次迭代生成的值封装到一个集合中,当然也可以理解成yield会自动创建一个集合
    val c: immutable.IndexedSeq[Int] = for (i <- 1 until 10) yield i
    print(c + " ")


  }
}
