package genericity

// 泛型的型变
object GenericDemo6 {
  def main(args: Array[String]): Unit = {
      // 父类型的集合对象，可以赋值给子类型的集合引用 --> 逆变
    val fList: MyList[Son] = new MyList[Father]

    // 子类型的集合对象 可以赋值给父类型的集合引用 --> 协变
    val sList: MyList1[Father] = new MyList1[Son]
  }
}

class MyList[-T]
class MyList1[+T]
class Father
class Son extends Father