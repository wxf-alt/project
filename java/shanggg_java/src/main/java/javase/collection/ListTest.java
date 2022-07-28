package javase.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ListTest {
    @Test
    // ArrayList
    public void test4() {
        List list = new ArrayList();
        list.add("苹果");
        list.add("葡萄");
        list.add("菠萝");
        System.out.println(list);
        // 放在第一个索引位置
        list.add(1, "西瓜");
        System.out.println(list);
        list.add("菠萝");
        list.add("菠萝");

        // 获取索引的值
        Object o = list.get(2);
        System.out.println(o);

        // 返回在集合中第一个出现的索引位置
        int index = list.indexOf("菠萝");
        System.out.println(index);
        // 返回在集合中最后一个出现的索引位置 如果没有返回 -1
        int j = list.lastIndexOf("菠萝");
        System.out.println(j);

        System.out.println(list);
        // 修改_设置 索引位置的值
        list.set(0, "红苹果");
        System.out.println(list);
    }

    @Test
    // ArrayList
    public void test5() {
        List list = new ArrayList();
        list.add("苹果");
        list.add("葡萄");
        list.add("菠萝");

        // 1.使用 iterator 遍历
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            String str = (String) iterator.next();
            System.out.println(str);
        }
        System.out.println("==================================");
        // 2.使用增强for 遍历   foreach
        for (Object l : list) {
            String s = (String) l;
            System.out.println(s);
        }
        System.out.println("==================================");
        // 3.使用索引遍历 list
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }


    @Test
    public void test6(){
        LinkedList linkedList = new LinkedList();
        linkedList.add("苹果");
        linkedList.add("葡萄");
        linkedList.add("菠萝");
        System.out.println(linkedList);

        linkedList.addFirst("aa");
        System.out.println(linkedList);
        linkedList.addLast("bb");
        System.out.println(linkedList);

        linkedList.removeFirst();
        linkedList.removeLast();
        linkedList.getFirst();
        linkedList.getLast();


    }



}
