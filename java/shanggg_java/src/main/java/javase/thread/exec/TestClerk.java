package javase.thread.exec;

class TestClerk {
    public static void main(String[] args) {
        Clerk clerk = new Clerk();
        Producer producer = new Producer(clerk);
        Producer producer1 = new Producer(clerk);
        Consumer consumer = new Consumer(clerk);
        Consumer consumer1 = new Consumer(clerk);
        Consumer consumer2 = new Consumer(clerk);

        producer.start();
        producer1.start();
        consumer.start();
        consumer1.start();
        consumer2.start();
    }
}

class Clerk {
    private int product;

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    // 保存一个商品
    public synchronized void save() {
//        if (product >= 20) { // 只有一个消费和生产线城时有效
        while (product >= 20) { // 有多个生产线程的时候需要使用 while
            try {
                System.out.println("商品已经达到上限");
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        product++;
        System.out.println("生产线程生产一个商品，此时商品数量是：" + getProduct());
        // 唤醒另一个线程
//        this.notify(); // 只有一个消费和生产线城时有效
        this.notifyAll();
    }

    // 消费一个商品
    public synchronized void get() {
//        if (product <= 0) { // 只有一个消费和生产线城时有效
        while (product <= 0) { // 有多个消费线程的时候需要使用 while
            try {
                System.out.println("商品已经空仓");
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        product--;
        System.out.println("消费线程消费一个商品，此时商品数量是：" + getProduct());
//        this.notify(); // 只有一个消费和生产线城时有效
        this.notifyAll();
    }
}


class Producer extends Thread {
    private Clerk clerk;

    public Producer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        while (true) {
            clerk.save();
        }
    }
}

class Consumer extends Thread {
    private Clerk clerk;

    public Consumer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        while (true) {
            clerk.get();
        }
    }
}