package basics

object StringDemo {
  def main(args: Array[String]): Unit = {
    val a:Int = 20
    printf("格式化输出 %d",a)

    printf("浮点数：%f",math.Pi)
    printf("浮点数：%.2f",math.Pi)
    printf("字符串：%s,%f,%d","abc",1.2,1)

    // s 插值法
    val a1: Int = 10
    val b1: Int = 20
    println(s"a = $a1,b = ${b1 * 2}")

    // 原始插值法
    val s: String = raw"\n \t \s"
    println(s)
  }
}
