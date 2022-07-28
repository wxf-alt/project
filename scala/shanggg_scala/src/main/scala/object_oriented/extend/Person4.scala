package object_oriented.extend

//（2）如果想让主构造器变成私有的，可以在()之前加上private
class Person4 private(cName: String) {
  var name: String = cName
}

object Person4 {
  def apply(): Person4 = {
    println("apply空参被调用")
    new Person4("xx")
  }
  def apply(name: String): Person4 = {
    println("apply有参被调用")
    new Person4(name)
  }
}

object Test {
  def main(args: Array[String]): Unit = {
    //（1）通过伴生对象的apply方法，实现不使用new关键字创建对象。
    val p1: Person4 = Person4()
    println("p1.name=" + p1.name)

    val p2: Person4 = Person4("bobo")
    println("p2.name=" + p2.name)
  }
}
