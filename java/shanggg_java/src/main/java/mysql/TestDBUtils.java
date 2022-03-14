package mysql;

import mysql.bean.Employees;
import mysql.utils.JDBCUtilsDruid2;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

// 测试DBUtils 的增删改方法
@SuppressWarnings("ALL")
public class TestDBUtils {
    @Test
    // 添加
    public void test1() throws Exception {
        QueryRunner queryRunner = new QueryRunner();
        Connection conn = JDBCUtilsDruid2.getConn();
        int update = queryRunner.update(conn, "insert into employees(employee_id,first_name,last_name,salary) values(?,?,?,?)", 302, "W", "xf", 15000);
        String result = (update > 0) ? "成功" : "失败";
        System.out.println(result);
    }

    @Test
    // 查询单条记录
    public void test2() throws Exception {
        QueryRunner queryRunner = new QueryRunner();
        Connection conn = JDBCUtilsDruid2.getConn();
        Employees employees = queryRunner.query(conn,
                "select e.employee_id,e.first_name,e.last_name,e.salary from employees e where e.employee_id = ?",
                new BeanHandler<>(Employees.class),
                128);
        System.out.println(employees);
    }

    @Test
    // 查询多条记录
    public void test3() throws Exception {
        QueryRunner queryRunner = new QueryRunner();
        Connection conn = JDBCUtilsDruid2.getConn();
        List<Employees> list = queryRunner.query(conn,
                "select e.employee_id,e.first_name,e.last_name,e.salary from employees e where e.employee_id >= ? and e.employee_id <= ?",
                new BeanListHandler<>(Employees.class),
                125,126);
        for (Employees employees : list) {
            System.out.println(employees);
        }
    }
}
