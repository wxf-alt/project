package for_if_tield

import scala.util.control.Breaks

// ④ break 循环控制
// Scala 不支持 break 或 continue 语句，但从 2.8 版本后使用另外一种方式来实现 break 语句。
// 当在循环中使用 break 语句，在执行到该语句时，
// 就会中断循环并执行循环体之后的代码块。

// 伴生类
case class Break_Test()

object Break_Test {
  def main(args: Array[String]): Unit = {

    val breaks: Breaks = new Breaks
    breaks.breakable(
      for( i <- 1 to 10){
        // 判断 然后跳出循环
        if( i > 5) breaks.break()
        print(i + " ")
      }
    )

    println("------- continue 的逻辑 ---------")

    // 实现continue的逻辑
    // 在 for 循环体中 调用 Breaks 的 breakable方法 进行 循环控制
    // 控制的只是在 breakable方法体中的代码，整个方法还处在循环体中
    for( i <- 1 to 10){
      breaks.breakable(
        if(i == 6){
          println("\n")
          breaks.break()
        }
        else print(i + " ")
      )
    }


  }
}


/*
// 导入以下包
import scala.util.control._
// 创建 Breaks 对象
val loop = new Breaks;
// 在 breakable 中循环
loop.breakable(
// 循环
for(...){
....
// 循环中断
loop.break;
}
)*/
