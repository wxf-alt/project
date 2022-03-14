package hadoop.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

@SuppressWarnings("ALL")
public class WordCount {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 创建Job
        Configuration conf = new Configuration();
        // 本地
        conf.set("fs.defaultFS","file:///");
        Job job = Job.getInstance(conf);
        job.setJobName("--WordCount--");
        // 设置Job
//        job.setJarByClass(WordCount.class);
        // 设置 Map和Reduce的与运行类
        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);

        // 设置 Combiner
        job.setCombinerClass(MyReducer.class);

        // 设置 Map和Reducer的输入 v-k 类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        // 设置输出输入目录
        Path inputPath = new Path("C:\\Users\\wxf\\Desktop\\4.测试数据\\输入\\java_spark测试数据.txt");
        Path outputPath = new Path("C:\\Users\\wxf\\Desktop\\4.测试数据\\输出\\java_mr_out");
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

    private static class MyMapper extends Mapper<LongWritable,Text,Text,LongWritable>{
        private Text out_key = new Text();
        private LongWritable out_val = new LongWritable(1);
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split("\t");
            for (String s : split) {
                out_key.set(s);
                // 写出
                context.write(out_key,out_val);
            }
        }
    }

    private static class MyReducer extends Reducer<Text,LongWritable,Text,LongWritable>{
        private Text out_key = new Text();
        private LongWritable out_val = new LongWritable();
        private Long sum;
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            sum = 0L;
            for (LongWritable value : values) {
                sum += value.get();
            }
            out_key.set(key);
            out_val.set(sum);
            context.write(out_key,out_val);
        }
    }
}
