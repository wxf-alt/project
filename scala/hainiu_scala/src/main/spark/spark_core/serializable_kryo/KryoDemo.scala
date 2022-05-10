package spark_core.serializable_kryo

import com.esotericsoftware.kryo.Kryo
import com.twitter.chill.KryoSerializer
import org.apache.hadoop.io.Text
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.serializer.{KryoRegistrator, KryoSerializer}
import org.apache.spark.{SparkConf, SparkContext}

class MyRegistrator extends KryoRegistrator {
  override def registerClasses(kryo: Kryo): Unit = {
    kryo.register(Class.forName(classOf[UserInfo].getName))
    kryo.register(Class.forName(classOf[Text].getName))
    kryo.register(classOf[Array[Int]])
    kryo.register(Class.forName("scala.collection.mutable.WrappedArray$ofInt"))
    kryo.register(Class.forName("scala.reflect.ClassTag$$anon$1"))
    kryo.register(Class.forName("java.lang.Class"))
  }
}

object KryoDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("KryoDemo")
    // 开启 Kryo序列化
    conf.set("spark.serializer", classOf[KryoSerializer].getName)
    // 设置主动注册
    conf.set("spark.kryo.registrationRequired", "true")
//    // 方式2
//    conf.set("spark.kryo.registrator",classOf[MyRegistrator].getName)

//    // 方式1
//    val classes: Array[Class[_]] = Array[Class[_]](classOf[UserInfo]
//      , classOf[Text]
//      , Class.forName("scala.collection.mutable.WrappedArray$ofInt")
//      , classOf[Array[Int]]
//      , Class.forName("scala.reflect.ClassTag$$anon$1")
//      , Class.forName("java.lang.Class")
//    )
//    //     将上面的类注册
//    conf.registerKryoClasses(classes)

//    // 方式三
//    // 如果要使用 这种方式那么;应该设置: conf.set("spark.kryo.registrationRequired", "false") 或者去掉 conf中的set 默认是 false
    conf.registerKryoClasses(Array(classOf[UserInfo]))

    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[Int] = sc.parallelize(List(1,1,2,2,3,3),2)
    // java 对象
    val user: UserInfo = new UserInfo
    val broad: Broadcast[UserInfo] = sc.broadcast(user)
    val pairRdd: RDD[(Int, UserInfo)] = rdd.map(f => {
      val userInfo: UserInfo = broad.value
      (f, userInfo)
    })
    // groupByKey 是为了测试序列化
    val map: collection.Map[Int, Iterable[UserInfo]] = pairRdd.groupByKey().collectAsMap()
    for(f <- map){
      println(f)
    }
  }
}
