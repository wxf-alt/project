package genericity

// 上下文界定
object GenericDemo5 {
  def main(args: Array[String]): Unit = {
    println(max(10, 20))
  }

  def max[T](x:T,y:T)(implicit ord:Ordering[T]): T ={
    if(ord.gt(x,y)) x else y
  }

  // 上下文界定
  def max1[T:Ordering](x:T,y:T): T ={
    // 从冥界召唤隐式值
    val ord: Ordering[T] = implicitly[Ordering[T]]
    if(ord.gt(x,y)) x else y
  }

}
