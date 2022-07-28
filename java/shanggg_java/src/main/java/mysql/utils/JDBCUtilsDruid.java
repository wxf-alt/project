package mysql.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 使用数据库连接池
 *      DBCP 阿帕奇的产品
 *      C3P0 个人产品
 *      Druid 德鲁伊  阿里巴巴
 */
@SuppressWarnings("ALL")
public class JDBCUtilsDruid {

    public static Connection getConn() throws ClassNotFoundException, SQLException, IOException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/myemployees");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        dataSource.setInitialSize(10); // 初始化设置连接池中右多少空闲连接
        dataSource.setMaxActive(20);  // 连接池最大链接数
        dataSource.setMinIdle(1);  // 连接池最小链接数

        dataSource.setMaxWait(1000); // 获取连接时最大等待时间 毫秒
        dataSource.setTimeBetweenConnectErrorMillis(60000); // 多久进行一次检查。检测需要关闭的空闲连接  毫秒
        dataSource.setMinEvictableIdleTimeMillis(300000); // 连接在连接池中最小生存的时间  毫秒

        Connection connection = dataSource.getConnection();
        return connection;
    }

    public static void closeAll(Connection conn, Statement st, ResultSet rs){
            try {
                if(rs != null) {
                    rs.close();
                }
                if(conn != null) {
                    conn.close();
                }
                if(st != null) {
                    st.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
    }

}
