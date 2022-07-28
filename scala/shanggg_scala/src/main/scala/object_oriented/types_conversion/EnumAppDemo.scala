package object_oriented.types_conversion

// 定义枚举和应用类
object EnumAppDemo {
  def main(args: Array[String]): Unit = {
    println(Color.RED)
  }
}

// 定义枚举类
object Color extends Enumeration{
  // Value() 中的第一个参数一定是一个 唯一值
  val RED: Color.Value = Value(1, "red")
  val YELLOW: Color.Value = Value(2, "yellow")
  val BLUE: Color.Value = Value(3, "blue")
}

// 定义应用类
object Test20 extends App{
  println("xxxxxxxxxx")
}