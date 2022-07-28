package utils;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

// 随机取词 然后生成到文件中
public class HaoopCreateWordTest {

    @Test
    public void test1() throws IOException{
        // 随机取词 然后生成到文件中
        String str = "zookeeper HDFS YRAN Linux ELT HBASE HIVE MAPREDUCE HADOOP HTML CSS JavaScript jQuery bootstrap MySQL JDBC DBtils JSP Servlet Spring SpringMVC 项目实战";
        String[] strs = str.split(" ");
        Random r = new Random();
        int num = 0;
        File file = new File("words1");
        FileWriter w = new FileWriter(file);
        String a = "";
        for (int i = 0; i < 60000; i++) {
            num = r.nextInt(strs.length-1);
            if(i%(r.nextInt(10)+10)==0){
                a+=strs[num] + "\t\n";
            }else{
                a+=strs[num] + "\t";
            }
            if(i%10==0){
                w.write(a);
            }
        }
        w.flush();
        w.close();
        System.out.println("OK!");
    }

}