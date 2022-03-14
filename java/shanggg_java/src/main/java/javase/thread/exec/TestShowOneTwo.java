package javase.thread.exec;

public class TestShowOneTwo {
    public static void main(String[] args) {
        ShowNumberOne showNumberOne = new ShowNumberOne();
        Thread thread1 = new Thread(showNumberOne);
        Thread thread2 = new Thread(showNumberOne);

        thread1.start();
        thread2.start();
    }
}

class ShowNumberOne implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            synchronized (this) {
                this.notifyAll();
                System.out.println(Thread.currentThread().getName() + "\t线程正在打印~~~~~~~~~~" + i);
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}