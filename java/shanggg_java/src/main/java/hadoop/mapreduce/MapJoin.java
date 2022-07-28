package hadoop.mapreduce;

import hadoop.mapreduce.bean.JoinBean2;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class MapJoin {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        // 创建Job
        Configuration conf = new Configuration();
        // 本地
        conf.set("fs.defaultFS","file:///");
        Job job = Job.getInstance(conf);
        job.setJobName("--WordCount--");
        // 设置Job
//        job.setJarByClass(WordCount.class);
        // 设置 Map和Reduce的与运行类
        job.setMapperClass(MyMapJoinMapper.class);
        job.setNumReduceTasks(0);

        // 设置 分布式缓存
        job.addCacheFile(new URI("file:///E:/A_data/4.测试数据/输入/reducejoin/pd.txt"));

        // 设置 Map和Reducer的输入 v-k 类型
        job.setOutputKeyClass(JoinBean2.class);
        job.setOutputValueClass(NullWritable.class);

        // 设置输出输入目录
        Path inputPath = new Path("E:\\A_data\\4.测试数据\\输入\\mapjoin");
        Path outputPath = new Path("E:\\A_data\\4.测试数据\\输出\\mapjoin");
        FileSystem fs = FileSystem.get(conf);
        if(fs.exists(outputPath)){
            fs.delete(outputPath,true);
            System.out.println("输出目录删除成功");
        }
        FileInputFormat.setInputPaths(job,inputPath);
        FileOutputFormat.setOutputPath(job,outputPath);
        // 运行Job
        job.waitForCompletion(true);
    }

    public static class MyMapJoinMapper extends Mapper<LongWritable, Text, JoinBean2, NullWritable> {

        private Map<String,String> pdDatas = new HashMap<>();
        private JoinBean2 orderData = new JoinBean2();

        // 在 map 之前 手动读取 pd.txt
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            // 从分布式缓存中读取数据
            URI[] cacheFiles = context.getCacheFiles();
            for (URI cacheFile : cacheFiles) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(cacheFile)));
                String line = "";
                // 驯化读取数据
                while(StringUtils.isNotBlank(line = bufferedReader.readLine())){
                    String[] split = line.split("\t");
                    pdDatas.put(split[0],split[1]);
                }
                bufferedReader.close();
            }
        }

        // 对 切片中 order.txt 数据进行 join 操作
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split("\t");
            orderData.setOrderId(split[0]);
//            orderData.setPid(split[1]);
            orderData.setPname(pdDatas.get(split[1]));
            orderData.setAmount(split[2]);

            context.write(orderData,NullWritable.get());
        }

    }

}
