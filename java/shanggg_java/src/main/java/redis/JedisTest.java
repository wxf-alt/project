package redis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("ALL")
public class JedisTest {

    @Test
    // 单机 redis操作
    public void test1(){
        // 1.创建 Jedis 对象
        Jedis jedis = new Jedis("nn1.hadoop", 6379);
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            // 获取值
            String value = jedis.get(key);
            System.out.println(key + "===>" + value);
        }
        // 2.关闭Jedis
        jedis.close();
    }

    @Test
    public void test2() throws IOException {
        HostAndPort hostAndPort1 = new HostAndPort("nn1.hadoop", 6379);
//        HostAndPort hostAndPort2 = new HostAndPort("nn2.hadoop", 6379);
//        HostAndPort hostAndPort3 = new HostAndPort("s1.hadoop", 6379);
//        HostAndPort hostAndPort4 = new HostAndPort("s2.hadoop", 6379);
//        HostAndPort hostAndPort5 = new HostAndPort("s3.hadoop", 6379);
//        HostAndPort hostAndPort6 = new HostAndPort("s4.hadoop", 6379);
        HashSet<HostAndPort> hostAndPorts = new HashSet<>();
        hostAndPorts.add(hostAndPort1);
//        hostAndPorts.add(hostAndPort2);
//        hostAndPorts.add(hostAndPort3);
//        hostAndPorts.add(hostAndPort4);
//        hostAndPorts.add(hostAndPort5);
//        hostAndPorts.add(hostAndPort6);

        JedisCluster jedisCluster = new JedisCluster(hostAndPorts,100000);
        String hainiu = jedisCluster.set("hainiu", "15");
        System.out.println(hainiu);

        // 2.关闭Jedis
        jedisCluster.close();
    }

}
