package object_oriented.package_demo

// 包对象测试
case class PackageDemo(){
  private val str: String = shareValue + "\tPackageDemo"
  def f1(): Unit ={
    shareMethod()
  }
}

object PackageDemo {
  def main(args: Array[String]): Unit = {
    // 直接访问包对象中的内容
    println(shareValue)
    shareMethod()

    // 调用伴生类的属性和方法
    println(PackageDemo().str)
    PackageDemo().f1()
  }
}