package hadoop.mapreduce.bean;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

@SuppressWarnings("ALL")
public class MyRecordWriter extends RecordWriter<String, NullWritable> {

    private Path path1 = new Path("E:\\A_data\\4.测试数据\\输出\\outputformat\\atguigu.log");
    private Path path2 = new Path("E:\\A_data\\4.测试数据\\输出\\outputformat\\other.log");
    private FileSystem fs;
    private FSDataOutputStream outputStream1;
    private FSDataOutputStream outputStream2;

    public MyRecordWriter(TaskAttemptContext job) throws IOException {
        Configuration conf = job.getConfiguration();
        fs = FileSystem.get(conf);
        outputStream1 = fs.create(path1);
        outputStream2 = fs.create(path2);
    }

    //负责将 key-value写出到文件
    @Override
    public void write(String key, NullWritable value) throws IOException, InterruptedException {
        if(key.contains("atguigu")){
            outputStream1.write(key.getBytes());
        }else{
            outputStream2.write(key.getBytes());
        }
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        if(outputStream1 != null){
            IOUtils.closeStream(outputStream1);
        }
        if(outputStream2 != null){
            IOUtils.closeStream(outputStream2);
        }
        if(fs != null){
            IOUtils.closeStream(fs);
        }
    }

}
