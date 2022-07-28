package spark_streaming.kafka_offset.SimpleConsumerCase

import java.{lang, util}

import kafka.api.{OffsetRequest, PartitionMetadata, PartitionOffsetRequestInfo, TopicMetadata, TopicMetadataRequest, TopicMetadataResponse}
import kafka.common.TopicAndPartition
import kafka.consumer.SimpleConsumer
import org.I0Itec.zkclient.ZkClient
import org.apache.kafka.common.TopicPartition
import org.apache.zookeeper.data.Stat
import org.apache.zookeeper.{WatchedEvent, Watcher, ZooKeeper}

import scala.collection.mutable



object GetTopicPartitionOffsets {

  class MyZookeeper() extends Watcher {
    val zkQuorum: String = "nn1.hadoop:2181,nn2.hadoop:2181,s1.hadoop:2181"

    val zk: ZooKeeper = new ZooKeeper(zkQuorum, 20000, this)

    @Override
    def process(event: WatchedEvent) {}
  }

  def main(args: Array[String]): Unit = {
    val res3: util.Map[TopicPartition, lang.Long] = getPartitionOffset(new MyZookeeper().zk, "/consumers/group2/offsets/hainiu_test")
    println(res3)
  }


  def findLeader(seedBrokers: List[(String, Int)], topic: String): mutable.Map[Int, PartitionMetadata] = {
    val map: mutable.Map[Int, PartitionMetadata] = mutable.Map[Int, PartitionMetadata]()
    // 遍历每个节点机器
    for (seed <- seedBrokers) {
      var consumer: SimpleConsumer = null;
      try {
        consumer = new SimpleConsumer(seed._1, seed._2, 100000, 64 * 1024,
          "leaderLookup" + new java.util.Date().getTime);
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
            map += (part.partitionId -> part)  // part.getClass  kafka.api.PartitionMetadata
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

  /**
   * 获得主题下各个分区的最早和最新offset
   * @param topic
   * @param bootstrap
   * @param where
   * @return
   */
  def getTopicOffsets(topic: String, bootstrap: String, where: String): java.util.Map[TopicPartition, java.lang.Long] = {
    val clientId: String = ""
    var brokers: List[(String, Int)] = List[(String, Int)]()
    var hostAry: Array[String] = bootstrap.split(",");
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
    val metas: mutable.Map[Int, PartitionMetadata] = findLeader(brokers, topic)  // Map("分区" -> 分区元数据)
    val ret: util.HashMap[TopicPartition, lang.Long] = new java.util.HashMap[TopicPartition, java.lang.Long]()
    //遍历每个分区
    metas.keys.foreach(f => {
      val meta: PartitionMetadata = metas(f)
      meta.leader match {  // Some(BrokerEndPoint(79,cloudera03,9092)))
        case Some(leader) => {
          var consumer: SimpleConsumer = null
          try {
            // 发送请求给每个分区的leader
            consumer = new SimpleConsumer(leader.host, leader.port, 10000, 100000, clientId)
            val topicAndPartition = TopicAndPartition(topic, f)
            var request: OffsetRequest = null
            // 得到每个分区最早的offset和最新的offset
            if (where.equals("earliest")) {
              request = OffsetRequest(Map(topicAndPartition -> PartitionOffsetRequestInfo(OffsetRequest.EarliestTime, 1)))
            } else if (where.equals("latest")) {
              request = OffsetRequest(Map(topicAndPartition -> PartitionOffsetRequestInfo(OffsetRequest.LatestTime, 1)))
            }
            val offsets = consumer.getOffsetsBefore(request).partitionErrorAndOffsets(topicAndPartition).offsets
            ret.put(new TopicPartition(topic, f), new java.lang.Long(offsets.head))  // offsets.head 0  30
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
    ret  // {test_gp-0=0, test_gp-1=0}
  }

  def getPartitionOffset(zk: ZooKeeper, path: String): java.util.Map[TopicPartition, java.lang.Long] = {
    // 如果path不存在直接返回空
    if (zk.exists(path, false) == null) {
      null
    } else {
      val children: java.util.List[java.lang.String] = zk.getChildren(path, false)
      // 遍历zk上每个分区的offset值
      if (children != null && children.size() > 0) {
        val topic: String = path.split("/")(4)  // /test/monitor/test_gp/kafka_operation_group
        // 获取最早的offset
        val earliestOffsets: util.Map[TopicPartition, lang.Long] = getTopicOffsets(
          topic,
          "s2.hadoop:9092,s3.hadoop:9092,s4.hadoop:9092",
          "earliest")
        println("获取最早的offset: ", earliestOffsets)  

        // 获取最新的offset
        val latestOffsets: util.Map[TopicPartition, lang.Long] = getTopicOffsets(
          topic,
          "s2.hadoop:9092,s3.hadoop:9092,s4.hadoop:9092",
          "latest")
        println("获取最新的offset: ", latestOffsets)  
        // zk的节点+1的结果为{test_gp-0=17, test_gp-1=18}
        // 两者不一致，可能之前消费又漏写如zk的，理论上zk的值小于等于broker获得最新offset值

        // 初始化一个Map存储所有分区的offset
        val ret: util.HashMap[TopicPartition, lang.Long] = new java.util.HashMap[TopicPartition, java.lang.Long]()
        // 遍历主题的每一个分区
        for (i <- 0 until children.size) {
          val topicPartition: TopicPartition = new TopicPartition(topic, children.get(i).toInt)  // topicPartition test_gp-0
          // 再往下一层节点，获取节点值+1为新的要拉取的offset
          var offset: lang.Long = new java.lang.Long(getData(zk, path + "/" + children.get(i)))

          // 用从broker端获取的最新offset，最老offset最zk上记录的offset进行修正
          if (offset.longValue() < earliestOffsets.get(topicPartition).longValue()) {
            offset = earliestOffsets.get(topicPartition)
          } else if (offset.longValue() > latestOffsets.get(topicPartition).longValue()) {
            offset = latestOffsets.get(topicPartition)
          }
          ret.put(topicPartition, offset)  // 分区 -> offset
          println("ret: ", ret)
          //把存在的都删掉
          earliestOffsets.remove(topicPartition)
        }

        //把不存在的 再添加进去, 防止zk中有节点没有值
        ret.putAll(earliestOffsets)
        ret
      } else {
        null
      }
    }
  }

  def getData(zk: ZooKeeper, path: String): String = {
    val stat: Stat = zk.exists(path, false)
    if (stat == null) {
      null
    } else {
      val zkQuorum: String = new MyZookeeper().zkQuorum
//      new String(zk.getData(path, false, stat))
      new ZkClient(zkQuorum).readData(path)
    }
  }

}
