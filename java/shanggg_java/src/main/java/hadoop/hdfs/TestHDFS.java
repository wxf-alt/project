package hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("ALL")
public class TestHDFS {

    private Configuration conf = null;
    private URI uri = null;
    private FileSystem fs = null;

    @Before
    public void init() throws URISyntaxException, IOException, InterruptedException {
        conf = new Configuration();
        uri = new URI("hdfs://nn2.hadoop:9000");
        fs = FileSystem.get(uri, conf, "hadoop");
    }

    @After
    public void close() throws IOException {
        if (fs != null) {
            fs.close();
        }
    }

    @Test
    public void testMkdir() throws IOException, URISyntaxException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("fs.permissions.umask-mode", "000");

        // 使用 window 用户 在 hdfs 上创建目录
//        FileSystem fs = FileSystem.get(conf);

        // 使用 自定义用户 在 hdfs 上创建目录
        // 必须使用 active 状态的 服务器地址
        URI uri = new URI("hdfs://nn2.hadoop:9000");
        FileSystem fs = FileSystem.get(uri, conf, "hadoop");

        // 路径
        Path path = new Path("/IdeaTest2");

        // 创建目录 ，不自定义权限
//        fs.mkdirs(path);

        // https://blog.csdn.net/lonewolf1992/article/details/88993938?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_utm_term~default-0.no_search_link&spm=1001.2101.3001.4242.1&utm_relevant_index=3
        /**
         * 查看HDFS的umask
         * #>  hdfs getconf -confkey fs.permissions.umask-mode
         *  打印结果是022, 缘由在此处产生(777-022=755)
         *  自定义权限 需要 添加配置
         *      conf.set("fs.permissions.umask-mode", "000");
         */
        FsPermission fsPermission = new FsPermission("770");
        fs.mkdirs(path, fsPermission);

