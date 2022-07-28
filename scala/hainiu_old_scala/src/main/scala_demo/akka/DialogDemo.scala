package akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class BoyActor(val girlRef:ActorRef) extends Actor{
  def receive: Receive = {
    case "start" => {
      println("boy actor receive => start, boy send 美女踩我脚了")
      girlRef ! "美女踩我脚了"
    }

    case "谁啊" => {
      println("boy actor receive => 谁啊, send 你啊")
      girlRef ! "你啊"
    }

    case "咋滴啦" => {
      println("boy actor receive => 咋滴啦, boy send 美女踩我脚了")
      girlRef ! "美女踩我脚了"
    }
  }
}


class GirlActor extends Actor{
  def receive: Receive = {
    case "美女踩我脚了" => {
      println("girl actor receive => 美女踩我脚了, send 谁啊")
      Thread.sleep(1000)
      sender() ! "谁啊"
    }

    case "你啊" => {
      println("girl actor receive => 你啊, send 咋滴啦")
      sender() ! "咋滴啦"
    }
  }
}

// 发送给本机的其它线程
object DialogDemo {
  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("dialog_actor_sys")
    // 创建GirlActor，并返回对应的ref
    // akka://dialog_actor_sys/user/girl
    val girlRef: ActorRef = actorSystem.actorOf(Props[GirlActor], "girl")
    // akka://dialog_actor_sys/user/boy
    val boyRef: ActorRef = actorSystem.actorOf(Props[BoyActor](new BoyActor(girlRef)), "boy")
    boyRef ! "start"

  }
}
