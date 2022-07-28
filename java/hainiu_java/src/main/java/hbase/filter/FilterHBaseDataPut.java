package hbase.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @Auther: wxf
 * @Date: 2022/5/26 16:15:41
 * @Description: HBaseText
 * @Version 1.0.0
 */
@SuppressWarnings("ALL")
public class FilterHBaseDataPut {

    // 定义配置对象
    private static Configuration conf = HBaseConfiguration.create();
    // 定义表名
    private static TableName mytable = TableName.valueOf("myns1:table5");

    @Test
    public void createTable() throws Exception {
        // 1. 获取连接
        Connection conn = ConnectionFactory.createConnection(conf);
        // 3. 创建hbase表的DDL管理对象
        HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
        // 5. 创建表的描述器  描述一下 这个表示什么样的(主要设置列族信息)
        HTableDescriptor desc = new HTableDescriptor(mytable);

        HColumnDescriptor cf1 = new HColumnDescriptor(Bytes.toBytes("cf1"));
        HColumnDescriptor cf2 = new HColumnDescriptor(Bytes.toBytes("cf2"));
        HColumnDescriptor cf3 = new HColumnDescriptor(Bytes.toBytes("cf3"));


        // 6. 添加列族 hbase的表进行创建的时候必须有两个数据是已知 表名 列族名
        desc.addFamily(cf1);
        desc.addFamily(cf2);
        desc.addFamily(cf3);

        // 7. 添加表的时候会判断一下 这个表是否存在

        if (admin.tableExists(mytable)) {
            // 下线表
            admin.disableTable(mytable);
            // 删除表
            admin.deleteTable(mytable);
            System.out.println("The Old Table is deleted!");
        }
        // 4. 创建表
        admin.createTable(desc);

        System.out.println("The New Table is created!");

        // 2. 释放资源
        if (null != admin) {
            admin.close();
        }
        if (null != conn) {
            conn.close();
        }
        System.out.println("The Conn  is closed!");
    }

    @Test
    public void putSomeDatasToTable() throws Exception {
        // 获取Hbase的连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        // 根据表名获取Hbase表对象
        HTable table = (HTable) conn.getTable(mytable);

        // 开始保存数据
        Put row1 = new Put(Bytes.toBytes("row1"));
        // 设置这行的数据 列族 列 值
        row1.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes("孙建国"));
        row1.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("idcard"), Bytes.toBytes("110102197806055632"));
        row1.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("age"), Bytes.toBytes("41"));
        row1.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("job"), Bytes.toBytes("个体"));

        Put row2 = new Put(Bytes.toBytes("row2"));
        // 设置这行的数据 列族 列 值
        row2.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes("王小花"));
        row2.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("idcard"), Bytes.toBytes("430101199507235569"));
        row2.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("age"), Bytes.toBytes("34"));
        row2.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("job"), Bytes.toBytes("学生"));

        Put row3 = new Put(Bytes.toBytes("row3"));
        // 设置这行的数据 列族 列 值
        row3.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes("赵佳佳"));
        row3.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("idcard"), Bytes.toBytes("130102199311251125"));
        row3.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("age"), Bytes.toBytes("19"));
        row3.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("job"), Bytes.toBytes("学生"));

        Put row4 = new Put(Bytes.toBytes("row4"));
        // 设置这行的数据 列族 列 值
        row4.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes("赵文明"));
        row4.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("idcard"), Bytes.toBytes("130102199012205680"));
        row4.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("age"), Bytes.toBytes("25"));
        row4.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("job"), Bytes.toBytes("公务员"));

        // 开始保存数据
        Put row5 = new Put(Bytes.toBytes("row1")); // 定义rowkey
        // 设置这行的数据 列族 列 值
        row5.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("name"), Bytes.toBytes("孙建国"));
        row5.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("birthday"), Bytes.toBytes("1988-08-19"));
        row5.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("sex"), Bytes.toBytes("1"));
        row5.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("np"), Bytes.toBytes("北京市"));

        Put row6 = new Put(Bytes.toBytes("row2")); // 定义rowkey
        // 设置这行的数据 列族 列 值
        row6.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("name"), Bytes.toBytes("王小花"));
        row6.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("birthday"), Bytes.toBytes("1996-04-12"));
        row6.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("sex"), Bytes.toBytes("0"));
        row6.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("np"), Bytes.toBytes("湖南省"));

        Put row7 = new Put(Bytes.toBytes("row3")); // 定义rowkey
        // 设置这行的数据 列族 列 值
        row7.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("name"), Bytes.toBytes("赵佳佳"));
        row7.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("birthday"), Bytes.toBytes("1993-11-25"));
        row7.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("sex"), Bytes.toBytes("0"));
        row7.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("np"), Bytes.toBytes("河北省"));

        Put row8 = new Put(Bytes.toBytes("row4")); // 定义rowkey
        // 设置这行的数据 列族 列 值
        row8.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("name"), Bytes.toBytes("赵文明"));
        row8.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("birthday"), Bytes.toBytes("1990-12-20"));
        row8.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("sex"), Bytes.toBytes("1"));
        row8.addColumn(Bytes.toBytes("cf2"), Bytes.toBytes("np"), Bytes.toBytes("河北省"));

        // 批量保存

        java.util.List<Put> rows = new ArrayList<Put>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        rows.add(row5);
        rows.add(row6);
        rows.add(row7);
        rows.add(row8);

        // 添加数据
        table.put(rows);
        System.out.println("msg : Datas is Inserted!");

        // 释放资源
        if (null != table) {
            table.close();
        }
        if (null != conn) {
            conn.close();
        }
    }

}