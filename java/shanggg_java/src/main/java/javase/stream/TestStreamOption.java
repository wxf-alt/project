package javase.stream;

import org.junit.Test;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestStreamOption {

    @Test
    // distinct 过滤重复  -> 基于 hashCode和equals方法
    public void test1(){
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(12);
        integers.add(1);
        integers.add(2);
        integers.add(10);
        integers.add(12);
        Stream<Integer> stream = integers.stream();
        Stream<Integer> distinct_stream = stream.distinct();
        distinct_stream.forEach(x -> System.out.print(x + "\t"));
//        distinct_stream.forEach(System.out::println);
    }

    @Test
    // limit 截断流  使元素的数量不超过参数数量
    //  显示从 开始 到 limit 结束
    public void test2(){
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(12);
        integers.add(1);
        integers.add(2);
        integers.add(10);
        integers.add(12);
        Stream<Integer> stream = integers.stream();
        Stream<Integer> limit = stream.limit(3);
        limit.forEach(x -> System.out.print(x + "\t"));
    }

    @Test
    // skip 跳过流 使的元素跳过参数个数量的数据
    //    显示从 skip开始 到 结束
    public void test3(){
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(12);
        integers.add(1);
        integers.add(2);
        integers.add(10);
        integers.add(12);
        Stream<Integer> stream = integers.stream();
        Stream<Integer> skip = stream.skip(3);
        skip.forEach(x -> System.out.print(x + "\t"));
    }

    @Test
    // map 映射流
    public void test4(){
        ArrayList<String> list = new ArrayList<>();
        list.add("aa");
        list.add("bb");
        list.add("cc");
        list.add("dd");
        list.add("aa");
        Stream<String> stream = list.stream();
//        Stream<Map> mapStream = stream.map(new Function<String, Map>() {
//            @Override
//            public Map apply(String s) {
//                HashMap<String, Integer> map = new HashMap<>();
//                map.put(s, 1);
//                return map;
//            }
//        });
        Stream<String> mapStream = stream.map(x -> x + ",1");
        mapStream.forEach(System.out::println);
    }

    @Test
    // sorted 排序
    public void test5(){
        ArrayList<String> list = new ArrayList<>();
        list.add("aa");
        list.add("bb");
        list.add("cc");
        list.add("dd");
        list.add("aa");
        Stream<String> stream = list.stream();
        Stream<String> sorted = stream.sorted();
        sorted.forEach(System.out::println);
//        List<String> collect = sorted.collect(Collectors.toList());
//        for (String s : collect) {
//            System.out.println(s);
//        }
    }
}
