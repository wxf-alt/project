package wordcount

object WordCount_Test {
  def main(args: Array[String]): Unit = {
    val list: List[String] = List("a b c d", "a d e s", "a b d e", "a a a b")
    val wordCount: List[(String, Int)] = list.flatMap(_.split(" ")).map((_, 1)).groupBy(_._1).mapValues(_.length).toList.sortBy(_._2).reverse
    println(wordCount.toBuffer)
  }
}
