package hive;

import java.sql.*;

// 使用 jdbc 方式连接 hive
// hive 必须 开启 hiveserver2 服务
@SuppressWarnings("ALL")
public class HiveJdbc {
    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:hive2://nn1.hadoop:10000", "hadoop", "hadoop");
        // 准备sql
        String sql = "select * from test.demo1";
        // 预编译sql
        PreparedStatement ps = connection.prepareStatement(sql);
        // 执行sql
        ResultSet resultSet = ps.executeQuery();

        // 输出
        while (resultSet.next()) {
            System.out.println(resultSet.getString("name") + resultSet.getInt("age"));
        }
    }
}
