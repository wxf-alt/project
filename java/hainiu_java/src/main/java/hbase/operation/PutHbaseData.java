package hbase.operation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @Auther: wxf
 * @Date: 2022/5/26 19:21:56
 * @Description: PutHbaseData
 * @Version 1.0.0
 */
@SuppressWarnings("ALL")
public class PutHbaseData {
    // 定义配置对象
    private static Configuration conf = HBaseConfiguration.create();
    // 定义表名
    private static TableName tableName = TableName.valueOf("myns1:table5");

    public static void main(String[] args) throws IOException {
        // 创建Hbase的连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        // 创建Hbase的 表操作对象
        HTable table = (HTable) conn.getTable(tableName);
        // 封装put对象数据，列族一定不为null，其它可以为null，为null的在hbase里不占存储空间
        Put rowKey = new Put(Bytes.toBytes("p1"));
        // 添加这行数据
        rowKey.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes("赵文明"));
        rowKey.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("sex"), Bytes.toBytes("1"));
        rowKey.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("age"), Bytes.toBytes("25"));
        rowKey.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("birthday"), Bytes.toBytes("1995-02-23"));
        rowKey.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("np"), Bytes.toBytes("河北省"));
        // 添加这条数据
        table.put(rowKey);

        System.out.println("msg : One Data is Inserted");
        // 释放资源
        if(null != table){
            table.close();
        }
        if(null != conn){
            conn.close();
        }
    }

}