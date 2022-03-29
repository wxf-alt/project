package implicit_function

import java.io.File
import scala.io.{BufferedSource, Source}

//noinspection LanguageFeature
// 隐式类
object ImplicitDemo2 {
  def main(args: Array[String]): Unit = {

    val content: String = new File("C:\\Users\\wxf\\Desktop\\桌面下载文件夹\\new 2.txt").readContent
    println(content)
  }

  implicit class RichFile(file:File) {
    def readContent: String = {
      val source: BufferedSource = Source.fromFile(file, "utf-8")
      val content: String = source.mkString
      content
    }
  }
}


//implicit class RichFile(file:File) {
//  def readContent: String = {
//    val source: BufferedSource = Source.fromFile(file, "utf-8")
//    val content: String = source.mkString
//    content
//  }
//}
