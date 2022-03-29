package object_oriented.class_demo

// 主构造器没有参数,所以可以省略()
//class Student()
class Student {
  var name: String = _
  var age: Int = _

  def this(age: Int) {
    this()
    this.age = age
    println("辅助构造器")
  }

  def this(age: Int, name: String) {
    this(age)
    this.name = name
  }
  println("主构造器")
}

object Student {
  def main(args: Array[String]): Unit = {
    val student: Student = new Student(18)
  }
}
