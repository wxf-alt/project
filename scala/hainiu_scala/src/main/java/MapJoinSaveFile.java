//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hive.ql.io.orc.OrcNewInputFormat;
//import org.apache.hadoop.hive.ql.io.orc.OrcNewOutputFormat;
//import org.apache.hadoop.hive.ql.io.orc.OrcStruct;
//import org.apache.hadoop.io.NullWritable;
//import org.apache.hadoop.io.Writable;
//import org.apache.hadoop.io.compress.SnappyCodec;
//import org.apache.spark.Accumulator;
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.api.java.function.PairFunction;
//import org.apache.spark.broadcast.Broadcast;
//import scala.Tuple2;
//import utils.HdfsDelete;
//import utils.OrcFormat;
//import utils.OrcUtil;
//import utils.Utils;
//
//import java.io.*;
//import java.util.HashMap;
//
//import static utils.OrcFormat.COUNTRY;
//
//public class MapJoinSaveFile {
//    public static void main(String[] args) throws IOException {
//        SparkConf conf = new SparkConf();
//        conf.setMaster("local[*]");
//        conf.setAppName("MapJoinSaveFile");
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
//        JavaPairRDD<NullWritable, Writable> orcPairRddW = orcPairRdd.mapToPair(new PairFunction<Tuple2<NullWritable, OrcStruct>, NullWritable, Writable>() {
//            @Override
//            public Tuple2<NullWritable, Writable> call(Tuple2<NullWritable, OrcStruct> v1) throws Exception {
//                // 创建orcUtil对象
//                OrcUtil orcUtil = new OrcUtil();
//                // 设置读取orc文件， 传入指定的schema
//                orcUtil.setOrcTypeReadSchema(OrcFormat.SCHEMA);
//                // 读取country简称
//                String countryCode = orcUtil.getOrcData(v1._2, "country");
//                java.util.Map<String, String> map = broadcast.getValue();
//                String countryName = map.get(countryCode);
//                if (!Utils.isEmpty(countryName)) {
//                    // 统计匹配上的个数
//                    hasCountryAcc.add(1);
//                } else {
//                    // 统计没匹配上的个数
//                    notHasCountryAcc.add(1);
//                }
//                // 将数据序列化成orc格式
//                orcUtil.setOrcTypeWriteSchema("struct<code:string,name:string>");
//                orcUtil.addAttr(countryCode, countryName);
//                Writable w = orcUtil.serialize();
//                return new Tuple2<NullWritable, Writable>(v1._1, w);
//            }
//        });
//        // 设置输出orc文件采用snappy压缩
//        conf.set("orc.compress", SnappyCodec.class.getName());
//        String outPath = "E:\\A_data\\4.测试数据\\输出\\mapjoin";
//        new HdfsDelete(outPath).deletePath();
//        // 将序列化对象写入hdfs
//        orcPairRddW.saveAsNewAPIHadoopFile(outPath,NullWritable.class, Writable.class, OrcNewOutputFormat.class,hadoopConf);
//
//        System.out.println(hasCountryAcc.value());
//        System.out.println(notHasCountryAcc.value());
//    }
//}
