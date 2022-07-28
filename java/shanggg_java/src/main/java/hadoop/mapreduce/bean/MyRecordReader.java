package hadoop.mapreduce.bean;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class MyRecordReader extends RecordReader {

    private Text key;
    private BytesWritable value;
    private String fileName;
    private Path path;
    private int length;
    private FileSystem fs;
    private FSDataInputStream inputStream;

    private boolean flag = true;

    // 初始化，在创建后 进入Mapper的 run() 之前自动调用
    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        FileSplit fileSplit = (FileSplit) split;
        path = fileSplit.getPath();
        fileName = path.getName();
        length = (int) fileSplit.getLength();

        Configuration conf = context.getConfiguration();
        fs = FileSystem.get(conf);
        inputStream = fs.open(path);

    }


    // 读取一组输入的 key-value 读取到返回true,反之返回false
    // 文件名称封装成key  文件的内容封装成value
    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if(flag){
            if (key == null) {
                key = new Text();
            }
            if (value == null) {
                value = new BytesWritable();
            }

            // 赋值
            // 获取文件名
            key.set(fileName);

            // 将文件的内容读取到 BytesWritable中
            byte[] content = new byte[length];
            // 读取文件内容
            IOUtils.readFully(inputStream,content,0,length);
            // 封装
            value.set(content,0,length);

            flag = false;
            return true;
        }
        return false;
    }

    // 返回当前读取到的 key-value 中的 key
    @Override
    public Object getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    // 返回当前读取到的 key-value 中的 value
    @Override
    public Object getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    // 返回读取切片的进度
    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    // 在Mapper的输入关闭时调用，清理工作
    @Override
    public void close() throws IOException {
        if(inputStream != null){
            IOUtils.closeStream(inputStream);
        }
        if(fs != null){
            IOUtils.closeStream(fs);
        }
    }

}
