package object_oriented

// 可以为每个包定义一个同名的 包对象
package object package_demo {
  // 定义在包对象中的成员，作为其对应包下所有class和object的共享变量，可以被直接访问
  val shareValue: String ="share"
  def shareMethod(): Unit ={
    println("I am package object object_oriented")
  }
}
