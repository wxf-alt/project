package hadoop.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;
import org.junit.Test;
import java.io.IOException;

public class TestCompression {
    @Test
    public void testCompression() throws ClassNotFoundException, IOException {

        // 解压缩：调用CompressionCodec.createCompressionInputStream返回一个可以解压缩的输入流
        // 压缩：调用CompressionCodec.createCompressionOutputStream返回一个可以压缩的输出流
        Path file = new Path("e:/悲惨世界(英文版).txt");

        String codecClassName = "org.apache.hadoop.io.compress.GzipCodec";
        Class<?> codecClass = Class.forName(codecClassName);

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        FSDataInputStream inputStream = fs.open(file);

        // 确定使用哪种压缩格式 CompressionCodec
        CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);
        FSDataOutputStream outputStream = fs.create(new Path("e:/file" + codec.getDefaultExtension()), true);
        //带压缩的输出流
        CompressionOutputStream createOutputStream = codec.createOutputStream(outputStream);
        IOUtils.copyBytes(inputStream, createOutputStream, conf, true);
    }

    @Test
    public void testDecompression() throws Exception {

        Path file = new Path("e:/file.gz");

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        FSDataInputStream is = fs.open(file);
        //根据后缀名获取文件对应的压缩格式
        CompressionCodec codec = new CompressionCodecFactory(conf).getCodec(file);
        //创建一个可以解压缩的输入流
        CompressionInputStream createInputStream = codec.createInputStream(is);

        //创建一个非压缩的输出流
        FSDataOutputStream outputStream = fs.create(new Path("e:/file.txt"), true);
        IOUtils.copyBytes(createInputStream, outputStream, conf, true);
    }
}

