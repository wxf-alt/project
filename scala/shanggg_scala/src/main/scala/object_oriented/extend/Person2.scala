package object_oriented.extend

// 匿名子类
abstract class Person2 {
  val name: String
  def hello(): Unit
}

object Persons{
  def main(args: Array[String]): Unit = {
    val person: Person2 = new Person2 {
      override val name: String = "teacher"

      override def hello(): Unit = println("hello teacher")
    }
    println(person.name)
  }
}
