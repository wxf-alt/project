package collections

object Foreach_Test {
  def main(args: Array[String]): Unit = {
    val l = List(1,2,3)
    l.foreach[Unit]((f:Int) => {println(f)})
    l.foreach((f:Int) => {println(f)})
    l.foreach((f:Int) => println(f))
    l.foreach((f) => println(f))
    l.foreach(f => println(f))
    l.foreach(println(_))
    l.foreach(println)
    l foreach println
  }
}
