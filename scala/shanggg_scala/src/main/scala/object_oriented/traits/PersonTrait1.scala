package object_oriented.traits

// 特质可以多继承
// 特质可以包含抽象与非抽象的属性和方法
// java接口可以当作scala的特质使用
// 可以动态混入( 在 new 对象时 with 特质{方法实现} )
trait PersonTrait1 {
  //（1）特质可以同时拥有抽象方法和具体方法
  // 声明属性
  var name: String = _

  // 抽象属性
  var age: Int

  // 声明方法
  def eat(): Unit = {
    println("eat")
  }

  // 抽象方法
  def say(): Unit
}

trait SexTrait {
  var sex: String
}

//（2）一个类可以实现/继承多个特质
//（3）所有的Java接口都可以当做Scala特质使用
class TestPersonTrait1 extends PersonTrait1 with java.io.Serializable{
  override def say(): Unit = {
    println("say")
  }

  override var age: Int = _
}

object TestPersonTrait1{
  def main(args: Array[String]): Unit = {
    val personTrait: TestPersonTrait1 = new TestPersonTrait1()
    personTrait.say()
    personTrait.eat()

    //（4）动态混入：可灵活的扩展类的功能
    val t2: TestPersonTrait1 with SexTrait = new TestPersonTrait1 with SexTrait {
      override var sex: String = "男"
    }

    //调用混入trait的属性
    println(t2.sex)
  }
}