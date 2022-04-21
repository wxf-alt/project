package sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}


// 测试 sparkStreaming 的 window
object SparkStreamingSocketWindows {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreamingSocketWindows")
    val checkPointPath: String = "/Users/leohe/Data/output/SparkStreamingSocketWindows"

    val ssc: StreamingContext = new StreamingContext(conf, Durations.seconds(5))
    //使用updateStateByKey必须得设置一个checkPoint地址用于保存历史的数据
    ssc.checkpoint(checkPointPath)

    //在有checkpoint数据的时候，修改地址和端口是不生效的
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 6666)
    val flatMap: DStream[String] = lines.flatMap(_.split(" "))
    val mapToPair: DStream[(String, Int)] = flatMap.map((_, 1))

    //这个是使用磁盘存储windows的数据，所以必须设置checkpoint的地址，并直接返回统计好的值
    //为什么使用磁盘存储windows的数据呢？因为进入这个windows是没有合并结果的，是原始数据，所以数据量比较大，使用内存的风险比较高
    //    val reduceByKey: DStream[(String, Long)] = flatMap.countByValueAndWindow(Durations.seconds(20),Durations.seconds(10))

    //这个是使用了内存保存windows的数据，不用设置一个checkPoint地址，使用了DStream的countByValue()代替了reduceByKey，返回统计好的值
    //但是这是不建议的写法？为什么呢？因为这种写法相当于咱们把没有合并的结果放到了内存里面导致内存占用比较大
    //    val reduceByKey: DStream[(String, Long)] = flatMap.window(Durations.seconds(20),Durations.seconds(10)).countByValue()

    //这是正确的写法，既然windows使用了内存，所以就得考虑内存使用的大小，那自然要将小的数据，也就是合并之后的结果放到windows中
    //那咱们总结一下sparkStreaming使用内存的方法有那些呢？为什么要进行这个总结呢，因为sparkStreaming的24H运行的，所以内存的使用关系其稳定性
    //那sparkSteaming使用内存的方法有，比如DStream的cache,persist,windows还有streamingContext.remember()
    //还有就是咱们自己在foreachRDD/Tranform中把你的RDD拿出来然后进行cache,persist
    //注意：DStream的cache默认的级别是MEMORY_ONLY_SER，因为SER可以减少缓存数据的大小，更利于流式程序的稳定
    val reduceByKey: DStream[(String, Int)] = mapToPair.reduceByKey(_ + _).window(Durations.seconds(20),Durations.seconds(10))
    //当没有设置划动间隔的时候，那划动间隔默认与批次间隔相同

    //transform和foreachRDD都可以进行RDD转换，比如RDD转成DS或DF进行spark-sql的操作，这时就方便多了
    //但是要记住tranform是转换操作，而foreachRDD是动作，这两个的共同特点是可以让写spark-streaming程序像写spark-core一样
    //然后这两个算子还有一个特点，就是其function中的代码运行的时候分为本地端和集群端运行
    //DStream自己也有和RDD一样的filter,map算子，并且有自己独有的比如windows,updateStateByKey这样的算子
    //但是DStream转换的函数返回的是别一个DStream，所以不像transform和foreachRDD的函数可以得到原始的RDD，所以普通的DStream算子无法进行spark-sql的运算
    //注意：目前来看只有这两个transform和foreachRDD算子里面的函数中的代码分为"本地"和"集群"运算的
    //而DStream的其它算子的函数中的代码是不分本地和集群的，因为他们都是在集群中运行的
    //    reduceByKey.foreachRDD(r => {
    //      val flatMap: RDD[String] = r.flatMap(_.split(" "))
    //      val mapToPair: RDD[(String, Int)] = flatMap.map((_, 1))
    //      val reduceByKey: RDD[(String, Int)] = mapToPair.reduceByKey(_ + _)
    //
    //      import sparkSession.implicit._
    //      val df:DataFrame = reduceByKey.toDF
    //      df.createOrReplaceTempView("test")
    //      val df1:DataFrame = sparkSession.sql("select * from test")
    //      val rdd: RDD[test.Row] = df1.rdd
    //    })

    val updateStateByKey: DStream[(String, Int)] = reduceByKey.updateStateByKey(
      //这个参数a是本批次的数据，b是这个key上次的历史结果
      (a: Seq[Int], b: Option[Int]) => {
        var total = 0
        for (i <- a) {
          total += i
        }

        //这里为什么要判断一下，因为这个key有可能是第一次进入，也就是说没有历史数据，那此时应该给个初始值0
        val last = if (b.isDefined) b.get else 0
        val now = last + total
        Some(now)
      })

    //在有checkpoint数据的时候，修改算子中的function是有效的
    //所以使用了checkpoint恢复streamingContext的程序，这样的程序并不影响你后续的升级代码
    updateStateByKey.foreachRDD((r, t) => {
      //可以用rdd的isEmpty方法，判断此批次是否有数据，如果有数据再执行
      println(s"count time:${t},${r.collect().toList}")
    })

    ssc.start()
    ssc.awaitTermination()


  }
}
