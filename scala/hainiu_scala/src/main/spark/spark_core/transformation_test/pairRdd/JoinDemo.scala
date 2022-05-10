package spark_core.transformation_test.pairRdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object JoinDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("JoinDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd1: RDD[(String, String)] =sc.parallelize(List(("id01", "aa"),("id02","bb"),("id03","cc")))
    val rdd2: RDD[(String, Int)] =sc.parallelize(List(("id01", 10),("id03",13),("id04",14)))
    val rdd1JoinRdd2: RDD[(String, (String, Int))] = rdd1.join(rdd2)
    println(rdd1JoinRdd2.collect.toList)  // List((id01,(aa,10)), (id03,(cc,13)))

    val rdd1LeftJoinRdd2: RDD[(String, (String, Option[Int]))] = rdd1.leftOuterJoin(rdd2)
    println(rdd1LeftJoinRdd2.collect.toList)  // List((id01,(aa,Some(10))), (id02,(bb,None)), (id03,(cc,Some(13))))

    val rdd1RightJoinRdd2: RDD[(String, (Option[String], Int))] = rdd1.rightOuterJoin(rdd2)
    println(rdd1RightJoinRdd2.collect.toList)  // List((id01,(Some(aa),10)), (id03,(Some(cc),13)), (id04,(None,14)))
  }
}
