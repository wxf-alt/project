package javase.thread;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * Executors.newCachedThreadPool()： 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
 * Executors.newFixedThreadPool(n); 创建一个可定长线程数的线程池，可控制最大并发数，超出的线程会在队列中等待分配。
 * Executors.newSingleThreadExecutor() ：创建一个只有一个线程的线程池，单一线程可以保证所有的任务按照指定的顺序执行。
 * Executors.newScheduledThreadPool(n)：创建一个线程池，它可安排在给定延迟后运行命令或者定期地执行。
 */
@SuppressWarnings("ALL")
public class ThreadPool {
    public static void main(String[] args) {
        // 创建线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + "=========" + i);
                }
            }
        });
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "=========" + i);
        }
    }


    @Test
    public void test1() {
        MyCallable myCallable = new MyCallable();
        FutureTask<String> futureTask = new FutureTask<>(myCallable);
        Thread thread = new Thread(futureTask);
        thread.start();

        // 获取线程的返回值
        try {
            String s = futureTask.get();  // 执行该方法时 会阻塞当前线程
            System.out.println("分线程返回的结果为：" + s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "=======" + i);
        }
    }

}

// 通过 Callable接口 实现线程
// 特点：有返回值
class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "=======" + i);
        }
        return "ccc";
    }
}
