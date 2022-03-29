package basics

object DoubleDemo {
  def main(args: Array[String]): Unit = {
    // 建议，在开发中需要高精度小数时，请选择Double
    var n7: Float = 2.2345678912f
    var n8: Double = 2.2345678912

    println("n7=" + n7)
    println("n8=" + n8)
  }
}
