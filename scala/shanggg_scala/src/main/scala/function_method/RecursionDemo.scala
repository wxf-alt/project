package function_method

// 递归
object RecursionDemo {
  def main(args: Array[String]): Unit = {
/*    阶乘
     递归算法
     1) 方法调用自身
     2) 方法必须要有跳出的逻辑
     3) 方法调用自身时，传递的参数应该有规律
     4) scala中的递归必须声明函数返回值类型*/
    println(test1(5,1))
  }

  // 递归
  def test( i : Int ) : Int = {
    if ( i == 1 ) {
      1
    } else {
      i * test(i-1)
    }
  }

  // 尾递归
  @scala.annotation.tailrec
  def test1(i : Int, acc:Int) : Int = {
    if ( i <= 1 ) {
      acc
    } else {
      test1(i-1,acc * i)
    }
  }

}