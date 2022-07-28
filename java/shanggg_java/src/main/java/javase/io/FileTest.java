package javase.io;

import org.junit.Test;

import java.io.*;


/**
 * @author wxf
 */
public class FileTest {


    // 读取文件(基础版)
    @Test
    public void test1() {
        FileReader reader = null;
        try {
            reader = new FileReader(new File("C:\\Users\\wxf\\Desktop\\4.测试数据\\新文件 5.txt"));
            int read = reader.read(); // 读到的是字符的码值
            while (read != -1) {
                System.out.print((char) read);
                read = reader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert reader != null;
                reader.close();
//                if(reader != null){
//                    reader.close();
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 写文件
    @Test
    public void test2() {
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File("C:\\Users\\wxf\\Desktop\\4.测试数据\\新文件 5.txt"));
            writer.write("a");
            writer.write("b");
            writer.write("c");
            writer.write("d");
            writer.write("e");
            writer.write("f");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 使用 char数组 读取数据
    @Test
    public void test3() {
        FileReader reader = null;
        try {
            reader = new FileReader(new File("C:\\Users\\wxf\\Desktop\\4.测试数据\\新文件 5.txt"));
            char[] chars = new char[100];
            int realCount = reader.read(chars);// 一次读取多个字符到数组中
            while (realCount != -1) {
                for (int i = 0; i < realCount; i++) {
                    System.out.print(chars[i]);
                }
                realCount = reader.read(chars);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert reader != null;
                reader.close();
//                if(reader != null){
//                    reader.close();
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 读取文件
    @Test
    public void tes4(){
        File file = new File("C:\\Users\\wxf\\Desktop\\4.测试数据\\新文件 5.txt");
        FileReader fr = null;
        BufferedReader bfr = null;
        try {
            fr = new FileReader(file);
            bfr = new BufferedReader(fr);
            String temp;
            while((temp = bfr.readLine()) != null){
                System.out.println(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bfr != null) {
                try {
                    bfr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fr != null){
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // 使用 InputStreamReader 和 OutputStreamReader 转换流
    // 字节 转 字符
    @Test
    public void test5() throws IOException {

        File file = new File("C:\\Users\\wxf\\Desktop\\4.测试数据\\新文件 5.txt");
//        // 以前是这么写
//        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

//        // 利用转换流 将一个字节的节点流 封装成 一个字符的处理流
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        // 先构建字节节点流
        FileInputStream fis = new FileInputStream(file);
        // 利用字节节点流 构建 转换流
        InputStreamReader isr = new InputStreamReader(fis);
        // 利用转换流作为基础构建 字符处理流
        BufferedReader br = new BufferedReader(isr);

        String temp;
        while((temp = br.readLine()) != null){
            System.out.println(temp);
        }

        br.close();
        isr.close();
        fis.close();
    }

    @Test
    public void test6(){
        File file = new File("C:\\Users\\wxf\\Desktop\\4.测试数据\\新文件 6.txt");
        if(! file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter br = null;
        try {
            fos = new FileOutputStream(file,true);
            osw = new OutputStreamWriter(fos);
            br = new BufferedWriter(osw);
            // 追加文件时 先换行
            br.newLine();
            br.write("aa\nbb\ncc\ndd\nee\nff");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 注意流 关闭的 顺序
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(osw != null){
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
