package test

object PNumToString {
  def main(args: Array[String]): Unit = {

    for (i <- 1 to 96) {
      println("p" + i + "\t-->\t" + cas(i))
    }

  }

  def cas(a: Int): String = {

    val num1: Int = a / 4
    val num2: Int = a % 4
    var result: String = ""

    if(a == 1){
      result = "00:00:00"
    }
    else if (a <= 4) {
      val i: Int = (a - 1) * 15
      result = "00:" + i + ":00"
    } else {
      if (num1 < 10) {
        if (num2 == 0) {
          result = "0" + num1 + ":00:00"
        } else {
          result = "0" + num1 + ":" + num2 * 15 + ":00"
        }
      } else {
        if (num2 == 0) {
          result = num1 + ":00:00"
        } else {
          result = num1 + ":" + num2 * 15 + ":00"
        }

      }
    }


    result
  }


}
