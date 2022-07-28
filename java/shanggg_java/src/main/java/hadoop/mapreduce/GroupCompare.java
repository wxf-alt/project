package hadoop.mapreduce;

import hadoop.mapreduce.bean.OrderBean;
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

public class GroupCompare {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 创建Job
        Configuration conf = new Configuration();
        // 本地
        conf.set("fs.defaultFS","file:///");
        Job job = Job.getInstance(conf);
        job.setJobName("--WordCount--");
        // 设置Job
        // 设置 Map和Reduce的与运行类
        job.setMapperClass(MyOrderMapper.class);
        job.setReducerClass(MyOrderReducer.class);

        // 设置 Map和Reducer的输入 v-k 类型
        job.setMapOutputKeyClass(OrderBean.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setOutputKeyClass(OrderBean.class);
        job.setOutputValueClass(NullWritable.class);

        // 设置分组器
        job.setGroupingComparatorClass(MyGroupingComparator2.class);


        // 设置输出输入目录
        Path inputPath = new Path("E:\\A_data\\4.测试数据\\输入\\groupcomparator\\data.txt");
        Path outputPath = new Path("E:\\A_data\\4.测试数据\\输出\\groupcomparator");
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

    public static class MyOrderMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable> {
        private OrderBean out_key = new OrderBean();
        private NullWritable out_val = NullWritable.get();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split("\t");
            out_key.setOrderId(split[0]);
            out_key.setpId(split[1]);
            out_key.setAccount(Double.parseDouble(split[2]));

            context.write(out_key, out_val);
        }
    }


    // 自定义分组器
    //      1.继承 WritableComparator
    //      2.实现 RawComparator
    public static class MyGroupingComparator implements RawComparator<OrderBean> {

        private OrderBean key1 = new OrderBean();
        private OrderBean key2 = new OrderBean();
        private DataInputBuffer buffer = new DataInputBuffer();

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

        @Override
        public int compare(OrderBean o1, OrderBean o2) {
            return o1.getOrderId().compareTo(o2.getOrderId());
        }
    }

    public static class MyGroupingComparator2 extends WritableComparator {

        public MyGroupingComparator2() {
            super(OrderBean.class,null,true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            OrderBean o1 = (OrderBean) a;
            OrderBean o2 = (OrderBean) b;
            return o1.getOrderId().compareTo(o2.getOrderId());
        }
    }

    public static class MyOrderReducer extends Reducer<OrderBean, NullWritable, OrderBean, NullWritable> {
        @Override
        protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            Double maxAccount = key.getAccount();

            for (NullWritable value : values) {
                if (!(key.getAccount().equals(maxAccount))) {
                    break;
                }
                context.write(key, value);
            }
        }
    }


}
