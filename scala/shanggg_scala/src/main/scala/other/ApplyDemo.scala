package other

// apply 方法
object ApplyDemo {
  def main(args: Array[String]): Unit = {
    val user: User = new User(10, "aa")
    // 调用 apply 方法
    println(user(0))
    println(user(1))
  }
}

class User(val age:Int,val name:String){
  def apply(i: Int) = {
    i match {
      case 0 => age
      case 1 => name
      case _ => throw new IndexOutOfBoundsException("下标越界")
    }
  }
}