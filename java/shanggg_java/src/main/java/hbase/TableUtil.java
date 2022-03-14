package hbase;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@SuppressWarnings("ALL")
public class TableUtil {

    public static Logger logger = LoggerFactory.getLogger(NameSpaceUtil.class);

    // 验证 库名和表名
    public static TableName checkTableName(String nameSpace, String tableName) {
        if (StringUtils.isBlank(tableName)) {
            logger.error("表名违法");
            return null;
        }
        return TableName.valueOf(nameSpace, tableName);
    }

    @Test
    // 判断表是否存在
    public static Boolean existsTable(Connection conn, String nameSpace, String tableName) throws IOException {

        // 验证表名和库名
        TableName checkTableName = checkTableName(nameSpace, tableName);
        if (checkTableName == null) {
            return false;
        }

        Admin admin = conn.getAdmin();
        boolean tableExists = admin.tableExists(checkTableName);

        admin.close();
        return tableExists;
    }

    // 创建表
    public static Boolean createTable(Connection conn, String nameSpace, String tableName, String... cfs) throws IOException {

        // 验证表名和库名
        TableName checkTableName = checkTableName(nameSpace, tableName);
        if (checkTableName == null) {
            return false;
        }
        // 至少需要传入一个列族
        if (cfs.length < 1) {
            logger.error("至少需要一个列族");
            return false;
        }

        Admin admin = conn.getAdmin();
        // 创建表的描述
        HTableDescriptor hTableDescriptor = new HTableDescriptor(checkTableName);
        // 添加列族的描述
        for (String cf : cfs) {
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);
            hColumnDescriptor.setMinVersions(3);
            hColumnDescriptor.setMaxVersions(10);
            hTableDescriptor.addFamily(hColumnDescriptor);
        }
        // 创建表
        admin.createTable(hTableDescriptor);

        // 创建预分区 的表
        //  必须 numRegions >= 3
//        admin.createTable(hTableDescriptor, Bytes.toBytes("aaaa"), Bytes.toBytes("dddd"),4);

//        // 自定义rowkey 边界 预分区
//        byte [][] bytes = new byte [4][];
//        bytes[0] = Bytes.toBytes("aaaa");
//        bytes[1] = Bytes.toBytes("bbbb");
//        bytes[2] = Bytes.toBytes("cccc");
//        bytes[3] = Bytes.toBytes("dddd");
//
//        admin.createTable(hTableDescriptor,bytes);

        admin.close();
        return true;
    }

    // 删除表
    public static Boolean dropTable(Connection conn, String nameSpace, String tableName) throws IOException {

        if (!(existsTable(conn, nameSpace, tableName))) {
            return false;
        }
        // 校验表名
        TableName checkTableName = checkTableName(nameSpace, tableName);

        Admin admin = conn.getAdmin();
        // 禁用表
        admin.disableTable(checkTableName);
        // 删除表
        admin.deleteTable(checkTableName);

        admin.close();
        return true;
    }

}
