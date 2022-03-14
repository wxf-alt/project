package javase.collection;

import org.junit.Test;

import java.util.*;

public class SetTest {

    @Test
    public void test(){
        Set set = new HashSet();
        set.add("發發瘋");
        set.add("有發熱");
        set.add("歐體");

        // 使用 Iterator 遍历
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
        System.out.println("====================================");
        // 使用增强 for 遍历
        for (Object o : set) {
            String str = (String) o;
            System.out.println(str);
        }
    }

    @Test
    public void test2(){
        Set set = new TreeSet<Book>(new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return (int)(o1.getBookPrica() - o2.getBookPrica());
            }
        });

        LinkedHashSet linkedHashSet = new LinkedHashSet();


        set.add("發發瘋");
        set.add("有發熱");
        set.add("歐體");


    }



}
