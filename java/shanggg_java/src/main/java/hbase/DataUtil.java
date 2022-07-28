package hbase;

import it.unimi.dsi.fastutil.bytes.Byte2ReferenceSortedMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

// 数据的增删改查,需要使用的是 Table
@SuppressWarnings("ALL")
public class DataUtil {

    public static Logger logger = LoggerFactory.getLogger(NameSpaceUtil.class);

    // 获取表的Table的对象
    public static Table getTable(Connection conn, String nameSpace, String tableName) throws IOException {
        // 验证表名是否合法
        TableName checkTableName = TableUtil.checkTableName(nameSpace, tableName);
        if (checkTableName == null) {
            return null;
        }
        Table table = conn.getTable(checkTableName);
        return table;
    }

    // Put 数据
    public static void Put(Connection conn, String nameSpace, String tableName, String rowkey, String columnFamily, String columnQualifier, String value) throws IOException {
        // 获取表对象
        Table table = getTable(conn, nameSpace, tableName);
        if (table == null) {
            return;
        }
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier), Bytes.toBytes(value));
        table.put(put);

        table.close();
    }

    // Get 数据
    public static void Get(Connection conn, String nameSpace, String tableName, String rowkey) throws IOException {
        // 获取表对象
        Table table = getTable(conn, nameSpace, tableName);
        if (table == null) {
            return;
        }
        Get get = new Get(Bytes.toBytes(rowkey));
        // 设置查询某个列  还有其他方法 set  add 等
//        get.addColumn();
        Result result = table.get(get);
        parseResult(result);

        table.close();
    }

    public static void parseResult(Result result) {
        if (result != null) {
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {
                System.out.print("rowkey : " + new String(CellUtil.cloneRow(cell)) + "\t");
                System.out.print("列族 : " + new String(CellUtil.cloneFamily(cell)) + "\t");
                System.out.print("列 : " + new String(CellUtil.cloneQualifier(cell)) + "\t");
                System.out.print("值 : " + new String(CellUtil.cloneValue(cell)) + "\t");
                System.out.print("版本 : " + cell.getTimestamp() + "\t\n");
//                System.out.println(
//                        "列族" + Bytes.toString(CellUtil.cloneFamily(cell)) +
//                        "列名" + Bytes.toString(CellUtil.cloneQualifier(cell)) +
//                        "值" + Bytes.toString(CellUtil.cloneValue(cell)));

            }
        }
    }

    // Scan 数据
    public static void Scan(Connection conn, String nameSpace, String tableName) throws IOException {
        // 获取表对象
        Table table = getTable(conn, nameSpace, tableName);
        if (table == null) {
            return;
        }

        Scan scan = new Scan();
//        QualifierFilter qualifierFilter = new QualifierFilter(CompareFilter.CompareOp.EQUAL,new BinaryComparator(Bytes.toBytes("")));
//        QualifierFilter qualifierFilter = new QualifierFilter(CompareFilter.CompareOp.EQUAL,new BinaryComparator(Bytes.toBytes("")));
//        scan.setFilter(qualifierFilter);
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result : resultScanner) {
            parseResult(result);
        }
//        Iterator<Result> resultIterator = resultScanner.iterator();
//        if (resultIterator.hasNext()) {
//            Result result = resultIterator.next();
//            parseResult(result);
//        }
        table.close();
    }


    // delete 数据
    public static void Delete(Connection conn, String nameSpace, String tableName, String rowkey, String cfs, String... cf) throws IOException {
        // 获取表对象
        Table table = getTable(conn, nameSpace, tableName);
        if (table == null) {
            return;
        }

        Delete delete = new Delete(Bytes.toBytes(rowkey));
        if (cf != null) {
            for (int i = 0; i < cf.length; i++) {
                // 为此列最新的cell,添加一条type=DELETE 的删除标记  如果有历史版本无法删除
//                delete.addColumn(Bytes.toBytes(cfs), Bytes.toBytes(cf[i]));

                // 删除 指定列所有版本的数据  添加一条type=DeleteColumn 的标记
                delete.addColumns(Bytes.toBytes(cfs), Bytes.toBytes(cf[i]));
            }
        }
        table.delete(delete);

        table.close();
    }

}
