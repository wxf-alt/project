package equals_eq_sameElements

/*
 eq：是比较内存地址。
 equals：比较样例类的数据用 。
 sameElements：比较集合内的数据用
 */
object Equals_Eq_SameElements_Test {
  def main(args: Array[String]): Unit = {
    val a1: HainiuTest = HainiuTest("aa", 10)
    val a2: HainiuTest = HainiuTest("aa", 10)

    val b1: HainiuTest1 = new HainiuTest1("aa", 10)
    val b2: HainiuTest1 = new HainiuTest1("aa", 10)

    println("样例类 eq 比较结果：" + a1.eq(a2))
    println("样例类 equals 比较结果：" + a1.equals(a2))
    println("eq 比较结果：" + b1.eq(b2))
    println("equals 比较结果：" + b1.equals(b2))

    val list1: List[Int] = List(1, 2, 3)
    val list2: List[Int] = List(1, 2, 3)
    val list3: List[Int] = List(1, 2, 3,4)
    println("sameElements 比较结果：" + list1.sameElements(list2))
    println("sameElements 比较结果：" + list1.sameElements(list3))
  }
}

case class HainiuTest(name:String,age:Int)

class HainiuTest1(val name:String,val age:Int)
