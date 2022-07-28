package object_oriented.traits

// 定义特质
trait PersonTrait {
  // 声明普通属性
  var name:String = _
  // 声明抽象属性
  var age:Int

  // 声明普通方法
  def say(name:String): Unit ={
    println(s"$name 你好")
  }
  // 声明抽象方法
  def eat():Unit
}
