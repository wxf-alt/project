package mysql.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 使用数据库连接池
 *      DBCP 阿帕奇的产品
 *      C3P0 个人产品
 *      Druid 德鲁伊  阿里巴巴
 */
@SuppressWarnings("ALL")
public class JDBCUtilsDruid2 {

    public static Connection getConn() throws Exception {
        Properties properties = new Properties();
        properties.load(JDBCUtilsDruid2.class.getClassLoader().getResourceAsStream("druid.properties"));
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
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
