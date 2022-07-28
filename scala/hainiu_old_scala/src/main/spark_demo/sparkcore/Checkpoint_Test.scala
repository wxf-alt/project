package sparkcore

import org.apache.spark.{SparkConf, SparkContext}

// 5.checkpoint 检查点操作 --》 当执行action时，才会触发checkpoint
/*
checkpoint的意思就是建立检查点，类似于快照，例如在spark计算里面计算流程DAG特别长，服务器需要将整个DAG计算完成得出结果。但是如果
在这很长的计算流程中突然中间算出的数据丢失了，spark又会根据RDD的依赖关系从头到尾计算一遍,这样子就很费性能，当然我们可以将中间的计算结果
通过cache或者persist放到内存或者磁盘中，但是这样也不能保证数据完全不会丢失，存储的这个内存出问题了或者磁盘坏了，也会导致spark从头再根据RDD计算一遍，所以就有了checkpoint。
checkpoint的作用就是将DAG中比较重要的中间数据做一个检查点将结果存储到一个高可用的地方(通常这个地方就是HDFS里面)
执行在action之后，checkpoint 会忘记血缘关系。并且把数据放到hdfs上。
cache 不会忘记血缘关系，把数据放到内存。
*/
object Checkpoint_Test {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("5- checkpoint 操作").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)

    // 1.设置 checkpoint 目录,用来存储
    sc.setCheckpointDir("C:\\Users\\wxf\\Desktop\\4.测试数据\\检查点")
    val rdd = sc.textFile("C:\\Users\\wxf\\Desktop\\4.测试数据\\输入").flatMap(_.split(" ")).map((_, 1)).reduceByKey(_+_)
    // 2.调用 checkpoint 方法进行存储
    rdd.checkpoint
    println(rdd.isCheckpointed)
    rdd.count //   **  当执行action时，才会触发checkpoint
    println(rdd.isCheckpointed)
    // 获取 checkpoinit 文件目录
    println(rdd.getCheckpointFile.getOrElse("0"))

  }
}
