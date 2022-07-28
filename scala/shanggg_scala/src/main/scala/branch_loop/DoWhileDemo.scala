package branch_loop

object DoWhileDemo {
  def main(args: Array[String]): Unit = {
    var i:Int = 1
    do{
      println("宋宋，喜欢海狗人参丸" + i)
      i += 1
    }
    while (i <= 10)
  }
}
