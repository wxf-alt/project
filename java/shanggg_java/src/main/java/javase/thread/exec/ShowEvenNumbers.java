package javase.thread.exec;

/**
 *
 */
class ShowEvenNumbers extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            if(i % 2 == 0){
                System.out.println( Thread.currentThread().getName() + "\t偶数 = " + i);
            }
        }
    }
}
