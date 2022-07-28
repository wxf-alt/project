package transform

import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala._

import scala.util.Random

/**
 * @Auther: wxf
 * @Date: 2022/6/9 11:46:23
 * @Description: UnionTest
 * @Version 1.0.0
 */
object UnionTest {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    //
    //    val inputStream: DataStream[String] = env.socketTextStream("localhost", 6666)
    //    val inputStream1: DataStream[String] = env.socketTextStream("localhost", 7777)
    //

    val inputStream: DataStream[String] = env.addSource(new MySource())
    val inputStream1: DataStream[String] = env.socketTextStream("localhost", 7777)
    val unionStream: DataStream[String] = inputStream.union(inputStream1)

    unionStream.print("UnionTest")

    env.execute("UnionTest")

  }
}

class MySource extends SourceFunction[String] {

  private var flag: Boolean = true
  private val random: Random = new Random(10)

  override def run(ctx: SourceFunction.SourceContext[String]): Unit = {
    while (flag) {
      val random: Int = new Random().nextInt(101)
      val timeSteamp: Long = System.currentTimeMillis()
      val random1: Int = new Random().nextInt(51)
      ctx.collect(random.toString + "\t" + timeSteamp + "\t" + random1)
      Thread.sleep(1000)
    }
  }

  override def cancel(): Unit = flag = false
}
