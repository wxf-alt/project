//package actor
//
//
//import scala.actors.{Actor, Future}
//
//class ActorDemo3 extends Actor {
//  def act(): Unit = {
//    while(true){
//      receive{
//        case "start" => println("starting....")
//        case "id01" => {
//          println("receive ==> id01, send id02")
//          // 谁给actor发的信息，sender就代表谁
//          sender ! "id02"
//        }
//
//        case SyncMsg(id) => {
//          println(s"receive ==> ${id}, send id04")
//          // 谁给actor发的信息，sender就代表谁
//          sender ! "id04"
//        }
//
//        case AsyncMsgAndReturn(id) => {
//          println(s"receive ==> ${id}, send id06")
//          Thread.sleep(3000)
//          // 谁给actor发的信息，sender就代表谁
//          sender ! "id06"
//        }
//      }
//    }
//  }
//}
//
//object ActorDemo3{
//  def main(args: Array[String]): Unit = {
//    val demo = new ActorDemo3
//    demo.start()
//    // 发送异步无返回消息
//    demo ! "start"
//
//    // 发送同步消息
//    val res1: Any = demo !? "id01"
//    println(s"res1:${res1}")
//
//    val res2: Any = demo !? SyncMsg("id03")
//    println(s"res2:${res2}")
//    // 发送异步有返回的消息时，本地会开辟的空间，等待actor返回消息
//    // 当消息返回就返回到future，等有时间了再来看future里是否有返回数据
//    val future: Future[Any] = demo !! AsyncMsgAndReturn("id05")
//
//    // 通过 future.isSet 判断是否已经返回数据，如果isSet结果是true，代表已返回数据；否则没返回
//    println("在干其他的活,耗时700ms")
//    Thread.sleep(700)
//    while(!future.isSet){
//      Thread.sleep(700)
//      println("等待 700ms")
//    }
//    // 调用 Future 的apply()提取数据
//    val res3: String = future().asInstanceOf[String]
//    println(s"res3:${res3}")
//
//  }
//}
//
//// 封装发送同步有返回消息
//case class SyncMsg(val id:String)
//
//// 封装发送异步有返回消息
//case class AsyncMsgAndReturn(val id:String)
