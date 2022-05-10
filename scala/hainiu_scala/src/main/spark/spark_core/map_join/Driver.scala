package spark_core.map_join

import org.apache.hadoop.util.ProgramDriver

object Driver {
  def main(args: Array[String]): Unit = {
    val driver: ProgramDriver = new ProgramDriver
    driver.addClass("mapJoin",classOf[MapJoinDemo2],"mapJoin任务")
    driver.run(args)
  }
}


