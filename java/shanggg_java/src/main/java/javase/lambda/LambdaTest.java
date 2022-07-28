package javase.lambda;

import org.junit.Test;

public class LambdaTest {

    @Test
    public void test1(){
        // 匿名内部类的形式
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("aa");
            }
        };
        runnable.run();
        System.out.println("================================");
        // Lambda表达式调用 无参 无返回值
        Runnable runnable1 = () -> System.out.println("bb");
        runnable1.run();

        System.out.println("===============================");
        // Lambda表达式调用 有参 无返回值
//        MyInter1 myInter1 = (a) -> System.out.println("输入的参数是：" + a);
        MyInter1 myInter1 = a -> System.out.println("输入的参数是：" + a);
        myInter1.methon("string");

        System.out.println("===============================");
        // Lambda表达式调用 有参 有返回值
//        MyInter2 myInter2 = (a, b) -> {return a + b;};
        // 简化
        MyInter2 myInter2 = (a, b) -> a + b;
        System.out.println(myInter2.methon("aa", 13));
    }
}

interface MyInter1{
    public void methon(String a);
}

interface MyInter2{
    public String methon(String a,Integer b);
}

