package branch_loop

import scala.util.control.Breaks

// 循环遍历10以内的所有数据，奇数打印，偶数跳过（continue）
object ContinueDemo {
  def main(args: Array[String]): Unit = {

    for (i <- 1 until 11) {
      Breaks.breakable {
        if (i % 2 == 0) {
          Breaks.break()
        } else {
          println("i = " + i)
        }
      }
    }

  }
}
