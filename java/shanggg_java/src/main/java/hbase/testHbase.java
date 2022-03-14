package hbase;

import org.apache.hadoop.hbase.client.Connection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("ALL")
public class testHbase {

    private Connection conn;

    @Before
    public void init() {
        try {
            conn = ConnectionUtil.getConn();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void close() {
        try {
            ConnectionUtil.close(conn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNameSpace() throws IOException {
        List<String> list = NameSpaceUtil.listNameSpace(conn);
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void testExistsNameSpace() throws IOException {
//        String nameSpace = "wxf1";
        String nameSpace = "default";
        System.out.println(NameSpaceUtil.existsNameSpace(conn, nameSpace) ? nameSpace + "\n存在" : nameSpace + "\n不存在");
    }

    @Test
    public void testCreateNameSpace() throws IOException {
        String nameSpace = "idea";
        System.out.println(NameSpaceUtil.createNameSpace(conn, nameSpace) ? nameSpace + "\t创建成功" : nameSpace + "\t创建失败");
    }

    @Test
    public void testListNameSpaceTable() throws IOException {
        String nameSpace = "wxf";
        List<String> strings = NameSpaceUtil.listNameSpaceTable(conn, nameSpace);
        System.out.println(strings);
    }

    @Test
    public void testDeleteNameSpace() throws IOException {
        String nameSpace = "idea";
        System.out.println(NameSpaceUtil.deleteNameSpace(conn, nameSpace));
    }


    @Test
    public void testExistsTable() throws IOException {
        String nameSpace = "wxf";
        String tableName = "table1";
        System.out.println(TableUtil.existsTable(conn, nameSpace, tableName));
    }

    @Test
    public void testCreateTable() throws IOException {
        String nameSpace = "wxf";
        String tableName = "table2";
        System.out.println(TableUtil.createTable(conn, nameSpace, tableName,"cf1","cf2"));
    }

    @Test
    public void testDropTable() throws IOException {
        String nameSpace = "wxf";
        String tableName = "table2";
        System.out.println(TableUtil.dropTable(conn, nameSpace, tableName));
    }



    @Test
    public void testPut() throws IOException {
        String nameSpace = "wxf";
        String tableName = "table1";
        DataUtil.Put(conn,nameSpace,tableName,"a1","cf1","name","jack");
    }

    @Test
    public void testGet() throws IOException {
        String nameSpace = "wxf";
        String tableName = "table1";
        DataUtil.Get(conn,nameSpace,tableName,"r1");
    }

    @Test
    public void testScan() throws IOException {
        String nameSpace = "wxf";
        String tableName = "table1";
        String rowkey = "r1";
        DataUtil.Scan(conn,nameSpace,tableName);
    }

    @Test
    public void testDelete() throws IOException {
        String nameSpace = "wxf";
        String tableName = "table1";
        String rowkey = "r1";
        DataUtil.Delete(conn,nameSpace,tableName,rowkey,"cf1","age","sex");
    }

}
