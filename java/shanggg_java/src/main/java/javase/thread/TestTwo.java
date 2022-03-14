package javase.thread;

// 用于测试自定义线程类
// 需要调用 Thread 的 start 方法 调用 Runnable 的 run 方法
// 传参 Runnable 的对象 到 Thread 的构造方法中
public class TestTwo {
    public static void main(String[] args) {
        MyThreadTwo threadTest = new MyThreadTwo();
        Thread thread = new Thread(threadTest);
        thread.start();

        for (int i = 1; i <= 100; i++) {
            System.out.println(Thread.currentThread().getName() + "线程正在运行==========" + i);
        }
    }
}
