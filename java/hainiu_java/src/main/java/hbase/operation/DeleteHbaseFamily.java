package hbase.operation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @Auther: wxf
 * @Date: 2022/5/26 20:38:32
 * @Description: DeleteHbaseFamily
 * @Version 1.0.0
 */
public class DeleteHbaseFamily {
    // 定义配置对象
    private static Configuration conf = HBaseConfiguration.create();
    // 定义表名
    private static TableName tableName = TableName.valueOf("myns1:table5");

    public static void main(String[] args) throws IOException {
        // 创建Hbase的连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        // 指定要删除的列族 删除表的列族信息的时候是不需要下线表的
        admin.deleteColumn(tableName, Bytes.toBytes("cf2"));
        System.out.println("msg : One Column Family is Deleted!");
        // 释放资源
        if(null != admin){
            admin.close();
        }
        if(null != conn){
            conn.close();
        }

    }
}