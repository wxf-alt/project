package test

import java.sql.Date
import java.text.SimpleDateFormat
import java.util
import java.util.Calendar

//noinspection TypeAnnotation
object test1 {
  def main(args: Array[String]): Unit = {

    //    Logger.getLogger("org").setLevel(Level.ERROR)
    //
    //    val ss: SparkSession = SparkSession.builder().master("local[*]").getOrCreate()
    //
    //    import ss.implicits._
    //    val sc: SparkContext = ss.sparkContext
    //
    //    val df = sc.parallelize(Seq(Row("Alice1", 5, 80), Row("Alice2", 5, 80), Row("Alice3", 10, 80))).toDF()
    //
    //    df.show()
    //    df.dropDuplicates().show()
    //    df.dropDuplicates("age").show()

    //    val ss = SparkSession.builder()
    //      .appName("test")
    //      .master("local[*]")
    //      .getOrCreate()
    //    import ss.implicits._
    //    val a = Seq(("lky","nan","28")
    //      ,("csx","nan","27")
    //    )
    //    val b = Seq(("csx","nan","27")
    //      ,("xh","nan","26")
    //    )
    //    val context: SparkContext = ss.sparkContext
    //    val bRdd = context.parallelize(b).toDF
    //    val aRdd = context.parallelize(a).toDF
    //    val test1: Dataset[Row] = aRdd.except(bRdd)
    ////    val test2 = bRdd.except(aRdd)
    ////    val test3 = aRdd.intersect(bRdd)
    //    test1.show(20)


    //    val array: Array[String] = new Array[String](0)
    //    val strings1: Array[String] = array:_*
    //
    //    val code: Array[String] = Array("ORG_CODE")
    //    val strings2: Array[String] = code:_*
    //
    //    println(strings1)
    //    println(strings2)

    //
    //    val array: Array[String] = Array("as", "sadsaff", "afs")
    //    println(array: _*)

    //    val today: Date = new Date(System.currentTimeMillis())
    //    val begDate: Date = new Date(today.getTime - 1 * 24 * 60 * 60 * 1000)
    //    val month = new java.util.Date(begDate.getYear, begDate.getMonth, 1)
    //    val sdf = new SimpleDateFormat("yyyyMM")
    //    val sdf1 = new SimpleDateFormat("yyyyMMdd")
    //    val stat_date = sdf.format(month)
    //    val stat_date1 = sdf1.format(month)
    //    val sql: String = s"delete from ocean_stat.a_upgrade_stat_m  where stat_date = to_date('${stat_date}01,'yyyyMMdd')"
    //    val sql1: String = s"delete from ocean_stat.a_upgrade_stat_m  where stat_date = to_date('${stat_date}',yyyyMMdd')"
    //    val sql2: String = s"delete from ocean_stat.a_upgrade_stat_m  where stat_date = to_date('${stat_date1}01,'yyyyMMdd')"
    //    val sql3: String = s"delete from ocean_stat.a_upgrade_stat_m  where stat_date = to_date('${stat_date1}',yyyyMMdd')"
    //    println(today)
    //    println(begDate)
    //    println(month)
    //    println(sql)
    //    println(sql1)
    //    println(sql2)
    //    println(sql3)

    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")

    //        val date1: Date = new java.sql.Date(System.currentTimeMillis())
    ////    val date1: Date = new java.sql.Date(System.currentTimeMillis() -1 * 24 * 60 * 60 * 1000)
    //    println(date1)
    ////        val str: String = calcfirstdayweek(date1)
    ////        val str1: String = calclastdayweek(date1)
    //    val str: String = calcFirstdayLastweek(date1)
    //    val str1: String = calcLastdayLastweek(date1)
    //    println("所在周 星期一:" + str)
    //    println("所在周 星期日:" + str1)
    //
    //    val date2: Date = sdf.parse(str)
    //    val edDay: Date = new java.util.Date(date2.getTime + 6 * 24 * 60 * 60 * 1000)
    //    println("周日:" + edDay)


    val date1: Date = new java.sql.Date(System.currentTimeMillis())
    println(calcdayfirst(calcnmonth(date1, -1)))


  }

  def calcfirstdayweek(date: java.util.Date): String = {
    val cal: Calendar = Calendar.getInstance()
    cal.setTime(date)
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    new SimpleDateFormat("yyyyMMdd").format(cal.getTime)
  }

  def calclastdayweek(date: java.util.Date): String = {
    val cal: Calendar = Calendar.getInstance()
    cal.setTime(date)
    cal.add(Calendar.DATE, 7)
    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    new SimpleDateFormat("yyyyMMdd").format(cal.getTime)
  }

  def calcFirstdayLastweek(date: java.util.Date): String = {
    val cal: Calendar = Calendar.getInstance
    cal.setTime(date)
    // 将每周第一天设为星期一，默认是星期天
    cal.setFirstDayOfWeek(Calendar.MONDAY)
    cal.add(Calendar.DATE, -1 * 7);
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    new SimpleDateFormat("yyyyMMdd").format(cal.getTime)
  }

  def calcLastdayLastweek(date: java.util.Date): String = {
    val cal: Calendar = Calendar.getInstance
    cal.setTime(date)
    //将每周第一天设为星期一，默认是星期天
    cal.setFirstDayOfWeek(Calendar.MONDAY)
    cal.add(Calendar.DATE, -1 * 7);
    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    new SimpleDateFormat("yyyyMMdd").format(cal.getTime)
  }


  def calcdayfirst(date: java.util.Date): String = {
    val cal: Calendar = Calendar.getInstance()
    cal.setTime(date)
    cal.set(Calendar.DAY_OF_MONTH, 1)
    new SimpleDateFormat("yyyyMMdd").format(cal.getTime)

  }

  def calcnmonth(date: java.util.Date, range: Int = 0): util.Date = {
    val cal: Calendar = Calendar.getInstance()
    cal.setTime(date)
    cal.add(Calendar.MONTH, range)
    cal.getTime
  }

}
