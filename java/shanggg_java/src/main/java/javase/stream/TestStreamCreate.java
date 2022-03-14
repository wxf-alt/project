package javase.stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class TestStreamCreate {

    @Test
    // 1.从集合中获取
    public void test1(){
        ArrayList<String> list = new ArrayList<>();
        list.add("aa_1");
        list.add("bb_2");
        list.add("cc_1");
        list.add("dd_3");
        Stream<String> stream = list.stream();
        Stream<String> stringStream = stream.filter(x -> x.contains("1"));
        stringStream.forEach(System.out::println);

    }

    @Test
    // 2.从数组中获取
    public void test2(){
        Integer [] nums = {20,34,56,78,9,120};
        Stream<Integer> stream = Arrays.stream(nums);
        stream.forEach(System.out::println);
    }

    @Test
    // 3.从散列数据中获取
    public void test3(){
        Stream<String> stream = Stream.of("aa", "bb", "cc", "dd", "ee", "ff");
        stream.forEach(System.out::println);
    }

    @Test
    // 4.获取无限流
    public void test4(){
        Stream<Double> stream = Stream.generate(() -> Math.random());
        stream.forEach(System.out::println);
    }

}
