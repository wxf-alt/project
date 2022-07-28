//package spark_stream.integrate_kafka0_8
//
//import kafka.common.TopicAndPartition
//import kafka.message.MessageAndMetadata
//import kafka.serializer.StringDecoder
//import org.apache.kafka.clients.consumer.ConsumerConfig
//import org.apache.spark.SparkConf
//import org.apache.spark.streaming.dstream.{DStream, InputDStream}
//import org.apache.spark.streaming.kafka.KafkaCluster.Err
//import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaCluster, KafkaUtils, OffsetRange}
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//
//object SparkStreamKafkaDemo3 {
//
//  val topics: Set[String] = Set("kafakFirst01")
//  val groupId: String = "idea_spark_kafka "
//  val boostarp: String = "nn1.hadoop:9092,nn2.hadoop:9092,s1.hadoop:9092"
//  val kafkaParams: Map[String, String] = Map[String, String](
//    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> boostarp,
//    ConsumerConfig.GROUP_ID_CONFIG -> groupId
//  )
//  val kafkaCluster: KafkaCluster = new KafkaCluster(kafkaParams)
//
//  // 读取开始的 offset
//  def readOffset(): Map[TopicAndPartition, Long] = {
//    var resultMap: Map[TopicAndPartition, Long] = Map[TopicAndPartition, Long]()
//    // 获取 topic 的所有分区
//    val topicAndPartitionSetEither: Either[Err, Set[TopicAndPartition]] = kafkaCluster.getPartitions(topics)
//    topicAndPartitionSetEither match {
//      // 获取到 topic 和 分区的信息
//      case Right(topicAndPartitionSet) =>
//        // 获取到分区信息和消费的offset
//        val topicAndPartitionToLongEither: Either[Err, Map[TopicAndPartition, Long]] = kafkaCluster.getConsumerOffsets(groupId, topicAndPartitionSet)
//        topicAndPartitionToLongEither match {
//          // 表示每个topic的每个分区都已经存储offset -> 表示曾经消费过,而且也维护过这个偏移量
//          case Right(map) =>
//            resultMap ++= map
//          // 表示这个topic的这个分区是第一次消费
//          case _ =>
//            topicAndPartitionSet.foreach(topicAndPartition => {
//              resultMap += (topicAndPartition -> 0L)
//            })
//        }
//      case _ => // 表示不存在任何 topic
//    }
//    resultMap
//  }
//
//  // 手动维护 offset
//  def saveOffsets(sourceStream: InputDStream[String]): Unit = {
//    sourceStream.foreachRDD(rdd => {
//      var offsets: Map[TopicAndPartition, Long] = Map[TopicAndPartition, Long]()
//      // 如果这个rdd直接来自于 kafka,可以强转成 HasOffsetRanges 类型
//      val hasOffsetRanges: HasOffsetRanges = rdd.asInstanceOf[HasOffsetRanges]
//      // 包含所有分区的偏移量
//      val offsetRanges: Array[OffsetRange] = hasOffsetRanges.offsetRanges
//      offsetRanges.foreach(offsetRange => {
//        val offset: Long = offsetRange.untilOffset
//        val topicAndPartition: TopicAndPartition = offsetRange.topicAndPartition()
//        offsets += (topicAndPartition -> offset)
//      })
//      kafkaCluster.setConsumerOffsets(groupId, offsets)
//    })
//  }
//
//  def main(args: Array[String]): Unit = {
//    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("SparkStreamKafkaDemo3")
//    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))
//    // 只有 这个DStream 有 offset信息
//    val sourceStream: InputDStream[String] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, String](
//      ssc,
//      kafkaParams,
//      readOffset(),
//      (handler: MessageAndMetadata[String, String]) => handler.message()
//    )
//    val result: DStream[(String, Int)] = sourceStream
//      .flatMap(_.split(" "))
//      .map((_, 1))
//      .reduceByKey(_ + _)
//    result.print(1000)
//
//    saveOffsets(sourceStream)
//
//    ssc.start()
//    ssc.awaitTermination()
//  }
//}
