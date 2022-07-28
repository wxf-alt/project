package javase.thread;

// 用于测试自定义线程类
public class TestOne {
    public static void main(String[] args) {
        MyThreadOne threadTest = new MyThreadOne();
        threadTest.start();
        for (int i = 1; i <= 100; i++) {
            System.out.println(Thread.currentThread().getName() + "线程正在运行==========" + i);
        }
    }
}
