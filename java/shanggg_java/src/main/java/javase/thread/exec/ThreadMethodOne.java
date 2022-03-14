package javase.thread.exec;

public class ThreadMethodOne {
    public static void main(String[] args) {
        MyTheardFour myTheardFour = new MyTheardFour();
        myTheardFour.start();
        MyTheardThree myTheardThree = new MyTheardThree(myTheardFour);
        myTheardThree.start();
    }
}

class MyTheardThree extends Thread {
    private MyTheardFour myTheardFour;
    public MyTheardThree(MyTheardFour myTheardFour) {
        this.myTheardFour = myTheardFour;
    }
    @Override
    public void run() {
        for (int i = 1; i <= 50; i++) {
            if(i == 5){
                try {
//                    myTheardFour.join();
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(this.getName() + "\t第一个线程在运行~~~~~~~~~~~~" + i);
        }
    }
}

class MyTheardFour extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 50; i++) {
            System.out.println(this.getName() + "\t第二个线程在运行~~~~~~~~~~~~" + i);
        }
    }
}
