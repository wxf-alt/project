package hbase.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @Auther: wxf
 * @Date: 2022/5/27 10:43:33
 * @Description: 根据某一列的值进行过滤
 * @Version 1.0.0
 */
public class SingleColumnValueFilterTest {

    // 定义配置对象
    private static Configuration conf = HBaseConfiguration.create();
    // 定义表名
    private static TableName mytable = TableName.valueOf("myns1:table5");

    public static void main(String[] args) throws IOException {
        // 获取Hbase的连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        // 根据表名获取Hbase表对象
        HTable table = (HTable) conn.getTable(mytable);

        // 创建过滤器
        // 根据给定的列族 列 值 和对比条件来进行条件筛选
        // 参数分别为 列族 列 对比条件 值
        // 一行中有一个满足条件的则整行所有列都返回，（当filter.setFilterIfMissing(true)时才生效）
//        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes("cf1"),
//                Bytes.toBytes("name"),
//                CompareFilter.CompareOp.EQUAL,
//                Bytes.toBytes("赵文明"));

        // 只有 row1 有 book 字段 ，所以输出数据 取决于 setFilterIfMissing 是否为true
        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes("cf1"),
                Bytes.toBytes("book"),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes("I_LIKE"));

        // 设置如果找不到列，是否应过滤整行
        // 如果为true，则如果未找到该列，则跳过整行 不输出该行。
        // 如果为false，则如果找不到该列，则该行将通过 输出该行。这是默认设置
        filter.setFilterIfMissing(true);

        Scan scan = new Scan();
        scan.setFilter(filter);

        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            java.util.List<Cell> cells = result.listCells();
            for (Cell cell : cells) {
                // 打印数据
                System.out.print("行键 : " + new String(CellUtil.cloneRow(cell)) + "\t");
                System.out.print("列族 : " + new String(CellUtil.cloneFamily(cell)) + "\t");
                System.out.print("列: " + new String(CellUtil.cloneQualifier(cell)) + "\t");
                System.out.print("值 : " + new String(CellUtil.cloneValue(cell)) + "\t");
                System.out.print("版本时间戳 : " + cell.getTimestamp() + "\t\n");
            }
        }
        System.out.println("msg : Datas is queryed!");
        // 释放资源
        if(null != scanner){
            scanner.close();
        }
        if(null != table){
            table.close();
        }
        if(null != conn){
            conn.close();
        }
    }

}