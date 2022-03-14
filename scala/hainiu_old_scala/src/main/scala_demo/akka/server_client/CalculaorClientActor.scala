package akka.server_client

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

import scala.io.StdIn

class CalculaorClientActor(val serverHost:String, val serverPort:Int) extends Actor{

  var serverRef: ActorSelection = _

  override def preStart(): Unit = {
    // context.actorSelection 是查找akka地址，获取serverRef对象
    // 这个方法只是获取，如果没有并不创建也不校验
    serverRef = context.actorSelection(s"akka.tcp://server_actor_sys@${serverHost}:${serverPort}/user/server")
  }

  def receive: Receive = {
    case "start" => {
      println("client actor receive => start")
      serverRef ! "计算开始"
    }

    case SendClientMsg(data) =>{
      println(s"client actor receive => ${data}")
      val arr: Array[String] = data.split(" ")
      if(arr.length != 3){
        println("从本地发送过来的数据格式错误")
      }else{
        val num1: Int = arr(0).toInt
        val symbol:String = arr(1)
        val num2: Int = arr(2).toInt
        serverRef ! Client2ServerMsg(num1,symbol,num2)
      }
    }

    case Server2ClientMsg(result) =>{
      println(s"client actor receive => 对比:${result}")
    }

  }
}

object CalculaorClientActor{
  def main(args: Array[String]): Unit = {
    val host = "127.0.0.1"
    val port = 8889
    val serverHost:String = "127.0.0.1"
    val serverPort:Int = 8888

    // 解析配置参数
    val config:Config = ConfigFactory.parseString(
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = $host
         |akka.remote.netty.tcp.port = $port
      """.stripMargin
    )
    val actorSystem: ActorSystem = ActorSystem("client_actor_sys", config)

    val clientRef: ActorRef = actorSystem.actorOf(Props[CalculaorClientActor](new CalculaorClientActor(serverHost,serverPort)),"client")
    clientRef ! "start"

    while(true){
      // 从控制台接收消息
      val line: String = StdIn.readLine()
      clientRef ! SendClientMsg(line)

      // 自动生成计算
      //val num1: Int = Random.nextInt(1000)
      //val num2: Int = Random.nextInt(1000)
      //val symbol = arr(Random.nextInt(3))
      //val line = s"${num1} ${symbol} ${num2}"
      //clientRef ! SendClientMsg(line)
      //Thread.sleep(2000)
    }

  }
}