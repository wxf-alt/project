package hbase.operation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * @Auther: wxf
 * @Date: 2022/5/26 20:41:22
 * @Description: DropHbaseTable
 * @Version 1.0.0
 */
public class DropHbaseTable {
    // 定义配置对象
    private static Configuration conf = org.apache.hadoop.hbase.HBaseConfiguration.create();
    // 定义表名
    private static TableName tableName = TableName.valueOf("myns1:table5");

    public static void main(String[] args) throws IOException {
        // 创建Hbase的连接对象
        org.apache.hadoop.hbase.client.Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();

        if (admin.tableExists(tableName)) {
            if (admin.isTableDisabled(tableName)) {
                admin.deleteTable(tableName);
            } else {
                admin.disableTable(tableName);
                admin.deleteTable(tableName);
            }
        }
        // 展示信息
        System.out.println("msg : The Table is deleted!");
        // 释放资源
        if(null != admin){
            admin.close();
        }
        if(null != conn){
            conn.close();
        }
    }

}