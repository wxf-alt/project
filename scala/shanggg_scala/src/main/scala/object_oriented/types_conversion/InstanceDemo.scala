package object_oriented.types_conversion

class InstanceDemo{}

// 类型转换
object InstanceDemo {
  def main(args: Array[String]): Unit = {
    val instanceDemo: InstanceDemo = new InstanceDemo

    //（1）判断对象是否为某个类型的实例
    val bool: Boolean = instanceDemo.isInstanceOf[InstanceDemo]
    if (bool) {
      //（2）将对象转换为某个类型的实例
      val instanceDemo1: InstanceDemo = instanceDemo.asInstanceOf[InstanceDemo]
      println(instanceDemo1)
    }

    //（3）获取类的信息
    val pClass: Class[InstanceDemo] = classOf[InstanceDemo]
    println(pClass)
  }
}
