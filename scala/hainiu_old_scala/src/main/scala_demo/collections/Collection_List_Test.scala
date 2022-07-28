package collections

import scala.collection.mutable.ListBuffer
// 列表
// 定长列表 -》 List   变长列表 -》ListBuffer
/* 注意：在可变list上也可以调用不可变list的"::","+:",":+","++",":::"，
        区别是可变list返回的是新的ListBuffer，不可变list返回的是新的List
    ::     在list前面添加        【ListBuffer 不可用】
    +:    在list前面添加
    :+    在list后面添加
    ++   两个list拼接
    :::    两个list拼接            【ListBuffer 不可用】
    Nil   表示空列表
*/
object Collection_List_Test {
  def main(args: Array[String]): Unit = {

    // list 不能修改元素和长度
    val list: List[Int] = List(1, 5, 9)
    val list_2: List[Int] = 0 +: list
    val list_3: List[Int] = list :+ 6
    println(list.toBuffer)
    println(list_2.toBuffer)
    println(list_3.toBuffer)

    println("====================================")

    val list1 = List(1,2,3)

    //在list前面添加
    val list2 = 0 :: list1        //在原有的list1前面加一个0并赋值给list2
    //以下3种和上面这种结果一样的，只是写法不一样
    val list3 =list1.::(0)
    val list4 = 0 +: list1
    val list5 = list.+:(0)

    //在list后面添加
    val list6 = list1 :+ 4
    val list7 = list1.:+(4)

    //合并两个list并生成新的list
    val list8 = list1 ++ list7
    val list9 = list1 ::: list7

    println("====================================")

    val listBuffer: ListBuffer[String] = ListBuffer("asd", "ksdf", "ref")
    // 追加
    listBuffer.append("dgfs")
    println(listBuffer)
    // 插入
    listBuffer.insert(1,"123") // 在索引为1的位置插入记录
    println(listBuffer)

    val list1Buffer = ListBuffer(1,2,3)
    //list后面添加元素
    list1Buffer += 4
    list1Buffer.append(5)
    //list前面添加元素
    list1Buffer.insert(0,0)
    //list 删除下标为2 的元素
    list1Buffer.remove(2)

    //合并list
    val list2Buffer = ListBuffer(6,7,8)
    list1Buffer ++= list2Buffer

    // 转可变的ArrayBuffer
    val l1 = List(1,2,3)
    val l2 = l1.toBuffer

    // 转不可变list
    val l3 = ListBuffer(1,2,3)
    val l4 = l3.toList



  }
}
