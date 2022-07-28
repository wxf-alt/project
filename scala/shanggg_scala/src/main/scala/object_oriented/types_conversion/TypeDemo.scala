package object_oriented.types_conversion

// Type定义新类型 (就是给类型起别名)
object TypeDemo {
  def main(args: Array[String]): Unit = {
    type S = String
    val str: S = "abc"

    def test(): S = "aaa"
  }
}
