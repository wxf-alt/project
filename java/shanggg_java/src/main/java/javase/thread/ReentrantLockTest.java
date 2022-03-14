package javase.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();
        Thread thread1 = new Thread(myRunnable,"窗口一");
        Thread thread2 = new Thread(myRunnable,"窗口二");
        thread1.start();
        thread2.start();
    }
}

class MyRunnable implements Runnable {
    private int ticket = 10000;
    // 创建 ReentrantLock 对象
    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        while(true){
            lock.lock(); // 使用方法加锁
            try {
                if (ticket > 0) {
                    System.out.println(Thread.currentThread().getName() + "=====" + ticket);
                    ticket--;
                } else {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                lock.unlock();
            }
        }
    }
}
