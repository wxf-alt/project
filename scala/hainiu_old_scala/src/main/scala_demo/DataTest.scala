import java.text.SimpleDateFormat
import java.util.Date

object DataTest {
  def main(args: Array[String]): Unit = {
    // 前七天
    var date = new Date(); //获取系统当前时间
    println(date)
    /**
     * yyyy：年
     * MM：月
     * dd：日
     * hh：1~12小时制(1-12)
     * HH：24小时制(0-23)
     * mm：分
     * ss：秒
     * S：毫秒
     * E：星期几
     * D：一年中的第几天
     * F：一月中的第几个星期(会把这个月总共过的天数除以7)
     * w：一年中的第几个星期
     * W：一月中的第几星期(会根据实际情况来算)
     * a：上下午标识
     * k：和HH差不多，表示一天24小时制(1-24)。
     * K：和hh差不多，表示一天12小时制(0-11)。
     * z：表示时区
     */
//    val format: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss:S E")
//    println(format.format(date))
//    var date2 = new Date(date.getTime() - (5 * 24 * 60 * 60 * 1000));
//    println(format.format(date2))


    val dt: String = "2021-01-10"
    val date1: Date = new Date()
    val sdf1: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val sdf2: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")

//    val date1: Date = sdf1.parse(dt)
    val oracleDate: Date = sdf1.parse(dt)
    val hiveDate: String = sdf2.format(oracleDate)
//    println(oracleDate)
//    println(hiveDate)

    println("=================================")
    val str: String = sdf2.format(date1)
    val date2: Date = sdf2.parse(str)
//    val date2: Date = sdf1.parse(str)
    println(date1)
    println(str)
    println(date2)

  }
}
