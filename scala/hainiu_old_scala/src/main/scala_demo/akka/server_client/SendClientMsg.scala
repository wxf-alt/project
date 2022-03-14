package akka.server_client

// 封装本地发送给ClientActor的消息
case class SendClientMsg(val data:String){
  override def toString: String = s"${data}"
}

// 封装Client发送给Server的消息
case class Client2ServerMsg(val num1:Int, val symbol: String, val num2:Int){
  override def toString: String = s"${num1}${symbol}${num2}"
}

// 封装Server返回给Client的消息
case class Server2ClientMsg(val result:String){
  override def toString: String = s"${result}"
}