        // 关闭
        fs.close();
    }

    @Test
    public void testPut() throws IOException {
        Path path1 = new Path("C:\\Users\\wxf\\Desktop\\4.测试数据\\test1\\新文件 5.txt");
        Path path2 = new Path("C:\\Users\\wxf\\Desktop\\4.测试数据\\test2\\新文件 6.txt");

        Path[] formPath = {path1, path2};
        Path toPath = new Path("/IdeaTest");
        // 是否删除源文件    是否覆盖    源文件路径    目标路径
        fs.copyFromLocalFile(false, true, formPath, toPath);

    }

    @Test
    public void testGet() throws IOException {
        // 生成 crc 文件, 校验文件 判断 源文件是否发生损坏
//        fs.copyToLocalFile(false,new Path("/data/ips"),new Path("C:\\Users\\wxf\\Desktop\\4.测试数据\\"),false);
        // 不会生成 crc 文件
        fs.copyToLocalFile(false, new Path("/data/ips"), new Path("C:\\Users\\wxf\\Desktop\\4.测试数据\\"), true);
    }

    @Test
    public void testRm() throws IOException {
        Path path = new Path("/IdeaTest");
        //    是否递归删除
        boolean delete = fs.delete(path, true);
        String s = delete == true ? "成功" : "失败";
        System.out.println(s);
//        if (delete == true) {
//            System.out.println("成功");
//        } else {
//            System.out.println("失败");
//        }
    }

    @Test
    public void testMv() throws IOException {
        fs.rename(new Path("/IdeaTest/新文件 6.txt"), new Path("/IdeaTest/file6.txt"));
    }

    @Test
    public void testIfPathExists() throws IOException {
        Path path = new Path("/IdeaTest/file6.txt");
        boolean exists = fs.exists(path);
        System.out.println(exists);
    }

    @Test
    public void testFileOrDir() throws IOException {
        Path path = new Path("/IdeaTest/file6.txt");

//        boolean file = fs.isFile(path);
//        boolean directory = fs.isDirectory(path);
//        System.out.println("文件 ? " + file);
//        System.out.println("目录 ? " + directory);

        // 建议使用 FileStatus
        // 使用 FileStatus 可以获取到很多东西
        FileStatus fsFileStatus = fs.getFileStatus(path);
        System.out.println("目录 ? " + fsFileStatus.isDirectory());
        System.out.println("文件 ? " + fsFileStatus.isFile());

        System.out.println("==================================");

        // 递归
        FileStatus[] fileStatuses = fs.listStatus(path);
        for (FileStatus fileStatus : fileStatuses) {
            System.out.println("文件 ? " + fileStatus.isFile());
            System.out.println("目录 ? " + fileStatus.isDirectory());
            // 获取路径  完整的路径 (协议 + 文件名)
            Path path1 = fileStatus.getPath();
            System.out.println(path);
            // 获取文件名
            String name = path1.getName();
            System.out.println(name);
        }
    }

    // LocatedFileStatus 是 FileStatus 子类,除了文件的属性,还有块的位置信息
    @Test
    public void testGetBlockInfomation() throws IOException {
        Path path = new Path("/IdeaTest/file6.txt");
        // 可以递归获取目录下所有文件
        RemoteIterator<LocatedFileStatus> locatedFileStatusRemoteIterator = fs.listFiles(path, true);

        // 获取单个文件
//        RemoteIterator<LocatedFileStatus> locatedFileStatusRemoteIterator = fs.listLocatedStatus(path);

        while (locatedFileStatusRemoteIterator.hasNext()) {
            LocatedFileStatus locatedFileStatus = locatedFileStatusRemoteIterator.next();
            // 块的位置信息
            BlockLocation[] blockLocations = locatedFileStatus.getBlockLocations();
            for (BlockLocation blockLocation : blockLocations) {
                System.out.println(blockLocation);
                System.out.println("========================");
            }
        }
    }

    // 上传文件时 只上传文件的一部分
    @Test
    public void testCustomUload() throws IOException, URISyntaxException {
        // 提供两个 Path 和两个 FileSstem
        Path src = new Path("E:\\A_data\\1.记录\\笔记\\张隆 JAVASE 笔记-供参考.docx");
        Path dest = new Path("/IdeaTest/张隆 JAVASE 笔记-供参考.docx");
        // 本地文件系统
        FileSystem localFileSystem = FileSystem.get(new URI("file:///"), new Configuration());

        // 使用本地文件系统中获取输入流 读取文件
        FSDataInputStream inputStream = localFileSystem.open(src);
        // 使用 HDFS 的分布式文件系统获取的输出流 来向 dest 路径写入数据
        FSDataOutputStream outputStream = fs.create(dest, true);

        // 流中的拷贝
        // 1k
        byte[] buffer = new byte[1024];
        // 读一次1k 读取 1024 * 10  10M数据
        for (int i = 0; i < 1024 * 10; i++) {
            inputStream.read(buffer);
            outputStream.write(buffer);
        }

        // 关流
        IOUtils.closeStream(inputStream);
        IOUtils.closeStream(outputStream);

        localFileSystem.close();
        fs.close();
    }

    // 下载文件时 只下载某一部分
    @Test
    public void testCustomDownload() throws IOException, URISyntaxException {
        // 提供两个 Path 和两个 FileSstem
        Path src = new Path("/IdeaTest/尚硅谷大数据技术之Hadoop.xmind");
        Path dest = new Path("C:\\Users\\wxf\\Desktop\\4.测试数据\\尚硅谷大数据技术之Hadoop2.xmind");

        // 本地文件系统
        FileSystem localFileSystem = FileSystem.get(new URI("file:///"), new Configuration());

        // 分布式文件系统获取输入流
        FSDataInputStream inputStream = fs.open(src);
        // 本地文件系统 输出流
        FSDataOutputStream outputStream = localFileSystem.create(dest, true);

        // 定位流的位置 偏移量
        inputStream.seek(1024 * 1024 * 128);

        byte[] buffer = new byte[1024];
        for (int i = 0; i < 1024 * 128; i++) {
            inputStream.read(buffer);
            outputStream.write(buffer);
        }

        IOUtils.closeStream(inputStream);
        IOUtils.closeStream(outputStream);

        fs.close();
        localFileSystem.close();

    }

    @Test
    public void testCustomDownload2() throws IOException, URISyntaxException {
        // 提供两个 Path 和两个 FileSstem
        Path src = new Path("/IdeaTest/尚硅谷大数据技术之Hadoop.xmind");
        Path dest = new Path("C:\\Users\\wxf\\Desktop\\4.测试数据\\尚硅谷大数据技术之Hadoop2.xmind");

        // 本地文件系统
        FileSystem localFileSystem = FileSystem.get(new URI("file:///"), new Configuration());

        // 分布式文件系统获取输入流
        FSDataInputStream inputStream = fs.open(src);
        // 本地文件系统 输出流
        FSDataOutputStream outputStream = localFileSystem.create(dest, true);

        // 定位流的位置 偏移量
        inputStream.seek(1024 * 1024 * 128);

        // 读取最后一个块
        // buffSize 不能超过 4096
        IOUtils.copyBytes(inputStream,outputStream,4096,true);

        fs.close();
        localFileSystem.close();
    }

}
