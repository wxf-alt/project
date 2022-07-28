package hbase.operation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @Auther: wxf
 * @Date: 2022/5/26 19:31:45
 * @Description: GetHbaseData
 * @Version 1.0.0
 */
public class GetHbaseData {
    // 定义配置对象
    private static Configuration conf = HBaseConfiguration.create();
    // 定义表名
    private static TableName tableName = TableName.valueOf("myns1:table5");

    public static void main(String[] args) throws IOException {
        // 创建Hbase的连接对象
        Connection conn = ConnectionFactory.createConnection(conf);;
        // 创建Hbase的 表操作对象
        HTable table = (HTable) conn.getTable(tableName);
        // 创建查询对象
        Get get = new Get(Bytes.toBytes("p3"));
        get.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("name"));
        get.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("np"));
//        // 设置版本
//        get.setMaxVersions(3);

        // 获取结果
        Result result = table.get(get);
        Cell[] cells = result.rawCells();
        for (Cell c : cells) {
            System.out.print("列族 : " + new String(CellUtil.cloneFamily(c)) + "\t");
            System.out.print("列 : " + new String(CellUtil.cloneQualifier(c)) + "\t");
            System.out.print("值 : " + new String(CellUtil.cloneValue(c)) + "\t");
            System.out.println("版本 : " + c.getTimestamp());
        }
        System.out.println("msg : One Data is geted!");
        // 释放资源
        if(null != table){
            table.close();
        }
        if(null != conn){
            conn.close();
        }
    }

}