package mysql.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 使用配置文件读取相关信息
 */
@SuppressWarnings("ALL")
public class JDBCUtilsProperties {

    public static Connection getConn() throws ClassNotFoundException, SQLException, IOException {
        Connection conn = null;
        Properties properties = new Properties();
        properties.load(new FileInputStream(new File("E:\\A_data\\3.code\\java\\shanggg\\src\\main\\resources\\jdbc.properties")));
//        properties.setProperty("username","root");
//        properties.setProperty("password","root");
//        properties.setProperty("url","jdbc:mysql://localhost:3306/myemployees");
//        properties.setProperty("driver","com.mysql.jdbc.Driver");
        String driver = properties.getProperty("driver");
        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        Class.forName(driver);
        conn = DriverManager.getConnection(url,username,password);
        return conn;
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
