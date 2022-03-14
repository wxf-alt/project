package hbase.mr_hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapred.TableReduce;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import java.io.IOException;

/**
 * 实现将HDFS中的数据写入到HBase表中
 */
@SuppressWarnings("ALL")
public class ExampleMR2 {

    private static class ExampleMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
        private ImmutableBytesWritable outKey = new ImmutableBytesWritable();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split("\t");
            // 封装 rowkey
            outKey.set(Bytes.toBytes(split[0]));
            Put put = new Put(Bytes.toBytes(split[0]));
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("categery"), Bytes.toBytes(split[1]));
            context.write(outKey, put);
        }
    }

    private static class ExampleReducer extends TableReducer<ImmutableBytesWritable, Put, NullWritable> {}

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = HBaseConfiguration.create();
        //创建Job任务
        Job job = Job.getInstance(conf);
        job.setJarByClass(ExampleMR2.class);
        job.setJobName("ExampleMR");

        job.setMapperClass(ExampleMapper.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(Put.class);

        FileInputFormat.setInputPaths(job,new Path("hdfs://nn1.hadoop:9000/....."));

        //设置Reducer
        TableMapReduceUtil.initTableReducerJob("t5", ExampleReducer.class, job);
        //设置Reduce数量，最少1个
        job.setNumReduceTasks(1);
        boolean isSuccess = job.waitForCompletion(true);
    }

}
