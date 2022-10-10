//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hive.ql.io.orc.OrcNewInputFormat;
//import org.apache.hadoop.hive.ql.io.orc.OrcStruct;
//import org.apache.hadoop.io.NullWritable;
//import org.apache.spark.Accumulator;
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.api.java.function.Function;
//import org.apache.spark.broadcast.Broadcast;
//import scala.Tuple2;
//import utils.OrcFormat;
//import utils.OrcUtil;
//import utils.Utils;
//
//import java.io.*;
//import java.util.HashMap;
//
//import static utils.OrcFormat.COUNTRY;
//
//public class MapJoin2Demo {
//    public static void main(String[] args) throws IOException {
//        SparkConf conf = new SparkConf();
//        conf.setMaster("local[*]");
//        conf.setAppName("MapJoin2Demo");
//        JavaSparkContext sc = new JavaSparkContext(conf);
//        Configuration hadoopConf = new Configuration();
//        // orc 文件位置
//        String path = "E:\\A_data\\4.测试数据\\输入\\mapjoin";
//        // 读取hdfs上的文件，转换成相应的 RDD
//        JavaPairRDD<NullWritable, OrcStruct> orcPairRdd = sc.newAPIHadoopFile(path,
//                OrcNewInputFormat.class, NullWritable.class, OrcStruct.class, hadoopConf);
//        // 加载字典文件
//        InputStream inputStream = ClassLoader.getSystemResourceAsStream("country_dict.dat");
//        assert inputStream != null;
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        // Map 用来存储 字典文件中的数据
//        HashMap<String, String> hashMap = new HashMap<>();
//        String line = null;
//        if ((line = reader.readLine()) != null) {
//            String[] split = line.split("\t");
//            String code = split[0];
//            String countName = split[1];
//            hashMap.put(code, countName);
//        }
//
//        // 将字典数据封装到广播变量中
//        Broadcast<HashMap<String, String>> broadcast = sc.broadcast(hashMap);
//        // 定义累加器
//        Accumulator<Integer> hasCountryAcc = sc.accumulator(0);
//        Accumulator<Integer> notHasCountryAcc = sc.accumulator(0);
//
//        // RDD 与 字典数据进行 join
//        JavaRDD<String> mapRdd = orcPairRdd.map(new Function<Tuple2<NullWritable, OrcStruct>, String>() {
//            @Override
//            public String call(Tuple2<NullWritable, OrcStruct> v1) throws Exception {
//                // 创建orcUtil对象
//                OrcUtil orcUtil = new OrcUtil();
//                // 设置读取orc文件， 传入指定的schema
//                orcUtil.setOrcTypeReadSchema(OrcFormat.SCHEMA);
//                // 获取数据
//                String countryCode = orcUtil.getOrcData(v1._2, COUNTRY);
//                // 广播变量数据
//                HashMap<String, String> broadcastMap = broadcast.value();
//                String countryName = broadcastMap.get(countryCode);
//                if (Utils.isNotEmpty(countryName)) {
//                    // 统计匹配上的个数
//                    hasCountryAcc.add(1);
//                } else {
//                    // 统计没匹配上的个数
//                    notHasCountryAcc.add(1);
//                }
//                return countryCode + "\t" + countryName;
//            }
//        });
//
//        // 输出数据 并打印
//        for (String s : mapRdd.take(20)) {
//            System.out.println(s);
//        }
//        System.out.println(hasCountryAcc.value());
//        System.out.println(notHasCountryAcc.value());
//    }
//}
