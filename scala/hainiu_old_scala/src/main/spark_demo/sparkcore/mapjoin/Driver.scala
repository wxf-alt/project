package sparkcore.mapjoin

import org.apache.hadoop.util.ProgramDriver
import sparkcore.Word_Count

// 在 Edit Configuration 中的 prograum arguments 添加设定的 类名称
object Driver {
  def main(args: Array[String]): Unit = {
    val driver = new ProgramDriver
    // MapJoin 需要有伴生类，classOf找的是伴生类
    driver.addClass("mapjoin", classOf[MapJoin], "mapJoin任务")
//    driver.addClass("word_count", classOf[Word_Count], "word_count任务")
    driver.run(args)

  }
}