package zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("ALL")
public class ZookeeperTest {

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

            }
        });
//        System.out.println(zooKeeper);
    }

    @Test
    public void ls() throws KeeperException, InterruptedException {
        // 获取 zookeeper 中路径下的节点
        List<String> children = zooKeeper.getChildren("/", null);
        System.out.println(children);

        // 获取 zookeeper 中路径的属性信息
        Stat stat = new Stat();
        List<String> children1 = zooKeeper.getChildren("/hadoop-ha", null, stat);
        System.out.println(children1);
        // 获取所有属性信息
        System.out.println(stat);
    }

    @Test
    public void create() throws KeeperException, InterruptedException {
        String s = zooKeeper.create("/idea", "hello\nsadf".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(s);
    }

    @Test
    public void get() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData("/idea", null, new Stat());
        System.out.println(new String(data));
    }

    @Test
    public void set() throws KeeperException, InterruptedException {
        // 设置 版本号为 -1 , 会忽略版本号的检查
        zooKeeper.setData("/idea", "hi".getBytes(), -1);
    }

    @Test
    public void delete() throws KeeperException, InterruptedException {
        zooKeeper.delete("/idea", -1);
    }

    @Test
    public void rmr() throws KeeperException, InterruptedException {
        // 要删除的节点路径
        String path = "/idea1";
        // 获取当前路径中的所有子节点
        List<String> children = zooKeeper.getChildren(path, null);
        for (String child : children) {
            zooKeeper.delete(path + "/" + child, -1);
        }
        zooKeeper.delete(path, -1);
    }

    // 判断节点 是否存在
    @Test
    public void exsits() throws KeeperException, InterruptedException {
        Stat exists1 = zooKeeper.exists("/idea", null);
        System.out.println(exists1 == null ? "/idea 不存在" : "/idea 存在");
        Stat exists2 = zooKeeper.exists("/zookeeper", null);
        System.out.println(exists2 == null ? "/zookeeper 不存在" : "/zookeeper 存在");
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
}
