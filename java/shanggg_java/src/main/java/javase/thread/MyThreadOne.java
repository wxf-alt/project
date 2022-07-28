package javase.thread;

// 第一种 实现多线程的方式
public class MyThreadOne extends Thread {

    // 需要使用多线程运行的代码 写在 run 方法里面
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            // 获取 正在运行的线程的线程名称
            System.out.println(Thread.currentThread().getName() + "线程正在运行~~~~~~~~~" + i);
        }
    }
}
