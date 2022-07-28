package hbase.operation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Auther: wxf
 * @Date: 2022/5/26 17:24:25
 * @Description: CreateHbaseTable
 * @Version 1.0.0
 */
@SuppressWarnings("ALL")
public class CreateHbaseTable {
    // 定义配置对象
    private static Configuration conf = HBaseConfiguration.create();
    private static String nameSpace = "myns1";
    // 定义表名
    private static TableName tablename = TableName.valueOf("myns1:table5");

    @Test
    public void CreateTable() throws IOException {
        // 创建Hbase的连接对象
        org.apache.hadoop.hbase.client.Connection conn = ConnectionFactory.createConnection(conf);;
        // 创建Hbase的管理对象
        HBaseAdmin admin = (HBaseAdmin)conn.getAdmin();
        // 设置表的描述信息
        HTableDescriptor hTableDescriptor = new HTableDescriptor(tablename);
        // 设置列族信息
        HColumnDescriptor cf1 = new HColumnDescriptor("cf1");
        HColumnDescriptor cf2 = new HColumnDescriptor("cf2");
        HColumnDescriptor cf3 = new HColumnDescriptor("cf3");
        hTableDescriptor.addFamily(cf1);
        hTableDescriptor.addFamily(cf2);
        hTableDescriptor.addFamily(cf3);

        ArrayList<String> nameSpaces = new ArrayList<>();
        // 查询所有的 nameSpace
        NamespaceDescriptor[] namespaceDescriptors = admin.listNamespaceDescriptors();
        for (NamespaceDescriptor namespaceDescriptor : namespaceDescriptors) {
            String name = namespaceDescriptor.getName();
            nameSpaces.add(name);
        }

        // 如果不存在 nameSpace 就创建
        if (!(nameSpaces.contains(nameSpace))) {
            admin.createNamespace(NamespaceDescriptor.create(nameSpace).build());
        }

        // 判断一下如果有相同的表就删除
        if (admin.tableExists(tablename)) {
            admin.disableTable(tablename);
            admin.deleteTable(tablename);
        }
        
        admin.createTable(hTableDescriptor);
        System.out.println("msg : Table Create is OK!");

        // 释放资源
        if(null != admin){
            admin.close();
        }
        if(null != conn){
            conn.close();
        }
    }

}