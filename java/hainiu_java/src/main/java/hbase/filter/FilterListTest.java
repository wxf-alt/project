package hbase.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @Auther: wxf
 * @Date: 2022/5/27 10:43:33
 * @Description: 综合多过滤条件
 * @Version 1.0.0
 */
public class FilterListTest {

    // 定义配置对象
    private static Configuration conf = HBaseConfiguration.create();
    // 定义表名
    private static TableName mytable = TableName.valueOf("myns1:table5");

    public static void main(String[] args) throws IOException {
        // 获取Hbase的连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        // 根据表名获取Hbase表对象
        HTable table = (HTable) conn.getTable(mytable);

        // 查询需求 : 查询大于20岁 或者 名字以赵开头 或者职业中包含公务的人员信息
        // 创建 大于20岁 过滤器
        SingleColumnValueFilter filter1 = new SingleColumnValueFilter(Bytes.toBytes("cf1"),
                Bytes.toBytes("age"),
                CompareFilter.CompareOp.GREATER,
                Bytes.toBytes(20));
        filter1.setFilterIfMissing(true);

        // 创建 名字以赵开头 过滤器
        RegexStringComparator regexStringComparator = new RegexStringComparator("^赵");
        SingleColumnValueFilter filter2 = new SingleColumnValueFilter(Bytes.toBytes("cf1"),
                Bytes.toBytes("name"),
                CompareFilter.CompareOp.EQUAL,
                regexStringComparator);
        filter2.setFilterIfMissing(true);

        // 创建 职业中包含公务 过滤器
        SubstringComparator substringComparator = new SubstringComparator("公务");
        SingleColumnValueFilter filter3 = new SingleColumnValueFilter(Bytes.toBytes("cf1"),
                Bytes.toBytes("job"),
                CompareFilter.CompareOp.EQUAL,
                substringComparator);
        filter3.setFilterIfMissing(true);

        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        filterList.addFilter(filter1);
        filterList.addFilter(filter2);
        filterList.addFilter(filter3);

        Scan scan = new Scan();
        scan.setFilter(filterList);

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