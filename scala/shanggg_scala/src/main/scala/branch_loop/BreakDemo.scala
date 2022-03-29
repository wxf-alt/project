package branch_loop

import scala.util.control.Breaks

// 循环遍历10以内的所有数据，数值为5，结束循环（break）
object BreakDemo {
  def main(args: Array[String]): Unit = {
//    val breaks: Breaks = new Breaks()
//    breaks.breakable{
//      for(i <- 1 to 10){
//        if(i == 5){
//          breaks.break()
//        }
//        println("i = " + i)
//      }
//    }
    Breaks.breakable{
      for(i <- 1 to 10){
        if(i == 5){
          Breaks.break()
        }
        println("i = " + i)
      }
    }

  }
}
