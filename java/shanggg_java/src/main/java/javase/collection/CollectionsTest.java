package javase.collection;

import org.junit.Before;
import org.junit.Test;

import java.util.*;


@SuppressWarnings("ALL")
public class CollectionsTest {

    private List<String> list;

    @Before
    public void init(){
        list = new ArrayList<String>();
        list.add("小明");
        list.add("小强");
        list.add("小白");
        list.add("小白");
        list.add("小黑");
        list.add("小黄");
    }

    @Test
    public void test1(){
        System.out.println(list);
//        // 反转集合中的元素
//        Collections.reverse(list);
//        // 随机排序
//        Collections.shuffle(list);
//        // 排序
//        Collections.sort(list);
//        // 自定义排序 -》 定制排序
//        Collections.sort(list, new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o2.compareTo(o1);
//            }
//        }); // 使用比较器
//        // 将指定下标的元素 进行交换
//        Collections.swap(list,0,3);
//
//        // 返回给定集合中最大的元素
//        String max = Collections.max(list);
//        System.out.println(max);
//        // 返回给定集合中最小的元素
//        String min = Collections.min(list);
//        System.out.println(min);

//        ArrayList newList = new ArrayList<String>(); // 声明时 是一个空数组
//        newList.add(null); // 添加元素 初始长度为10
//        newList.add(null);
//        newList.add(null);
//        newList.add(null);
//        newList.add(null);
//        Collections.copy(newList,list); // 在复制集合的时候 需要新集合的size 和 源集合的size 相同
//        System.out.println(newList);

//        // 返回指定集合中 指定元素出现的次数
//        int i = Collections.frequency(list, "小白");
//        System.out.println(i);

        // 使用新值 替换 List对象中 所有的 旧值
        Collections.replaceAll(list,"小白","小bai");
        System.out.println(list);

        System.out.println(list);
    }
}
