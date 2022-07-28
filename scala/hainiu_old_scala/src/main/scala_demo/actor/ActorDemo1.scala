//package actor
//
//import scala.actors.Actor
//
///*
//1.什么是Actor？
//   Actor是消息并发模型；
//   Scala中的Actor能够实现并行编程的强大功能，它是基于事件模型的并发机制。
//   Scala是运用消息（message）的发送、接收来实现多线程的。
//   使用Scala能够更容易地实现多线程应用的开发。
//Actor是一个封装了状态和行为的对象，Actor之间可以通过交换消息的方式进行通信，每个Actor都有自己的收件箱（MailBox）。
//它每次只能处理一条消息，所以actor内部可以安全的处理状态，而不用考虑锁机制。
//2.Java并发编程与Scala Actor编程的区别
//    Scala的Actor类似于Java中的多线程编程，但是不同的是，Scala的Actor提供的模型与多线程有所不同。
//    Scala的Actor尽可能地避免锁和共享状态，从而避免多线程并发时出现资源争用的情况。进而提升多线程编程的性能。
//    此外，Scala Actor的这种模型还可以避免死锁等一系列传统多线程编程的问题。
//    原因就在于Java中多数使用的是可变状态的对象资源，对这些资源进行共享来实现多线程编程的话，
//    控制好资源竞争与防止对象状态被意外修改是非常重要的，而对象状态的不变性也是较难以保证的。
//    而在Scala中，我们可以通过复制不可变状态的资源（即对象，Scala中一切都是对象，连函数、方法也是）的一个副本，
//    再基于Actor的消息发送、接收机制进行并行编程；
//3.Actor方法执行顺序
//    1）调用 start() 方法启动 Actor；
//    2）执行 act() 方法；
//    3）向 Actor 发送消息；
//4.发送消息的方式
//    !       发送异步消息，没有返回值；
//    !?     发送同步消息，等待返回值；
//    !!      发送异步消息，返回值是Future[Any]；*/
//class ActorDemo1 extends Actor{
//  // 相当于java多线程的run方法体
//  def act(): Unit = {
//    for(i <- 1 to 5){
//      println(s"actordemo1:${i}")
//      Thread.sleep(100)
//    }
//  }
//}
//class ActorDemo2 extends Actor{
//  // 相当于java多线程的run方法体
//  def act(): Unit = {
//    for(i <- 1 to 3){
//      println(s"actordemo2:${i}")
//      Thread.sleep(100)
//    }
//  }
//}
//
//object ActorDemo1{
//  def main(args: Array[String]): Unit = {
//    val demo = new ActorDemo1
//    // 启动actor，相当于java多线的thread.start()
//    demo.start()
//    val demo2 = new ActorDemo2
//    demo2.start()
//  }
//}