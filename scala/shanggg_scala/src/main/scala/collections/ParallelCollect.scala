package collections

import scala.collection.parallel.immutable.ParSeq

object ParallelCollect {
  def main(args: Array[String]): Unit = {
    //    val result1: Seq[String] = (0 to 100).map(_ => Thread.currentThread.getName)
    //    val result2: ParSeq[String] = (0 to 100).par.map(_ => Thread.currentThread.getName)
    //    println(result1)
    //    println(result2)

    val list: List[Int] = List(1, 5, 6, 8, 7)
    list.par.foreach(x => println(Thread.currentThread().getName))

  }
}
