package source

import org.apache.flink.streaming.api.scala._

/**
 * @Auther: wxf
 * @Date: 2022/6/7 17:54:08
 * @Description: 从文件读取数据
 * @Version 1.0.0
 */
object TextSource {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val path: String = this.getClass.getClassLoader.getResource("word.txt").getPath
    val inputStream: DataStream[String] = env.readTextFile(path)
    inputStream.print("input1:").setParallelism(1)
    env.execute("TextSource")
  }
}
