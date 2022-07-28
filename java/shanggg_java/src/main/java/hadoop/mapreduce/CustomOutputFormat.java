package hadoop.mapreduce;

import hadoop.mapreduce.bean.MyOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

@SuppressWarnings("ALL")
public class CustomOutputFormat {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 创建Job
        Configuration conf = new Configuration();
        // 本地
        conf.set("fs.defaultFS", "file:///");
        Job job = Job.getInstance(conf);
        // 设置Job
//        job.setJarByClass(WordCount.class);
        // 设置 Map和Reduce的与运行类
        job.setMapperClass(MyCustomMapper.class);
        job.setNumReduceTasks(0);

        // 输出格式
        job.setOutputFormatClass(MyOutputFormat.class);

        Path inputPath = new Path("E:\\A_data\\4.测试数据\\输入\\outputformat");
        Path outputPath = new Path("E:\\A_data\\4.测试数据\\输出\\outputformat1");
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

    public static class MyCustomMapper extends Mapper<LongWritable, Text, String, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String content = value.toString();
            context.write(content + "\n",NullWritable.get());
        }
    }


}
