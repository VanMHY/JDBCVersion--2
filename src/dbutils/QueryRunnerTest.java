package dbutils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import bean.Customer;
import util2.JDBCUtils;

/*
 * commons-dbutils��Apache��֯�ṩ��һ����ԴJDBC�������,��װ����������ݿ����ɾ�Ĳ����
 */
public class QueryRunnerTest {
	// ���Բ���
	@Test
	public void testInsert() {

		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils.getConnection3();
			String sql = "insert into customers(name,email,birth)values(?,?,?)";
			int insertCount = runner.update(conn, sql, "������", "cai@126.com", "1999-8-5");
			System.out.println("����˼�����¼��" + insertCount);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			JDBCUtils.closeResource(conn, null);
		}
	}

	// ���Բ�ѯ
	/*
	 * BeanHandler�������ڲ�ѯһ������
	 */
	@Test
	public void testQuery1() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils.getConnection3();
			String sql = "select id,name,email,birth from customers where id = ?";
			BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
			Customer customer = runner.query(conn, sql, 4, handler);
			System.out.println(customer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			JDBCUtils.closeResource(conn, null);
		}
	}

	/*
	 * BeanListHandler:�����ڲ�ѯ������¼�ļ���
	 * 
	 */
	@Test
	public void testQuery2() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils.getConnection3();
			String sql = "select id,name,email,birth from customers where id < ?";
			BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
			List<Customer> list = runner.query(conn, sql, 4, handler);
			list.forEach(System.out::println);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			JDBCUtils.closeResource(conn, null);
		}
	}

	/*
	 * ScalarHandler:���ڲ�ѯ����ֵ
	 */
	@Test
	public void testQuery3() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils.getConnection3();
			String sql = "select count(*) from customers";
			ScalarHandler handler = new ScalarHandler();
			Long count = (Long) runner.query(conn, sql, handler);
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			JDBCUtils.closeResource(conn, null);
		}
	}

	@Test
	public void testQueryr4() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils.getConnection3();
			String sql = "select id,name,email,birth from customers where id = ?";
			ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {
				@Override
				public Customer handle(ResultSet rs) throws SQLException {
					// return null;
					if (rs.next()) {
						int id = rs.getInt("id");
						String name = rs.getString("name");
						String email = rs.getString("email");
						Date birth = rs.getDate("birth");
						Customer customer = new Customer(id, name, email, birth);
						return customer;
					}
					return null;
				}
			};
			Customer customer = runner.query(conn, sql, 4, handler);
			System.out.println(customer);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			JDBCUtils.closeResource(conn, null);
		}
	}
}
