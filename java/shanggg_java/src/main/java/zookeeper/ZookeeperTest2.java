package zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SuppressWarnings("ALL")
public class ZookeeperTest2 {

    private String connectString = "nn1.hadoop:2181,nn2.hadoop:2181,s1.hadoop:2181";
    private int sessionTimeOut = 100000;
    private ZooKeeper zooKeeper;

    // 连接 Zookeeper
    @Before
    public void init() throws IOException {
        // 创建 Zookeeper 客户端对象
        zooKeeper = new ZooKeeper(connectString, sessionTimeOut, new Watcher() {
            // 一旦 watch 观察的path 触发了指定的事件，观察者会通知客户端, 客户端收到通知后 回调 process 方法
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getPath() + "发生了以下事件：" + event.getType());
            }
        });
    }

    @After
    public void close() {
        try {
            if (zooKeeper != null) {
                zooKeeper.close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void ls() throws KeeperException, InterruptedException {
//        // 传入true 模式使用客户端自带观察者(创建客户端时的 watch)
//        List<String> children = zooKeeper.getChildren("/idea", true);
//        System.out.println(children);
//        while (1 == 1) {
//            Thread.sleep(5000);
//            System.out.println("============");
//        }

        // 自定义观察者
        zooKeeper.getChildren("/idea", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getPath() + "发生了以下事件：" + event.getType());
                // 重新查询当前路径下的所有节点
                try {
                    List<String> children = zooKeeper.getChildren(event.getPath(), null);
                    System.out.println(event.getPath() + "下节点信息：" + children);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        while (1 == 1) {
            Thread.sleep(5000);
            System.out.println("============");
        }
    }


    private CountDownLatch cd1 = new CountDownLatch(1);
    @Test
    public void get() throws KeeperException, InterruptedException {
        // 是 Connect 线程调用
        byte[] data = zooKeeper.getData("/idea", new Watcher() {

            // 是 Listener 线程调用
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getPath() + "发生了以下事件：" + event.getType());
                try {
                    System.out.println("查询的结果为: "+ new String(zooKeeper.getData(event.getPath(),false,null)));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cd1.countDown();
            }
        }, null);
        System.out.println("查询的结果为: "+ new String(data));

        // 阻塞当前线程, 当初始化的值变为 0 时,当前线程被唤醒
        cd1.await();
    }



    @Test
    public void testLsAndAlwaysWtch() throws InterruptedException, KeeperException {
        lsAndAlwaysWatchCurrent();
        while(true){
            Thread.sleep(5000);
            System.out.println(Thread.currentThread().getName()+"---->我还活着......");
        }
    }

    @Test
    public void lsAndAlwaysWatchCurrent() throws KeeperException, InterruptedException {
        // 自定义观察者
        zooKeeper.getChildren("/idea", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getPath() + "发生了以下事件：" + event.getType());
                System.out.println( Thread.currentThread().getName() +  "============");
                try {
                    // 递归调用
                    lsAndAlwaysWatchCurrent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



}
