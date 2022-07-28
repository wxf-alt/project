package hbase.operation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Auther: wxf
 * @Date: 2022/5/26 19:27:30
 * @Description: PutsHbaseData
 * @Version 1.0.0
 */
@SuppressWarnings("ALL")
public class PutsHbaseData {
    // 定义配置对象
    private static Configuration conf = HBaseConfiguration.create();
    // 定义表名
    private static TableName tableName = TableName.valueOf("myns1:table5");

    public static void main(String[] args) throws IOException {
        // 创建Hbase的连接对象
        Connection conn = ConnectionFactory.createConnection(conf);;
        // 创建Hbase的 表操作对象
        HTable table = (HTable) conn.getTable(tableName);

        // 开始保存数据
        Put row1 = new Put(Bytes.toBytes("p2"));
        // 设置这行的数据 列族 列 值
        row1.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes("孙建国"));
        row1.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("sex"), Bytes.toBytes("1"));
        row1.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("birtyday"), Bytes.toBytes("1975-06-05"));
        row1.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("np"), Bytes.toBytes("北京市"));

        Put row2 = new Put(Bytes.toBytes("p3"));
        // 设置这行的数据 列族 列 值
        row2.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes("王小花"));
        row2.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("sex"), Bytes.toBytes("0"));
        row2.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("birtyday"), Bytes.toBytes("1998-07-16"));
        row2.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("np"), Bytes.toBytes("湖南省"));

        Put row3 = new Put(Bytes.toBytes("p4"));
        // 设置这行的数据 列族 列 值
        row3.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes("赵佳佳"));
        row3.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("sex"), Bytes.toBytes("0"));
        row3.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("birtyday"), Bytes.toBytes("1997-04-03"));
        row3.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("np"), Bytes.toBytes("河北省"));

        ArrayList<Put> puts = new ArrayList<>();
        puts.add(row1);
        puts.add(row2);
        puts.add(row3);

        // 添加这条数据
        table.put(puts);
        System.out.println("msg : Datas is Inserted");

        // 释放资源
        if(null != table){
            table.close();
        }
        if(null != conn){
            conn.close();
        }
    }

}