package basics

object UnitDemo {
  def main(args: Array[String]): Unit = {
    // unit表示没有返回值，即void
    def sayOk() : Unit = {
      println("say ok")
    }
    sayOk()
  }
}
