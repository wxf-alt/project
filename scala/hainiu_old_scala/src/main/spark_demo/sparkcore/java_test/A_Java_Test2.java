package sparkcore.java_test;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

// java(lambda) 版本的 saprk wordcount
public class A_Java_Test2 {
    // psvm
    public static void main(String[] args) {
        // 先创建sparkConf对象
        SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkWordCountForJava");
        // 根据配置初始化SparkContext对象，java版是JavaSparkContext。
        // SparkContext 是spark的核心
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        // 通过读取hdfs文件来创建rdd对象
        // rdd的每个元素代表对应hdfs的每行数据组成的字符串
        // 比如：aa bb aa
        JavaRDD<String> rdd = jsc.textFile("C:\\Users\\wxf\\Desktop\\4.测试数据\\输入");
        // aa bb aa ---> aa,bb,cc
        // 第一个参数： 每一行的数据组成的字符串
        // 第二个参数： 每个单词
        JavaRDD<String> flatMapRdd = rdd.flatMap(s ->{
            String[] arr = s.split(" ");
            List<String> list = Arrays.asList(arr);
            return list.iterator();
        });
        // aa  ---> (aa,1)
        JavaRDD<Tuple2<String, Integer>> mapRdd = flatMapRdd.map(s -> new Tuple2<String, Integer>(s, 1));
        // (aa,1)(bb,1)(aa,1) ---> aa,List((aa,1),(aa,1)) bb,List((bb,1))
        // 第一个泛型：(aa,1)
        // 第二个泛型：按照哪个字段分组，那个字段的类型
        JavaPairRDD<String, Iterable<Tuple2<String, Integer>>> groupByRdd = mapRdd.groupBy(s -> s._1);
        // aa,List((aa,1),(aa,1)) --> (aa,2)
        // 第一个泛型：每个map的value，比如 ；List((aa,1),(aa,1))
        // 第二个泛型：最后计算的结果， 比如：2
        JavaPairRDD<String, Integer> mapValuesRdd = groupByRdd.mapValues(s ->{
            int sum = 0;
            for (Tuple2<String, Integer> t : s) {
                sum += t._2;
            }
            return sum;
        });
        // 将 executor上的所有最终的rdd数据拉取到driver端
        // collect 是个行动算子
        // 当你用collect时，一定得知道你拉取的数据量，如果拉取的非常多，driver端容易导致内存溢出

        List<Tuple2<String, Integer>> list = mapValuesRdd.collect();
        for(Tuple2<String, Integer> t: list){
            //sout
            System.out.println(t);
        }

    }
}