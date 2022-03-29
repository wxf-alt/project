package object_oriented.traits

// 特质叠加 -> 钻石问题
trait Ball {
  def describe(): String = {
    "ball"
  }
}

trait Color extends Ball {
  override def describe(): String = {
    "blue-" + super.describe()
  }
}

trait Category extends Ball {
  override def describe(): String = {
    "foot-" + super.describe()
  }
}

class MyBall extends Category with Color {
  override def describe(): String = {
    "my ball is a " + super.describe()
  }
}

object TestTrait {
  def main(args: Array[String]): Unit = {
    println(new MyBall().describe())
  }
}