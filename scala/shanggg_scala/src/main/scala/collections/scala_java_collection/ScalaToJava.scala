package collections.scala_java_collection

import java.util

// scala 和 java 集合互转
// java 集合都是可变的；scala 可变，不可变都有
object ScalaToJava {
  def main(args: Array[String]): Unit = {
    val list_java: util.ArrayList[Int] = new util.ArrayList[Int]()
    list_java.add(10)
    list_java.add(20)
    list_java.add(100)
    list_java.add(60)

    // java集合 和 scala集合 互转(可以使用scala集合的方法)
    import scala.collection.JavaConversions._
//    import scala.collection.convert.wrapAsJava._
//    import scala.collection.convert.wrapAsScala._
    list_java += 1000
  }
}
