package hbase.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @Auther: wxf
 * @Date: 2022/5/27 10:43:33
 * @Description: 子字符串进行过滤
 * @Version 1.0.0
 */
public class SubstringComparatorFilterTest {

    // 定义配置对象
    private static Configuration conf = HBaseConfiguration.create();
    // 定义表名
    private static TableName mytable = TableName.valueOf("myns1:table5");

    public static void main(String[] args) throws IOException {
        // 获取Hbase的连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        // 根据表名获取Hbase表对象
        HTable table = (HTable) conn.getTable(mytable);

        // 创建字符包含比较器
        SubstringComparator substringComparator = new SubstringComparator("北");

        // 创建过滤器
        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes("cf2"),
                Bytes.toBytes("np"),
                CompareFilter.CompareOp.EQUAL,
                substringComparator);

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