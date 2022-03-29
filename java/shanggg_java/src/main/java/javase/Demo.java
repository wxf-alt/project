package javase;

import org.junit.Test;

@SuppressWarnings("ALL")
public class Demo {

    @Test
    public void test() {
        String s1 = "runoob";
        String s2 = "runoob";
        //  因为在 Java 中 + 操作符的优先级大于 ==，
        //  所以输出部分表达式等于 “s1 == s2 is:runoob” == “runoob”，
        //  该表达式计算结果为 false。
        System.out.println("s1 == s2 is:" + s1 == s2);
    }

    @Test
    public void test2() {
//        int x=4;
//        System.out.println(x++);

        String preserveExisting = "12";
        String key = "34";
        String value = "56";
        System.out.println(String.format("Creating StaticInterceptor: preserveExisting=%s,key=%s,value=%s", preserveExisting, key, value));
        System.out.printf("Creating StaticInterceptor: preserveExisting=%s,key=%s,value=%s", preserveExisting, key, value);
    }

    @Test
    public void test3() {
        StringBuffer a = new StringBuffer("Runoob");
        StringBuffer b = new StringBuffer("Google");
        // delete(x, y) 删除从字符串 x 的索引位置开始到 y-1 的位置，append() 函数用于连接字符串。
        a.delete(1, 3); // RoobGoogle
        a.append(b);
        System.out.println(a);
    }

    @Test
    public void test4() {
        //符合值范围时候，进入也创建好的静态IntergerCache，i+offset的值表示去取cache数组中那个下标的值
        Integer a = 100;//此处若使用new，则==值必为false
        Integer b = 100;
        System.out.println(a == b);//true

        //当不符合-128 127值范围时候。记住用的：new，开辟新的内存空间，不属于IntergerCache管理区
        Integer c = 150;
        Integer d = 150;
        System.out.println(c == d);//false
    }


    @Test
    public void test5() {
        String s1 = "aaa";
        String s2 = "aaa";
        String s9 = "aaa";
        // true
        System.out.println(s1 == s2);
        String s3 = new String("bbb");
        String s4 = new String("bbb");
        // false
        System.out.println(s3 == s4);
    }

    @Test
    public void test6() {
        String s = "hello";
        String s2 = "he";
        String s3 = "llo";
        String s4 = s2 + s3;
        // 可以调用 方法 去常量池查找 是否有结果对应的数据，有直接赋值
        String s5 = (s2 + s3).intern();
        System.out.println(s == s4); // false
        System.out.println(s == s5); // true
    }

    @Test
    public void test7() {
        for (int i = 1; i <= 100; i++){
            if(i % 2 == 0){
                continue;
            }
            System.out.println("i = " + i);
        }
    }

}