package hadoop.mapreduce.bean;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

// 改变切片策略  一个文件固定为一片,不可切
// 提供 RecordReader 读取切片的文件名作为 key, 文件内容作为 value
public class MyInputFormat extends FileInputFormat {

    @Override
    public RecordReader createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        return new MyRecordReader();
    }

    // 文件不可切
    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return false;
    }
}
