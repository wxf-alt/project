package spark_streaming.kafka_offset.SimpleConsumerCase

import java.util.Collections
import java.{lang, util}
import kafka.api.{OffsetRequest, PartitionMetadata, PartitionOffsetRequestInfo, TopicMetadata, TopicMetadataRequest, TopicMetadataResponse}
import kafka.common.TopicAndPartition
import kafka.consumer.SimpleConsumer
import org.apache.kafka.common.TopicPartition
import scala.collection.mutable

//
object SimpleConsumerGetOffset {
  def main(args: Array[String]): Unit = {
        val res: util.Map[TopicPartition, lang.Long] = getTopicOffsets("hainiu_test", "s2.hadoop:9092,s3.hadoop:9092,s4.hadoop:9092", "earliest")
        println("最早offset", res)  // (最早offset,{test_gp-0=0, test_gp-1=0})
        val res2: util.Map[TopicPartition, lang.Long] = getTopicOffsets("hainiu_test", "s2.hadoop:9092,s3.hadoop:9092,s4.hadoop:9092", "latest")
        println("最新offset", res2)  // (最新offset,{test_gp-0=30, test_gp-1=30})
  }

  def getTopicOffsets(topic: String, bootstrap: String, where: String): java.util.Map[TopicPartition, java.lang.Long] = {
    val clientId: String = ""
    var brokers: List[(String, Int)] = List[(String, Int)]()
    var hostAry: Array[String] = bootstrap.split(",")

    if (hostAry == null) {
      hostAry = new Array[String](1)
      hostAry(0) = bootstrap;
    }

    // 解析bootstrap为List[host, port]
    for (host <- hostAry) {
      val hostinfo: Array[String] = host.split(":")
      if (hostinfo == null) {
        if (host != null && !host.isEmpty()) {
          brokers = brokers.+:((host, 9092))
        }
      } else {
        if (hostinfo(0).length > 0 && hostinfo(1).length > 0) {
          brokers = brokers.+:((hostinfo(0), Integer.parseInt(hostinfo(1))))
        }
      }
    }

    // 先找到leader broker, 在每个机器节点使用SimpleConsumer发送请求，得到每个分区的元数据
    val metas: mutable.Map[Int, PartitionMetadata] = findLeader(brokers, topic) // Map("分区" -> 分区元数据)
    val ret: util.HashMap[TopicPartition, lang.Long] = new java.util.HashMap[TopicPartition, java.lang.Long]()
    //遍历每个分区
    metas.keys.foreach(f => {
      val meta: PartitionMetadata = metas(f)
      meta.leader match { // Some(BrokerEndPoint(79,cloudera03,9092)))
        case Some(leader) => {
          var consumer: SimpleConsumer = null
          try {
            // 发送请求给每个分区的leader
            consumer = new SimpleConsumer(leader.host, leader.port, 10000, 100000, clientId)
            val topicAndPartition: TopicAndPartition = TopicAndPartition(topic, f)
            var request: OffsetRequest = null
            // 得到每个分区最早的offset和最新的offset
            if (where.equals("earliest")) {
              request = OffsetRequest(Map(topicAndPartition -> PartitionOffsetRequestInfo(OffsetRequest.EarliestTime, 1)))
            } else if (where.equals("latest")) {
              request = OffsetRequest(Map(topicAndPartition -> PartitionOffsetRequestInfo(OffsetRequest.LatestTime, 1)))
            }
            val offsets: Seq[Long] = consumer.getOffsetsBefore(request).partitionErrorAndOffsets(topicAndPartition).offsets
            ret.put(new TopicPartition(topic, f), new java.lang.Long(offsets.head)) // offsets.head 0  30
          } catch {
            case ex: Exception => {
              ex.printStackTrace()
            }
          } finally {
            consumer.close
          }
        }
        case None => {
          System.err.println(Thread.currentThread().getName + "[" + "Error: partition %d does not exist".format(f))
        }
      }
    })
    ret // {test_gp-0=0, test_gp-1=0}
  }

  // 获取 leader broker
  def findLeader(seedBrokers: List[(String, Int)], topic: String): mutable.Map[Int, PartitionMetadata] = {
    val map: mutable.Map[Int, PartitionMetadata] = mutable.Map[Int, PartitionMetadata]()
    // 遍历每个节点机器
    for (seed <- seedBrokers) {
      var consumer: SimpleConsumer = null;
      try {
        consumer = new SimpleConsumer(seed._1, seed._2, 100000, 64 * 1024,
          "leaderLookup" + new java.util.Date().getTime());
        val topics: Array[String] = Array[String](topic);
        // 使用SimpleConsumer发送请求
        val req: TopicMetadataRequest = new TopicMetadataRequest(topics, 0);
        val resp: TopicMetadataResponse = consumer.send(req);
        val metaData: Seq[TopicMetadata] = resp.topicsMetadata
        // 遍历每个分区
        for (item <- metaData) {
          for (part <- item.partitionsMetadata) {
//            println("part.partitionID", part.partitionId)
//            println("part", part)
            map += (part.partitionId -> part) // part.getClass  kafka.api.PartitionMetadata
          }
        }
      } catch {
        case ex: Exception =>
          System.out.println(Thread.currentThread().getName + "[" + "Error communicating with Broker [" + seed + "] to find Leader for [" + topic
            + ", ] Reason: " + ex);
      } finally {
        if (consumer != null)
          consumer.close();
      }
    }
    //    println("最终的map", map)  // Map("分区" -> 分区元数据)
    //    println("最终的map size", map.size)  // 2
    map
  }

}