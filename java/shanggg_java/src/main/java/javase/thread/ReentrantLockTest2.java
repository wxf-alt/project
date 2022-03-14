package javase.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1. 如果通过 ReentrantLock 实现线程安全，那么必须s使用 Condition 实现线程通信(lock.newCondition())
 * 2.notify 唤醒是随机唤醒；使用 condition.signal() 可以精确唤醒
 * 3.signal() 线程唤醒
 * 4.await() 线程等待
 */
@SuppressWarnings("ALL")
public class ReentrantLockTest2 {
    public static void main(String[] args) {
        MyReentrantLock myReentrantLock = new MyReentrantLock();
        Thread thread1 = new Thread(myReentrantLock,"窗口一");
        Thread thread2 = new Thread(myReentrantLock,"窗口二");
        thread1.start();
        thread2.start();
    }
}


class MyReentrantLock implements Runnable{

    private int ticket = 100;
    private ReentrantLock lock = new ReentrantLock();
    // 使用这个进行线程通信
    private Condition condition = lock.newCondition();

    @Override
    public void run() {

        while(true){
            lock.lock();
            try {
                // 先唤醒
                condition.signal();
                if(ticket > 0){
                    System.out.println(Thread.currentThread().getName() + "=============" + ticket);
                    ticket--;
                }else{
                   return;
                }
                // 线程等待
                condition.await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
