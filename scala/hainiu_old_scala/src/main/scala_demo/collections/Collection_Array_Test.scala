package collections

import scala.collection.mutable.ArrayBuffer
// 集合类自动转换
//import scala.collection.JavaConversions._
// 定长数组 -》 Array   变长数组 -》ArrayBuffer
/*
   在scala 编程中经常需要用到各种数据结构，比如数组（Array）、元组（Tuple）、列表（List）、映射（Map）、集合（Set）等。
   Scala同时支持可变集合和不可变集合，不可变集合从不可变，可以安全的并发访问；
   不可变集合：scala.collection.immutable
   可变集合： scala.collection.mutable
   Scala优先采用不可变集合，对于几乎所有的集合类，Scala都同时提供了可变和不可变的版本；
*/
//注意：
//设置带初始值的定长数组不能用new，因为是调用Array的静态对象，这个静态对象可以传递多个参数，而new的是调用类的构造方法只能接受一个参数就是数组的长度

object Collection_Array_Test {
  def main(args: Array[String]): Unit = {
    // 定义定长数组
    val arr1 = new Array[Int](10)
    // 要打印有值的需要转换成数组缓冲
    println(arr1.toBuffer)
    // 数组下标也是从0开始的，和java对比不同的是java用的[]，而scala用的是()
    arr1(0) = 100
    // 带初始值的定长数组不能用new
    val arr2 = Array("a", "b", "c")
    println(arr2.toBuffer)

    // 创建数组
    val array1: Array[Int] = new Array[Int](10)
    val array2: Array[Int] = Array(1, 2, 3, 4, 5)
    // 数组的元素可以改变 但是长度不可改变
    array1(0) = 2
    // 打印数组
    println(array1.toBuffer)


    // 定义变长数组
    val arrayBuffer: ArrayBuffer[Nothing] = new ArrayBuffer()
    val arr3 = ArrayBuffer[Int]()
    // 追加
    arr3.append(1)

    // 追加操作
    arr3 += 2 //单追加
    arr3 += (3, 4) //元组进行多个追加
    arr3 ++= Array(5, 6) //定长数组进行多个追加
    arr3 ++= ArrayBuffer(7, 8) //变长数组进行多个追加
    println(arr3)

    // 插入 在下标为0的位置插入多个元素，在下标为0的位置插入1和2两个元素
    arr3.insert(0, 1, 2)
    println(arr3)

    val arr4 = ArrayBuffer[Int](1, 2, 3, 4, 5, 6, 7, 8, 9)
    // 删除操作 从下标为1开始删除3个元素
    arr4.remove(1, 3)
    println(arr4)

    // 定长数组与变长数组的相互转换
    // 不可变数组
    val arr5 = Array(8, 5, 4, 7, 6, 2, 1, 3)
    // 不可变转可变数组
    val arr6 = arr5.toBuffer
    arr6.append(6)
    // 可变数组转不可变数组
    val arr7 = arr6.toArray

    // 1.数组遍历用for
    for (i <- arr5) print(i + " ")
    println()
    // 2.数组遍历下标方式
    for (i <- 0 until arr5.length) print(arr5(i) + " ")
    println()

    // 数组排序
    val arr5_sortby: Array[Int] = arr5.sortBy(f => f)
    val arr8: Array[Int] = arr5.sorted
    println(arr5.toBuffer)
    println(arr8.toBuffer)
    println(arr5_sortby.toBuffer)
  }

}
