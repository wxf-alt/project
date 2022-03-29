package object_oriented.class_demo

// 没有修饰符 -> 局部变量
// var 修饰 -> 可以修改
// val 修饰 -> 不能修改
class Teacher(name: String, var age: Int, val sex: String){
  override def toString: String = {
    s"${name},${age},${sex}"
  }
}

object Teacher{
  def main(args: Array[String]): Unit = {
    var teacher: Teacher = new Teacher("aa", 20, "男")

    // 局部变量  获取不到
//    teacher.name

    // var修饰参数，作为类的成员属性使用，可以修改
    teacher.age = 24
    println(teacher.toString)

    // val修饰参数，作为类的只读属性使用，不能修改
//    teacher.sex = "女"
    println(teacher.sex)
  }
}