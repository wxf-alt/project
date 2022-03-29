package match_case

// match case 匹配Option
object MatchCaseOptionDemo {
  def main(args: Array[String]): Unit = {
    val p: People = new People(10, "a")
    p match {
      case People(age, name) => println(age, name)
      case _ => println("")
    }
  }
}

class People(val age: Int, val name: String)

object People {
  def unapply(arg: People): Option[(Int, String)] = if (arg != null) Some((arg.age, arg.name)) else None
}