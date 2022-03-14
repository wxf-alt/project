package javase.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CollecrtionTest {

    @Test
    public void test1() {
        Collection con = new ArrayList();
        // 添加数据
        con.add("aa");
        con.add("aa");
        con.add("aa");
        con.add("bb");
        System.out.println(con.size());

        // 删除匹配上的 一个元素
        con.remove("aa");
        System.out.println(con.size());

        Collection con1 = new ArrayList();
        con1.add("aa");
        // 删除集合中能匹配上的所有数据
        con.removeAll(con1);
        System.out.println(con.size());

        // 清空数据
        con.clear();
        System.out.println(con.size());
    }

    @Test
    public void test2() {
        // 1.使用迭代器遍历Collection接口
        Collection con = new ArrayList();
        con.add("aa");
        con.add("bb");
        con.add("cc");
        con.add("dd");
        con.add("ee");
        Iterator iterator = con.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            System.out.println(next);
        }
    }

    @Test
    public void test3() {
        // 2.使用增强for循环遍历
        Collection con = new ArrayList();
        con.add("aa");
        con.add("bb");
        con.add("cc");
        con.add("dd");
        con.add("ee");
        for (Object o : con) {
            System.out.println(o);
        }
    }

}
