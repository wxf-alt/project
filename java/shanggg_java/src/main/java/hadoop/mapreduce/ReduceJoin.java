package hadoop.mapreduce;

import hadoop.mapreduce.bean.JoinBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class ReduceJoin {

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
        job.setMapperClass(MyReduceJoinMapper.class);
        job.setReducerClass(MyReduceJoinReducer.class);

        // 设置 Map和Reducer的输入 v-k 类型
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(JoinBean.class);

        // 设置 分区器
        job.setPartitionerClass(MyPartitioner.class);
        // 设置 Reducer 个数
        job.setNumReduceTasks(5);

        // 设置输出输入目录
        Path inputPath = new Path("E:\\A_data\\4.测试数据\\输入\\reducejoin");
        Path outputPath = new Path("E:\\A_data\\4.测试数据\\输出\\reducejoin");
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

    public static class MyReduceJoinMapper extends Mapper<LongWritable, Text, NullWritable, JoinBean>{
        private NullWritable out_key = NullWritable.get();
        private JoinBean out_val = new JoinBean();
        private String source;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            // 获取当前数据来源于 哪一个切片
            InputSplit inputSplit = context.getInputSplit();
            FileSplit fileSplit = (FileSplit) inputSplit;
            // 获取文件名称
            source = fileSplit.getPath().getName();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] split = value.toString().split("\t");

            out_val.setSource(source);
            if(source.equals("order.txt")){
                out_val.setOrderId(split[0]);
                out_val.setPid(split[1]);
                out_val.setAmount(split[2]);
                // 保证所有的属性不为 null
                out_val.setPname("nodata");
            }else if(source.equals("pd.txt")){
                out_val.setPid(split[0]);
                out_val.setPname(split[1]);
                out_val.setOrderId("nodata");
                out_val.setAmount("nodata");
            }

            context.write(out_key,out_val);
        }
    }

    // 保证 pid 相同的数据分到一个区
    public static class MyPartitioner extends Partitioner<NullWritable, JoinBean>{
        @Override
        public int getPartition(NullWritable nullWritable, JoinBean joinBean, int numPartitions) {
            return (joinBean.getPid().hashCode() & Integer.MAX_VALUE) % numPartitions;
        }
    }

    public static class MyReduceJoinReducer extends Reducer<NullWritable, JoinBean,NullWritable, JoinBean>{

        private List<JoinBean> orderDatas = new ArrayList<>();
        private Map<String,String> pdDatas = new HashMap<>();

        @Override
        protected void reduce(NullWritable key, Iterable<JoinBean> values, Context context) throws IOException, InterruptedException {
            // 根据 source 分类
            for (JoinBean value : values) {
                if(value.getSource().equals("order.txt")){
                    JoinBean joinBean = new JoinBean();
                    try {
                        BeanUtils.copyProperties(joinBean,value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    orderDatas.add(joinBean);
                }else{
                    pdDatas.put(value.getPid(),value.getPname());
                }
            }

            for (JoinBean orderData : orderDatas) {
                orderData.setPname(pdDatas.get(orderData.getPid()));
                context.write(NullWritable.get(),orderData);
            }

        }

//        @Override
//        protected void cleanup(Context context) throws IOException, InterruptedException {
//            for (JoinBean orderData : orderDatas) {
//                orderData.setPname(pdDatas.get(orderData.getPid()));
//                context.write(NullWritable.get(),orderData);
//            }
//        }

    }

}
