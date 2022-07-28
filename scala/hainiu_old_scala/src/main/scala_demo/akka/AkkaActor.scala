package akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class AkkaActor extends Actor{
  def receive: Receive = {
    case "start" => println("starting....")
    case "hello" => println("actor recieve ==> hello")
    case "stop this" =>{
      println("actor recieve ==> stop this")
      // self 代表当前actor自己
      // 关闭自己
      context.stop(self)
    }
    case "test" => println("actor recieve ==> test")
    case "stop all" => {
      println("actor recieve ==> stop all")
      // 关闭所有
//      context.system.terminate()
    }
  }
}

// 发送给自己0 测试
object AkkaActor{
  def main(args: Array[String]): Unit = {
    // 先创建ActorSystem对象
    val actorSystem = ActorSystem("hello_akka_sys")
    // 通过actorOf 来创建actor对象，并返回对应的ActorRef对象
    // akka://hello_akka_sys/user/hello
    val helloRef: ActorRef = actorSystem.actorOf(Props[AkkaActor], "hello")
    // 发送异步消息
    helloRef ! "start"
    helloRef ! "hello"
    //    helloRef ! "stop this"
    //    Thread.sleep(2000)
    //    helloRef ! "text"
    helloRef ! "stop all"

  }
}