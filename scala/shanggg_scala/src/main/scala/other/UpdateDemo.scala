package other

//noinspection DuplicatedCode
// update 方法
object UpdateDemo {
  def main(args: Array[String]): Unit = {
    val user: User1 = new User1(10, "aa")
    // 调用 update 方法
    user(0) = 100
    user(1) = "zx"
    println(user.age + "\t" + user.name)
  }
}

class User1(var age: Int, var name: String) {
  def update[T](i: Int, v: T) = i match {
    case 0 => age = v.asInstanceOf[Int]
    case 1 => name = v.asInstanceOf[String]
    case _ => throw new IndexOutOfBoundsException("下标越界")
  }
}
