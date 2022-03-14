package akka.server_client

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

// 定义计算器的serveractor，主要是提供计算功能
class CalculatorServerActor extends Actor{
  def receive: Receive = {
    case "start" => println("server actor receive => start")
    case "计算开始" => println("server actor receive => 计算开始")

    case Client2ServerMsg(num1,symbol,num2) =>{
      println(s"server actor receive => ${num1}${symbol}${num2}")
      var result:String = ""
      symbol match{
        case "+" => result = (num1 + num2).toString
        case "-" => result = (num1 - num2).toString
        case "*" => result = (num1 * num2).toString
        case _ => result = "当前版本只支持+-*运算，待版本升级后再尝试此计算方式"
      }
      println(s"server actor send => ${result}")
      sender() ! Server2ClientMsg(result)
    }
  }
}

object CalculatorServerActor{
  def main(args: Array[String]): Unit = {
    val host = "127.0.0.1"
    val port = 8888
    // 解析配置参数
    val config:Config = ConfigFactory.parseString(
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = $host
         |akka.remote.netty.tcp.port = $port
      """.stripMargin
    )
    val actorSystem: ActorSystem = ActorSystem("server_actor_sys", config)
    // akka.tcp://server_actor_sys@127.0.0.1:8888/user/server
    val serverRef: ActorRef = actorSystem.actorOf(Props[CalculatorServerActor], "server")
    serverRef ! "start"
  }
}

