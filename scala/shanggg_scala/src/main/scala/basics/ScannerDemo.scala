package basics

import scala.io.StdIn

object ScannerDemo {
  def main(args: Array[String]): Unit = {

//    // 1.利用 Scanner
//    val sc: Scanner = new Scanner(System.in)
//    println(sc.nextLine())

//    // 2.把标准输入流转换成字符流
//    val reader: BufferedReader = new BufferedReader(new InputStreamReader(System.in, "utf-8"))
//    val line: String = reader.readLine()
//    println(line)

    // 3.利用 StdIn
    val line: String = StdIn.readLine("请输入: ")
    println(line)
  }
}
