package spark_core.transformation_test.rdd

import java.text.SimpleDateFormat
import java.util.Date

/**
 * @Auther: wxf
 * @Date: 2022/11/14 11:48:16
 * @Description: TestMapPartition
 * @Version 1.0.0
 */
object TestMapPartition {

  def getEMpCurve(str: String, date: Date, dateFormat: SimpleDateFormat): Long = {

    var REC_TIME: String = str
    REC_TIME = if (REC_TIME.matches("[0-9]*") && REC_TIME.size <= 8) {
      (REC_TIME + "00000000000000").substring(0, 14)
    } else if (REC_TIME.matches("[0-9]*") && REC_TIME.size > 8) {
      REC_TIME.substring(0, 14)
    } else {
      "19700101000000"
    }

    println("REC_TIME：" + REC_TIME)

    val REC: Long = dateFormat.parse(REC_TIME).getTime
    REC
  }

}