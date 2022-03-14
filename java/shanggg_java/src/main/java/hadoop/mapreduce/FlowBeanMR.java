package hadoop.mapreduce;

import hadoop.mapreduce.bean.FlowBean;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

@SuppressWarnings("ALL")
public class FlowBeanMR {

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
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        // 设置输出输入目录
        Path inputPath = new Path("C:\\Users\\wxf\\Desktop\\4.测试数据\\输入\\flowbean");
        Path outputPath = new Path("C:\\Users\\wxf\\Desktop\\4.测试数据\\输出\\flowbean");
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


    public static  class  MyFlowBeanMapper extends Mapper<LongWritable, Text,Text, FlowBean>{
        private Text out_key = new Text();
        private FlowBean out_val = new FlowBean();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] str = value.toString().split("\t");

            out_key.set(str[1]);
            out_val.setUpFlow(Long.parseLong(str[str.length - 3]));
            out_val.setDownFlow(Long.parseLong(str[str.length - 2]));
            context.write(out_key,out_val);
        }
    }


    public static class MyFlowBeanReducer extends Reducer<Text, FlowBean,Text, FlowBean>{
        private Text out_key = new Text();
        private FlowBean out_val = new FlowBean();
        @Override
        protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {

            long sumUpFlow = 0;
            long sumDownFlow = 0;

            for (FlowBean value : values) {
                out_key.set(key);
                sumUpFlow += value.getUpFlow();
                sumDownFlow += value.getDownFlow();
            }

            out_val.setUpFlow(sumUpFlow);
            out_val.setDownFlow(sumDownFlow);
            out_val.setSumFlow(sumUpFlow + sumDownFlow);

            out_key.set(key);

            context.write(out_key,out_val);
        }
    }
}
