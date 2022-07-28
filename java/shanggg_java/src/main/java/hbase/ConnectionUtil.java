package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HConnectable;

import java.io.IOException;

// 1.创建和关闭 Connection对象
@SuppressWarnings("ALL")
public class ConnectionUtil {

    // 创建一个 Connection 对象
    public static Connection getConn() throws IOException {

//        Connection conn = ConnectionFactory.createConnection();
        // 可以使用 ConnectionFactory.createConnection() 代替
        Configuration hbaseConf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(hbaseConf);
        return conn;
    }

    // 关闭方法
    public static void close(Connection conn) throws IOException {
        if (conn != null) {
            conn.close();
        }
    }

}
