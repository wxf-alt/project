package object_oriented.package_demo

class PackageAccessRights2 extends PackageAccessRights{
  def test(): Unit ={
    // protected 修饰
    this.age
    // private[object_oriented] 修饰
    this.sex
  }
}

class Animal{
  def test(): Unit ={
    // private[object_oriented] 修饰
    new PackageAccessRights().sex
  }
}
