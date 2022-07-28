package hbase.split_region;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Auther: wxf
 * @Date: 2022/5/30 09:20:35
 * @Description: 添加 HBase 数据,使用随机前缀
 * @Version 1.0.0
 */
@SuppressWarnings("ALL")
public class PutRandomRowKeyDataToHBase {

    @Test
    // 使用 随机RowKey 添加数据
    public void putHbaseData() throws IOException {
        Configuration hbaseConf = CreateSplitRegionTable.getHbaseConf();
        TableName tableName = CreateSplitRegionTable.getTableName();

        Connection conn = ConnectionFactory.createConnection(hbaseConf);
        HTable table = (HTable) conn.getTable(tableName);

        Date date = new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String formatDate = simpleDateFormat.format(date);

        // 开始保存数据   rowKey使用 随机数 + 时间戳拼接
        String rowKey1 = getRandomRowKey() + formatDate;
        System.out.println(rowKey1);
        Put row1 = new Put(Bytes.toBytes(rowKey1));
        // 设置这行的数据 列族 列 值
        row1.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes("孙建国"));
        row1.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("sex"), Bytes.toBytes("1"));
        row1.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("birtyday"), Bytes.toBytes("1975-06-05"));
        row1.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("np"), Bytes.toBytes("北京市"));

        String rowKey2 = getRandomRowKey() + formatDate;
        System.out.println(rowKey2);
        Put row2 = new Put(Bytes.toBytes(rowKey2));
        // 设置这行的数据 列族 列 值
        row2.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes("王小花"));
        row2.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("sex"), Bytes.toBytes("0"));
        row2.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("birtyday"), Bytes.toBytes("1998-07-16"));
        row2.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("np"), Bytes.toBytes("湖南省"));

        String rowKey3 = getRandomRowKey() + formatDate;
        System.out.println(rowKey3);
        Put row3 = new Put(Bytes.toBytes(rowKey3));
        // 设置这行的数据 列族 列 值
        row3.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes("赵佳佳"));
        row3.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("sex"), Bytes.toBytes("0"));
        row3.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("birtyday"), Bytes.toBytes("1997-04-03"));
        row3.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("np"), Bytes.toBytes("河北省"));

        ArrayList<Put> puts = new ArrayList<>();
        puts.add(row1);
        puts.add(row2);
        puts.add(row3);

        // 添加这条数据
        table.put(puts);
        System.out.println("msg : Datas is Inserted");

        // 释放资源
        if(null != table){
            table.close();
        }
        if(null != conn){
            conn.close();
        }

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

}