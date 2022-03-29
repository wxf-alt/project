package object_oriented.extend

// 定义抽象类
abstract class Person1 {
  val age:Int = 20

  def say(): Unit ={
    println(name + age)
  }

  var name: String
  def hello(): Unit
}

// 继承抽象类
class Teacher extends Person1 {
  // 重写属性和方法
  override var name: String = "teacher"

  override def hello(): Unit = {
    println("hello teacher")
  }
}

object Teacher{
  def main(args: Array[String]): Unit = {
    val teacher: Teacher = new Teacher()
    println(teacher.name)
  }
}