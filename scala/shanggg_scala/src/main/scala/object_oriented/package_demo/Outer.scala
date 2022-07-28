// 父包
package object_oriented.package_demo{
  import object_oriented.package_demo.subpackage.Inner
  object Outer {
    val out: String = "out"
    def main(args: Array[String]): Unit = {
      // 父包访问子包需要导包
      println(Inner.in)
    }
  }

  // 子包
  package subpackage{
    object Inner{
      val in: String = "in"
      def main(args: Array[String]): Unit = {
        println(Outer.out)
      }
    }
  }
}


