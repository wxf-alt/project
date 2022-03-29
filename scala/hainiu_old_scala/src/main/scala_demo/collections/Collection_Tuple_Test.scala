package collections

/*
  scala 的元组是对多个不同类型对象的一种简单封装。Scala 提供了TupleN 类（N的范围为1 ~ 22），用于创建一个包含N个元素的元组。
  构造元组只需把多个元素用逗号隔开并用圆括号括起来。
 */
object Collection_Tuple_Test {
  def main(args: Array[String]): Unit = {
    val tuple = (8,5,1)
//    元组取值时，元组的下标是从1开始的，而数组的下标是从0开始的，要注意区别；
    println(tuple._2) // 5

    // 元组的元素不可变
//    tuple._2 = 6

  }

}
