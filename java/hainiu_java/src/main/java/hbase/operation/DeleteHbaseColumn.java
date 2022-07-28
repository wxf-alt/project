package hbase.operation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Auther: wxf
 * @Date: 2022/5/26 20:22:20
 * @Description: DeleteHbaseColumn
 * @Version 1.0.0
 */
public class DeleteHbaseColumn {
    // 定义配置对象
    private static Configuration conf = HBaseConfiguration.create();
    // 定义表名
    private static TableName tableName = TableName.valueOf("myns1:table5");

    public static void main(String[] args) throws IOException {
        // 创建Hbase的连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        // 创建Hbase的 表操作对象
        HTable table = (HTable) conn.getTable(tableName);

        ArrayList<byte[]> rowKeyList = new ArrayList<>();
        ResultScanner scanner = table.getScanner(new Scan());
        for (Result result : scanner) {
            java.util.List<Cell> cells = result.listCells();
            for (Cell cell : cells) {
                byte[] bytes = CellUtil.cloneRow(cell);
                rowKeyList.add(bytes);
            }
        }

        ArrayList<Delete> deletes = new ArrayList<>();
        for (byte[] bytes : rowKeyList) {
            Delete delete = new Delete(bytes);
            // 删除最新的一个版本 的 sex 列
            delete.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("sex"));
            deletes.add(delete);
        }

        // 删除所有行的这个字段  不存在就不删除
        // 删除数据
        table.delete(deletes);

        System.out.println("msg : The Column is deleted");
        // 释放资源
        if(null != table){
            table.close();
        }
        if(null != conn){
            conn.close();
        }
    }
}