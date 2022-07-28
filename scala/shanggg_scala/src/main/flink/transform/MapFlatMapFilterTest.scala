package transform

import org.apache.flink.streaming.api.scala._

/**
 * @Auther: wxf
 * @Date: 2022/6/8 14:40:44
 * @Description: Map,FlatMap,Filter 测试
 * @Version 1.0.0
 */
object MapFlatMapFilterTest {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val inputStream: DataStream[String] = env.socketTextStream("localhost", 6666)

    //    // try-catch  当作 if-else 使用
    //    //    输入的是数字 返回(数字,1)
    //    //    输入的是字符 返回(字符,0)
    //    val mapStream: DataStream[(Any, Int)] = inputStream.map(x => {
    //      var tuple: (Any, Int) = null
    //      try {
    //        val i: Int = Integer.parseInt(x)
    //        tuple = (i, 1)
    //        tuple
    //      } catch {
    //        case a:Exception => tuple = (x,0); tuple
    //      }
    //    })
    //    mapStream.print("map:")


    //    // 拆分一行数据
    //    val flatMapStream: DataStream[String] = inputStream.flatMap(x => x.split(" "))
    //    flatMapStream.print("flatMap:")

    // 只输出带有字母 a 的字符串
    val filterStream: DataStream[String] = inputStream.filter(x => x.contains("a"))
    filterStream.print("filter:")

    env.execute("MapFlatMapFilterTest")
  }
}
