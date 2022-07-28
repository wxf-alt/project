package hbase.operation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import java.io.IOException;

/**
 * @Auther: wxf
 * @Date: 2022/5/26 20:10:35
 * @Description: ScanHbaseData
 * @Version 1.0.0
 */
public class ScanHbaseData {
    // 定义配置对象
    private static Configuration conf = HBaseConfiguration.create();
    // 定义表名
    private static TableName tableName = TableName.valueOf("myns1:table5");

    public static void main(String[] args) throws IOException {
        // 创建Hbase的连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        // 创建Hbase的 表操作对象
        HTable table = (HTable) conn.getTable(tableName);

        Scan scan = new Scan();
//        // 设置起始行
//        scan.setStartRow(Bytes.toBytes("p1"));
//        // 设置结束行
//        scan.setStopRow(Bytes.toBytes("p4"));
//        // 添加要查询的列
//        scan.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("age"));
//        // 获取版本
//        scan.setMaxVersions(3);

        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            java.util.List<Cell> cells = result.listCells();
            for (Cell cell : cells) {
                System.out.print("列族 : " + new String(CellUtil.cloneRow(cell)) + "\t");
                System.out.print("列族 : " + new String(CellUtil.cloneFamily(cell)) + "\t");
                System.out.print("列 : " + new String(CellUtil.cloneQualifier(cell)) + "\t");
                System.out.print("值 : " + new String(CellUtil.cloneValue(cell)) + "\t");
                System.out.print("版本 : " + cell.getTimestamp() + "\t\n");
            }
        }
        System.out.println("msg : Table Data is geted!");

        // 释放资源
        if (null != scanner) {
            scanner.close();
        }
        if (null != table) {
            table.close();
        }
        if (null != conn) {
            conn.close();
        }
    }

}