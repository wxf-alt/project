package collections.util

import scala.math.Ordering

//case class User(val age:Int,val name:String) extends Comparable[User]{
//  // 降序
//  override def compareTo(o: User): Int =  o.age - this.age
//}

case class User(val age: Int, val name: String) extends Ordered[User] {
  // 升序
  //  override def compare(that: User): Int = this.age - that.age
  // 降序
  override def compare(that: User): Int = that.age - this.age
}

// 测试 sorted
object SortedDemo {
  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(30, 5, 60, 7, 1, 20)
    // sorted排序 默认是升序
    println(list.sorted)
    println(list.sorted.reverse)

    val list2: List[User] = List(User(12, "a"), User(20, "b"), User(9, "c"))

    // 使用 类实现 Ordered -> Comparable 进行比较
//    println(list2.sorted)

    // 使用 Ordering -> Comparator 进行比较
    println(list2.sorted(new Ordering[User] {
      override def compare(x: User, y: User): Int = x.age - y.age
    }))
  }
}
