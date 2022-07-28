package exceptions

import java.io.IOException

import scala.io.StdIn

// 异常
object ExceptionDemo {
  def main(args: Array[String]): Unit = {

    val in: Int = StdIn.readLine("输入数字").toInt
    if(in == 0) throw new ArithmeticException("除数不能为 0 ")

    // try - catch
    try {
      val i: Int = 1 / 0
    } catch {
      case e:ArithmeticException => println("发生异常")
//      case e:Exception => println("发生异常")
    }finally {
      println("aaaa")
    }
    println("bbbbb")
  }


  // 抛出异常类型
  @throws(classOf[IOException])
  @throws(classOf[RuntimeException])
  def foo(): Unit ={
    println("aaa")
  }

}
