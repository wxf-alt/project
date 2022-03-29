package object_oriented.traits

class User(val name: String, val age: Int)

trait Dao {
  def insert(user: User): Unit = {
    println("insert into database :" + user.name)
  }
}
// 自身类型可实现依赖注入的功能
trait APP {
  _: Dao =>
  def login(user: User): Unit = {
    println("login :" + user.name)
    insert(user)
  }
}

object MyApp extends APP with Dao {
  def main(args: Array[String]): Unit = {
    login(new User("bobo", 11))
  }
}