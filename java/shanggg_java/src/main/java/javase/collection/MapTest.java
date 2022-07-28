package javase.collection;

import org.junit.Test;

import java.util.*;

public class MapTest {

    @Test
    public void Test1(){
        Map map = new HashMap();
        // 添加元素
        map.put("a","123");
        map.put("b","456");
        map.put("c","789");

        // 1.使用 keySet 遍历
        Set keySet = map.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            String value = (String) map.get(key);
            System.out.println("key :" + key + "\tvalue:" + value);
        }
        System.out.println("===================================================");
        // 2.使用 entrySet 遍历
        Set entrySet = map.entrySet();
        Iterator iterator1 = entrySet.iterator();
        while(iterator1.hasNext()){
            Map.Entry entry = (Map.Entry) iterator1.next();
            System.out.println("key :" + entry.getKey() + "\tvalue:" + entry.getValue());
        }
    }



}
