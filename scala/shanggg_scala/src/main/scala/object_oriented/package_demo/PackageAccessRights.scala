package object_oriented.package_demo

object PackageAccessRights {
  def main(args: Array[String]): Unit = {
    val accessRights: PackageAccessRights = new PackageAccessRights()
    accessRights.say()
    println(accessRights.name)
    println(accessRights.age)
    println(accessRights.sex)
  }
}

class PackageAccessRights{
  // 只有本类和伴生对象可以使用
  private var name: String = "bobo"
  // 同类和子类都可以使用
  protected var age: Int = 18
  // 在 object_oriented 包下的所有类都可以使用
  private[object_oriented] var sex: String = "男"

  def say(): Unit = {
    println(name)
  }

}
