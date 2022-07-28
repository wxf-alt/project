package hbase.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @Auther: wxf
 * @Date: 2022/5/27 10:43:33
 * @Description: 根据某一列的值进行过滤
 * @Version 1.0.0
 */
public class PageFilterTest {

    // 定义配置对象
    private static Configuration conf = HBaseConfiguration.create();
    // 定义表名
    private static TableName mytable = TableName.valueOf("myns1:table5");

    public static void main(String[] args) throws IOException {
        // 获取Hbase的连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        // 根据表名获取Hbase表对象
        HTable table = (HTable) conn.getTable(mytable);

        Scan scan = new Scan();
        // 设置分页参数
        int totalRowCount = 0;  // 总行数
        int pageSize = 2;    // 一次展示2行

        PageFilter pageFilter = new PageFilter(pageSize);
        scan.setFilter(pageFilter);

        // 定义startrow
        byte[] tmprow = null;
        byte[] pb = Bytes.toBytes("z");
        ResultScanner scanner = null;
        // 定义循环变量
        Cell[] cells = null;
        // 定义每次循环获取的数据量
        int scanRowCount = 0;
        while (true) {
            // 设置每次的起始startrow
            // 每次起始的stratrow的设置规则必须要大于本次的所有rowkey 并且小于下一次的第一个rowkey
            // rowkey  1 2 3 4 5 6 7 8 9
            // 这样能够保证每次查询肯定不会出现之前的数据从而造成重复
            if (null != tmprow) {
                // 计算rowkey
                tmprow = Bytes.add(tmprow, pb);
                System.out.println("startrow : " + Bytes.toString(tmprow));
                // 设置每行的起始rowkey
                scan.setStartRow(tmprow);
            }

            // 定义每次查询的行数(用作是否退出的判断条件)
            scanRowCount = 0;
            // 获取数据
            scanner = table.getScanner(scan);
            // 遍历数据
            for (Result result : scanner) {
                cells = result.rawCells();
                for (Cell cell : cells) {
                    System.out.print("列族 : " + new String(CellUtil.cloneFamily(cell)) + "\t");
                    System.out.print("列 : " + new String(CellUtil.cloneQualifier(cell)) + "\t");
                    System.out.print("值 : " + new String(CellUtil.cloneValue(cell)) + "\t");
                    System.out.print("列族时间戳 : " + cell.getTimestamp() + "\t\n");
                }
                // 计数器叠加
                scanRowCount++;
                totalRowCount++;
                // 获取当先循环的最后一行
                tmprow = result.getRow();
            }
            // 执行判断条件
            if (scanRowCount == 0) {
                // 退出while循环
                break;
            }
        }
        // 展示消息
        System.out.println("msg : The Data is queryed! TotalRowCount : " + totalRowCount);
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