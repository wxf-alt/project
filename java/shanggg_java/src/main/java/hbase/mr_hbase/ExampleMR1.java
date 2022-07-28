package hbase.mr_hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import java.io.IOException;

/**
 * 迁移表数据 将 A表的一部分数据，通过MR迁入到 B表中
 */
@SuppressWarnings("ALL")
public class ExampleMR1 {


    // Hbase 表的输入格式 是
    //     TableInputFormat 一个 region 切分为一个map
    //           RecordReader<ImmutableBytesWritable, Result>
    //       ImmutableBytesWritable  -> rowkey
    //       Result  -> 一行数据
    public static class ExampleMapper extends TableMapper<ImmutableBytesWritable, Put> {

        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
            // 构建 put 对象,封装rowkey
//            Put put = new Put(key.get());
            Put put = new Put(key.copyBytes());

            // 将读到的数据进行过滤,只选择info:age = 20 的数据进行输出
            Cell[] cells = value.rawCells();
            for (Cell cell : cells) {
                if (!("cf1".equals(Bytes.toString(CellUtil.cloneFamily(cell))) &&
                        "age".equals(Bytes.toString(CellUtil.cloneQualifier(cell))) &&
                        20 == Bytes.toInt(CellUtil.cloneValue(cell)))) {
                    put.add(cell);
                }
            }
            context.write(key, put);
        }
    }


    // Reducer 输出的类型必须是Mutation,是固定的
    //      Mutation是所有写数据类型的父类
    // Reducer的任务就是将 Mapper 输出的所有记录写出
    public static class ExampleReduce extends TableReducer<ImmutableBytesWritable, Put, NullWritable> {
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = HBaseConfiguration.create();
        //创建Job任务
        Job job = Job.getInstance(conf);
        job.setJarByClass(ExampleMR1.class);
        job.setJobName("ExampleMR");

        //配置Job
        Scan scan = new Scan();
        scan.setCacheBlocks(false);
        scan.setCaching(500);

        //设置Mapper，注意导入的是mapreduce包下的，不是mapred包下的，后者是老版本
        TableMapReduceUtil.initTableMapperJob(
                "fruit", //数据源的表名
                scan, //scan扫描控制器
                ExampleMapper.class,//设置Mapper类
                ImmutableBytesWritable.class,//设置Mapper输出key类型
                Put.class,//设置Mapper输出value值类型
                job//设置给哪个JOB
        );

        //设置Reducer
        TableMapReduceUtil.initTableReducerJob("fruit_mr", ExampleReduce.class, job);
        //设置Reduce数量，最少1个
        job.setNumReduceTasks(1);
        boolean isSuccess = job.waitForCompletion(true);
    }

}
