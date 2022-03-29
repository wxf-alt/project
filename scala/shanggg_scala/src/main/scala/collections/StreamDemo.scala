package collections

// 测试 stream
object StreamDemo {
  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(1, 5, 6, 8, 7)
    val stream: Stream[Int] = list.toStream
    println(stream)
    // 强制求值
    println(stream.force)

    // 测试 斐波那契数列
    println(fibSeq(30))
  }

  // 斐波那契数列
  def fibSeq(n:Int):List[Int] = {
    def loop(a:Int,b:Int):Stream[Int] = a #:: loop(b,a+b)
    loop(1,1).take(n).force.toList
  }
  def fibSeq2(n:Int):List[Int] = {
    def loop:Stream[Int] = 1 #:: loop.scanLeft(1)(_ + _)
    loop.take(n).force.toList
  }

}
