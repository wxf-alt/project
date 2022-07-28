package basics

object NothingDemo {
  def main(args: Array[String]): Unit = {
    // 此方法不会正常返回
    def test() : Nothing={
      throw new Exception()
    }
    test()
  }
}
