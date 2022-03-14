package javase.string;

import org.junit.Test;

public class StringBufferAPI {

    @Test
    public void test1(){
        StringBuffer sb = new StringBuffer();
        // 添加
        sb.append("abcdefg");
        System.out.println(sb);
        sb.delete(0,4);
        System.out.println(sb);
        // 从 0 开始(层次是数组)  左闭右开
        sb.replace(0,2,"a");
        System.out.println(sb);
        sb.insert(0,"cb");
        System.out.println(sb);
        sb.reverse();
        System.out.println(sb);
    }

    @Test
    public void test2(){
        // 底层创建一个 长度为 16 的数组
        // super(16);
        StringBuffer sb = new StringBuffer();
        sb.append("aaaaaaaaaaaaaaaaaaaaaaaaaaa");

        // 1.创建了一个长度为 (字符串长度 + 16) 的数组
        // 2.将字符串添加到数组中
        StringBuffer sb1 = new StringBuffer("aaaaa");
        System.out.println(sb1);

    }


}
