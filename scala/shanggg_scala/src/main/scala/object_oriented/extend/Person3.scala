package object_oriented.extend

class Person3 {
  var name: String = "bobo"
}

//（1）伴生对象采用object关键字声明
object Person3 {
  var country: String = "China"
}

object Test1 {
  def main(args: Array[String]): Unit = {
    //（3）伴生对象中的属性和方法都可以通过伴生对象名（类名）直接调用访问。
    println(Person3.country)
    println(new Person3().name)
  }
}