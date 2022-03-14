//package actor
//
//import scala.actors.{Actor, Future}
//import scala.collection.mutable.ListBuffer
//import scala.io.Source
//
//class WordCountActor extends Actor{
//  def act(): Unit = {
//    receive{
//      case file:String =>{
//        println(s"actor receive => ${file}")
//        val lines: List[String] = Source.fromFile(file).getLines().toList
//        val map: Map[String, Int] = lines.flatMap(_.split("\t")).map((_,1)).groupBy(_._1).mapValues(_.size)
//        sender ! map
//      }
//    }
//  }
//
////  override def receive: Receive = ???
//}
//
//object WordCountActor{
//  def main(args: Array[String]): Unit = {
//    val files = Array[String]("E:\\tmp\\scala\\input\\word1.txt",
//      "E:\\tmp\\scala\\input\\word2.txt",
//      "E:\\tmp\\scala\\input\\word3.txt",
//      "E:\\tmp\\scala\\input\\word4.txt")
//
//    // 封装返回数据future
//    val futures = new ListBuffer[Future[Any]]
//
//    // 封装真正的返回数据map集合
//    val maps = new ListBuffer[Map[String,Int]]
//
//    for(file <- files){
//      val actor = new WordCountActor
//      actor.start()
//      val future: Future[Any] = actor !! file
//      futures += future
//    }
//
//    while(futures.size > 0){
//      // 已经有返回数据的future列表
//      val dones: ListBuffer[Future[Any]] = futures.filter(_.isSet)
//      for(done <- dones){
//        val map: Map[String, Int] = done().asInstanceOf[Map[String,Int]]
//        println(s"中间结果：${map}")
//        maps += map
//        // 在futures 列表中删除
//        futures -= done
//      }
//    }
//
//    // 处理maps列表的结果，得到最终的结果
//    val resMap: Map[String, Int] = maps.flatten.groupBy(_._1).mapValues(_.map(_._2).sum)
//    //  val resMap:List[Any] = maps.flatten.groupBy(_._1).mapValues(_.map(_._2).sum).toList.sortBy(_._2).reverse
//    println(resMap)
//
//  }
//}
