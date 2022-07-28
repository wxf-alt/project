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
public class FlowBeanSortMR3 {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 创建Job
        Configuration conf = new Configuration();
        // 本地
        conf.set("fs.defaultFS", "file:///");
        Job job = Job.getInstance(conf);
        // 设置Job
//        job.setJarByClass(WordCount.class);
        // 设置 Map和Reduce的与运行类
        job.setMapperClass(MyFlowBeanMapper.class);
        job.setReducerClass(MyFlowBeanReducer.class);
        // 设置 Map和Reducer的输入 v-k 类型
        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(FlowBean.class);
        job.setOutputValueClass(Text.class);

        // 设置 比较器
        job.setSortComparatorClass(MyRawCmoparator.class);

        // 设置输出输入目录
        Path inputPath = new Path("E:\\A_data\\4.测试数据\\输出\\flowbean\\part-r-00000");
        Path outputPath = new Path("E:\\A_data\\4.测试数据\\输出\\flowbean_sort3");
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
            System.out.println("输出目录删除成功");
        }
        FileInputFormat.setInputPaths(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);
        // 运行Job
        job.waitForCompletion(true);
    }


    // 13470253144	180	180	360
    public static class MyFlowBeanMapper extends Mapper<LongWritable, Text, FlowBean, Text> {
        private FlowBean out_key = new FlowBean();
        private Text out_val = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split("\t");
            out_key.setUpFlow(Long.parseLong(split[1]));
            out_key.setDownFlow(Long.parseLong(split[2]));
            out_key.setSumFlow(Long.parseLong(split[3]));

            out_val.set(split[0].getBytes());
            context.write(out_key, out_val);
        }
    }

    public static class MyFlowBeanReducer extends Reducer<FlowBean, Text, Text, FlowBean> {
        private Text out_key = new Text();
        private FlowBean out_val = new FlowBean();

        @Override
        protected void reduce(FlowBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value : values) {
                out_key.set(value);
                out_val.setUpFlow(key.getUpFlow());
                out_val.setDownFlow(key.getDownFlow());
                out_val.setSumFlow(key.getSumFlow());
                context.write(out_key, out_val);
            }
        }
    }

    public static class MyRawCmoparator implements RawComparator<FlowBean> {

        private FlowBean key1 = new FlowBean();
        private FlowBean key2 = new FlowBean();
        private DataInputBuffer buffer = new DataInputBuffer();


        // 负责从缓冲区中解析出要比较的两个 Key 对象
        // 然后调用 compare 对两个key进行比较
        @Override
        public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
            try {
                buffer.reset(b1, s1, l1);                   // parse key1
                key1.readFields(buffer);

                buffer.reset(b2, s2, l2);                   // parse key2
                key2.readFields(buffer);

                buffer.reset(null, 0, 0);                   // clean up reference
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return compare(key1, key2);
        }

        // Comparator 的 compare 方法
        @Override
        public int compare(FlowBean o1, FlowBean o2) {
            return (int) (o2.getSumFlow() - o1.getSumFlow());
        }
    }
}




