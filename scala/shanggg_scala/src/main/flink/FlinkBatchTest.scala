import org.apache.flink.api.scala._

/**
 * @Auther: wxf
 * @Date: 2022/5/30 16:45:40
 * @Description: Flink 批处理
 * @Version 1.0.0
 */
object FlinkBatchTest {
  def main(args: Array[String]): Unit = {
    // 创建 Flink 执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val path: String = this.getClass.getClassLoader.getResource("word.txt").getPath
    //    println(path)
    val inputStream: DataSet[String] = env.readTextFile(path)
    val resultDataSet: AggregateDataSet[(String, Int)] = inputStream
      .flatMap(_.split(" "))
      .map((_, 1))
      .groupBy(0)
      .sum(1)

    resultDataSet.print()

  }
}
