package basics

object IntDemo {
  def main(args: Array[String]): Unit = {
    var n5: Int = 10
    println(n5)

//    Scala的整型，默认为Int型，声明Long型，须后加‘l’或‘L’
    var n6: Long = 9223372036854775807L
    var n7: Long = 879546254785658l
    println(n6)
    println(n7)
  }
}
