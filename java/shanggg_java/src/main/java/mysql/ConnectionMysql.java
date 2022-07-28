package mysql;

import mysql.utils.JDBCUtilsDruid2;
import mysql.utils.JDBCUtilsProperties;
import org.junit.Test;

import java.io.IOException;
import java.sql.*;

@SuppressWarnings("ALL")
public class ConnectionMysql {

    @Test
    // 测试查询数据
    public void test() {
        Connection conn = null;
        Statement st = null;
        ResultSet resultSet = null;
        try {
            // 1.加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 2.获取Connection对象
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/myemployees?user=root&password=root&characterEncoding=utf-8");
            // 3.编写sql语句
            String sql_query = "select employee_id,first_name,last_name,salary from employees";
            // 4.创建Statement 对象
            st = conn.createStatement();
            // 5.执行sql语句
            resultSet = st.executeQuery(sql_query);

            // 6.遍历结果集
            while (resultSet.next()) {
                int employee_id = resultSet.getInt("employee_id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                double salary = resultSet.getDouble("salary");
                System.out.println(employee_id + "\t" + first_name + "\t" + last_name + "\t" + salary);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // 7.关闭连接
            try {
                resultSet.close();
                st.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }

    @Test
    // 测试新增数据
    public void test2() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myemployees?user=root&password=root&characterEncoding=utf-8");
        String sql = "insert into employees(employee_id,first_name,last_name,salary) values(?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, 10010120);
        statement.setString(2, "W");
        statement.setString(3, "xf");
        statement.setDouble(4, 15000);

        int i = statement.executeUpdate();
        if (i == 1) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
    }

    @Test
    // 测试删除
    public void test3() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myemployees?user=root&password=root&characterEncoding=utf-8");
        String sql = "delete from employees where employee_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, 10010120);

        int i = statement.executeUpdate();
        if (i == 1) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
    }

    @Test
    // 测试使用 Properties 读取配置
    public void test4() throws ClassNotFoundException, SQLException {

        // 使用utils获取connection
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = JDBCUtilsProperties.getConn();
            String sql = "insert into employees(employee_id,first_name,last_name,salary) values(?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, 10010120);
            statement.setString(2, "W");
            statement.setString(3, "xf");
            statement.setDouble(4, 15000);

            int i = statement.executeUpdate();
            if (i == 1) {
                System.out.println("成功");
            } else {
                System.out.println("失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            JDBCUtilsProperties.closeAll(connection, statement, null);
        }
    }

    @Test
    // 测试 德鲁伊读取配置文件
    public void test5() throws Exception {

        Connection conn = JDBCUtilsDruid2.getConn();
        String sql = "insert into employees(employee_id,first_name,last_name,salary) values(?,?,?,?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, 10010120);
        statement.setString(2, "W");
        statement.setString(3, "xf");
        statement.setDouble(4, 15000);

        conn.setAutoCommit(false); // 设置自动提交关闭

        conn.commit(); // 提交事务

        conn.rollback(); // 回滚事务


        int i = statement.executeUpdate();
        if (i == 1) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
    }


}
