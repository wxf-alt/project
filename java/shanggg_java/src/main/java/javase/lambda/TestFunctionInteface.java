package javase.lambda;

import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TestFunctionInteface {
    @Test
    // 消费型接口  有参数 没有返回值
    public void test1(){
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        consumer.accept("hello");

        System.out.println("===============lambda 表达式===============");

        Consumer<String> consumer1 = s -> System.out.println(s);
        consumer1.accept("hello");
    }

    @Test
    // 供给型接口 无参数 有返回值
    public void test2(){
        Supplier<String> supplier = new Supplier<String>() {

            @Override
            public String get() {
                return "world";
            }
        };
        System.out.println(supplier.get());

        System.out.println("===============lambda 表达式===============");

        Supplier<String> supplier1  = () -> "world";
        System.out.println(supplier1.get());
    }

    @Test
    // 函数型接口 有参数 有返回值
    public void test3(){
        // 指定 参数类型,返回值类型
        Function<String, Integer> function = new Function<String, Integer>() {

            @Override
            public Integer apply(String s) {
                // 字符串转int
//                return Integer.parseInt(s);
                return Integer.valueOf(s);
            }
        };
        Integer apply = function.apply("50");
        System.out.println(apply);

        System.out.println("==================Lambda 表达式================");

        Function<String, Integer> function1 = (s) -> Integer.parseInt(s);
        Integer apply1 = function1.apply("30");
        System.out.println(apply1);
    }

    @Test
    // 断定型接口 有一个参数,返回值必须是 boolean
    public void test4(){
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                if (s.equals("hello")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        System.out.println(predicate.test("hello"));
        System.out.println(predicate.test("aa"));

        System.out.println("============= Lambda表达式 ==================");

        Predicate<String> predicate1 = (str) -> {
            if (str.equals("hello")) {
                return true;
            } else {
                return false;
            }
        };
        System.out.println(predicate1.test("hello"));
    }

}
