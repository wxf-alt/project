package hadoop.mapreduce;

import hadoop.mapreduce.bean.MyInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class CustomInputFormat {

    public static void main(String[] args) throws Exception {
        // 创建Job
        Configuration conf = new Configuration();
        // 本地
        conf.set("fs.defaultFS","file:///");
        Job job = Job.getInstance(conf);
        // 设置Job
//        job.setJarByClass(WordCount.class);
        // 设置 Map和Reduce的与运行类
        job.setMapperClass(MyCustomMapper.class);
        job.setReducerClass(MyCustomReducer.class);
        // 设置 Map和Reducer的输入 v-k 类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BytesWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);

        job.setInputFormatClass(MyInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        // 设置输出输入目录
        Path inputPath = new Path("C:\\Users\\wxf\\Desktop\\4.测试数据\\输入\\custom");
        Path outputPath = new Path("C:\\Users\\wxf\\Desktop\\4.测试数据\\输出\\custom");
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


    public static class MyCustomMapper extends Mapper<Text, BytesWritable,Text, BytesWritable>{}

    public static class MyCustomReducer extends Reducer<Text, BytesWritable,Text, BytesWritable>{}

}
