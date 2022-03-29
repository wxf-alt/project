package match_case

// // match case 匹配序列
object MatchCaseSeqDemo {
  def main(args: Array[String]): Unit = {
    val names: String = "lisi,zhangsan,wangwu,zhiling,fengjie"
    names match {
      case MyArray(a,b,rest@_*) =>
        println(a)
        println(b)
        println(rest)
    }
  }
}

object MyArray{
  def unapplySeq(s:String): Option[List[String]] = {
    if(s != null){
      Some(s.split(",").toList)
    }else{
      None
    }
  }
}