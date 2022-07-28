package hbase.operation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @Auther: wxf
 * @Date: 2022/5/26 20:19:42
 * @Description: DeleteHbaseRow
 * @Version 1.0.0
 */
public class DeleteHbaseRow {
    // 定义配置对象
    private static Configuration conf = HBaseConfiguration.create();
    // 定义表名
    private static TableName tableName = TableName.valueOf("myns1:table5");

    public static void main(String[] args) throws IOException {
        // 创建Hbase的连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        // 创建Hbase的 表操作对象
        HTable table = (HTable) conn.getTable(tableName);

        Delete delete = new Delete(Bytes.toBytes("p1"));
        table.delete(delete);
        System.out.println("msg One Row is deleted!");
        // 释放资源
        if(null != table){
            table.close();
        }
        if(null != conn){
            conn.close();
        }
    }
}