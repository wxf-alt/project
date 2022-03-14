package javase.thread;

// 第二种创建线程的方式  实现 Runnable 接口
public class MyThreadTwo implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            // 获取 正在运行的线程的线程名称
            System.out.println(Thread.currentThread().getName() + "线程正在运行~~~~~~~~~" + i);
        }
    }
}
