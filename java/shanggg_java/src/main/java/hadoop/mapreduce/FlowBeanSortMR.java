package hadoop.mapreduce;

import hadoop.mapreduce.bean.FlowBean;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

@SuppressWarnings("ALL")
public class FlowBeanSortMR {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 创建Job
        Configuration conf = new Configuration();
        // 本地
        conf.set("fs.defaultFS","file:///");
        Job job = Job.getInstance(conf);
        // 设置Job
//        job.setJarByClass(WordCount.class);
        // 设置 Map和Reduce的与运行类
        job.setMapperClass(MyFlowBeanMapper.class);
        job.setReducerClass(MyFlowBeanReducer.class);
        // 设置 Map和Reducer的输入 v-k 类型
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        // 设置 比较器
        job.setSortComparatorClass(MyDescComparator.class);

        // 设置输出输入目录
        Path inputPath = new Path("E:\\A_data\\4.测试数据\\输出\\flowbean\\part-r-00000");
        Path outputPath = new Path("E:\\A_data\\4.测试数据\\输出\\flowbean_sort");
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


    // 13470253144	180	180	360
    public static  class  MyFlowBeanMapper extends Mapper<LongWritable, Text,LongWritable, Text>{
        private LongWritable out_key = new LongWritable();
        private Text out_val = new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split("\t");
            out_key.set(Long.parseLong(split[3]));
            out_val.set(split[0] + "\t" + split[1] + "\t" + split[2]);

            context.write(out_key,out_val);
        }
    }

    public static class MyFlowBeanReducer extends Reducer<LongWritable, Text,Text, LongWritable>{
        private Text  out_key = new Text();
        private LongWritable out_val = new LongWritable();
        @Override
        protected void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value : values) {
                out_key.set(value);
                out_val.set(key.get());
                context.write(out_key,key);
            }
        }
    }

    public static class MyDescComparator extends WritableComparator{
        @Override
        public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
            long thisValue = readLong(b1, s1);
            long thatValue = readLong(b2, s2);
            return (int) (thatValue - thisValue);
        }
    }

}
