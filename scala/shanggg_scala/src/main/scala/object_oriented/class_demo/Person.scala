package object_oriented.class_demo

import scala.beans.BeanProperty

// Scala语法中，类默认是 public
class Person {
  // _表示给属性一个默认值
  @BeanProperty var name:String  = _
  private var age: Int = _
}

object Person{
  def main(args: Array[String]): Unit = {
    val person: Person = new Person()
    person.setName("asd")
    person.age = 20
    println(person.getName + "\t" + person.age)
  }
}

// 一个Scala源文件可以包含多个类
class Teacher1{}
