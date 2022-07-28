package hbase.split_region;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

/**
 * @Auther: wxf
 * @Date: 2022/5/27 16:26:30
 * @Description: 创建 预分区 region 表
 * @Version 1.0.0
 */
@SuppressWarnings("ALL")
public class CreateSplitRegionTable {

    // 定义配置对象
    private static Configuration hbaseConf = HBaseConfiguration.create();
    // 定义表名
    private static TableName tableName = TableName.valueOf("idea:prepartition");
    private static String nameSpace = "idea";

    public static Configuration getHbaseConf() {
        return hbaseConf;
    }

    public static TableName getTableName() {
        return tableName;
    }

    public static String getNameSpace() {
        return nameSpace;
    }

    @Test
    public void createHbaseTable() throws IOException {
        Connection conn = ConnectionFactory.createConnection(hbaseConf);
        HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();

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
        if (admin.tableExists(tableName)) {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        }

        HColumnDescriptor cf1 = new HColumnDescriptor(Bytes.toBytes("cf1"));
        HColumnDescriptor cf2 = new HColumnDescriptor(Bytes.toBytes("cf2"));
        HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
        hTableDescriptor.addFamily(cf1);
        hTableDescriptor.addFamily(cf2);


        byte[][] bytes = getRowKeys(255);
        // 创建 预分区表
//        admin.createTable(hTableDescriptor, Bytes.toBytes("0a"), Bytes.toBytes("9Z"), 150);
        admin.createTable(hTableDescriptor,bytes);

    }

    // 预分 region 使用的 RowKey 数组
    public byte[][] getRowKeys(int regionNum){
        byte [][] bytes = new byte [regionNum][];
        ArrayList<String> rowKeys = new ArrayList<String>();
        HashSet<String> set = new HashSet<String>();

        for (int i = 0; i <= regionNum-1;) {
            String rowKey = getRandomRowKey();
            set.add(rowKey);
            i = set.size();
        }

        for (String s : set) {
//            System.out.print(s + "\t");
            rowKeys.add(s);
        }
//        System.out.println("set 长度" + set.size());

        Collections.sort(rowKeys);
        // 最小的 rowKey
        rowKeys.set(0,"0A");
        // 最大的 rowKey
        rowKeys.set(regionNum -1,"9z");

//        System.out.println("rowKeys: " + rowKeys.toString());
        int index =0;
        for (String rowKey : rowKeys) {
            bytes[index] = Bytes.toBytes(rowKey);
            index += 1;
        }
        return bytes;
    }

    // 获取 随机 RowKey 前缀
    public String getRandomRowKey() {
        // 62
        String bwords[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String bwords2[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M","N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m","n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        // 获取 0-9 的随机整数
        int random = (int) (Math.random()* 10);
        // 获取 0-51 的随机整数
        int random2 = (int) (Math.random()* 52);
        String bword = bwords[random];
        String bword1 = bwords2[random2];
        String rowKey = bword + bword1;
        return rowKey;
    }

    @Test
    // 获取 region 信息
    public void getRegionInfo() throws IOException {
        Connection conn = ConnectionFactory.createConnection(hbaseConf);
        HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
        RegionLocator regionLocator = conn.getRegionLocator(tableName);
        Pair<byte[][], byte[][]> startEndKeys = regionLocator.getStartEndKeys();

        // start RowKey 的数组集合
        byte[][] startKey = startEndKeys.getFirst();
        // end RowKey 的数组集合
        byte[][] endKeys = startEndKeys.getSecond();

        for (int i = 0; i < startKey.length; i++) {
            // 获取 region 所在的 RegionServer
            String hostname = regionLocator.getRegionLocation(startKey[i]).getHostname();
            System.out.println(Bytes.toString(startKey[i]) + ":" + hostname);
        }
    }


}