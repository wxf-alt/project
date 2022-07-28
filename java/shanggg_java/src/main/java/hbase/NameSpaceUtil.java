package hbase;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class NameSpaceUtil {

    public static Logger logger = LoggerFactory.getLogger(NameSpaceUtil.class);

    @Test
    // 查所有的名称空间
    public static List<String> listNameSpace(Connection conn) throws IOException {
        ArrayList<String> list = new ArrayList<>();

        // 通过 Admin 来操作库
        Admin admin = conn.getAdmin();

        NamespaceDescriptor[] namespaceDescriptors = admin.listNamespaceDescriptors();
        for (NamespaceDescriptor namespaceDescriptor : namespaceDescriptors) {
            list.add(namespaceDescriptor.getName());
        }

        // 关闭 Admin
        admin.close();

        return list;
    }

    @Test
    // 查库下面有那些表
    public static List<String> listNameSpaceTable(Connection conn, String nameSpace) throws IOException {
        // 验证库名是否合法
        if (StringUtils.isBlank(nameSpace)) {
            logger.error("库名非法");
            return null;
        }

        ArrayList<String> tables = new ArrayList<>();
        // 通过 Admin 来操作库
        Admin admin = conn.getAdmin();

        // 查询当前库所有的表
        HTableDescriptor[] hTableDescriptors = admin.listTableDescriptorsByNamespace(nameSpace);
        for (HTableDescriptor hTableDescriptor : hTableDescriptors) {
            tables.add(hTableDescriptor.getNameAsString());
        }
        return tables;
    }

    @Test
    // 判断库是否存在
    public static Boolean existsNameSpace(Connection conn, String nameSpace) throws IOException {

        // 通过 Admin 来操作库
        Admin admin = conn.getAdmin();

        if (StringUtils.isBlank(nameSpace)) {
            logger.error("库名非法");
            return false;
        }
        try {
            // 根据库名查询对应的 NameSpace,如果找不到就会抛异常
            NamespaceDescriptor namespaceDescriptor = admin.getNamespaceDescriptor(nameSpace);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            admin.close();
        }
    }

    @Test
    // 创建库
    public static Boolean createNameSpace(Connection conn, String nameSpace) throws IOException {

        // 通过 Admin 来操作库
        Admin admin = conn.getAdmin();

        // 验证库名是否合法
        if (StringUtils.isBlank(nameSpace)) {
            logger.error("库名非法");
            return false;
        }
        // 创建库
        try {
            NamespaceDescriptor descriptor = NamespaceDescriptor.create(nameSpace).build();
            admin.createNamespace(descriptor);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            admin.close();
        }
    }

    @Test
    // 删除库
    public static Boolean deleteNameSpace(Connection conn, String nameSpace) throws IOException {
        // 通过 Admin 来操作库
        Admin admin = conn.getAdmin();

        // 验证库名是否合法
        if (StringUtils.isBlank(nameSpace)) {
            logger.error("库名非法");
            return false;
        }
        // 删除库
        List<String> tables = listNameSpaceTable(conn, nameSpace);
        if (tables.size() == 0){
            admin.deleteNamespace(nameSpace);
            admin.close();
            return true;
        }else{
            admin.close();
            logger.error(nameSpace  + "不为空！无法删除");
            return false;
        }
    }

}
