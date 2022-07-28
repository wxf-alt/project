package javase.thread.exec;

class ShowOddNumber implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            if(i % 2 != 0){
                System.out.println( Thread.currentThread().getName() + "\t奇数 = " + i);
            }
        }
    }
}
