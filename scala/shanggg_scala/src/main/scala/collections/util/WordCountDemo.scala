package collections.util

import scala.io.{BufferedSource, Source}

//noinspection SourceNotClosed
object WordCountDemo {
  def main(args: Array[String]): Unit = {

    val path: String = "E:\\A_data\\3.code\\project\\scala\\shanggg_scala\\src\\main\\scala\\collections\\util\\Cacl1.scala"
    // 读取文件
    val strings: Iterator[String] = Source.fromFile(path, "utf-8").getLines()

    val words: List[String] = strings.flatMap(_.split("\\w+")).toList
    val wordGroupBy: Map[String, List[String]] = words.groupBy(x => x)
    val result: Map[String, Int] = wordGroupBy.mapValues(_.size)
    println(result)
  }
}
