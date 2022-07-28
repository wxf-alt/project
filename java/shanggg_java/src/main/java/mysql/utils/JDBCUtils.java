package mysql.utils;

import java.sql.*;

@SuppressWarnings("ALL")
public class JDBCUtils {

    public static Connection getConn() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/myemployees?user=root&password=root&characterEncoding=utf-8");
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
