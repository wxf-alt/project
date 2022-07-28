package for_if_tield

// ② for 循环语句测试
object For_Test {
  def main(args: Array[String]): Unit = {
    val x: Int = 10

    // 1 to 10 : 返回的是1 到 10 的值
    for (i <- 1 to x) {
      println(" i = " + i)
    }

    // 双重循环
    println("双重循环")
    for (i <- 1 to 3; j <- 1 to 2) {
      println(" i = " + i + ";j = " + j)
    }

    // 利用守卫
    println("利用守卫")
    // 双层循环中，外循环1到3， 内循环1到2，输出外层循环1到2，内层循环1 的值
    for (i <- 1 to 3 if (i <= 2); j <- 1 to 2 if (j <= 1)) {
      println(" i = " + i + ";j = " + j)
    }

    // 1 to 10 返回1到10的所有值
    for(i <- 1 to 10){print(i + " ")}
    println()
    // 1 until 10 等于Range(1, 10) 返回1到9的所有值
    for(i <- 1 until  10){print(i + " ")};println()
    for(i <- Range(1,10)){print(i + " ")}

  }
}
