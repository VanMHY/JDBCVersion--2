package dao.junit;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import org.junit.Test;

import bean.Customer;
import dao.CustomerDAOImpl;
import util.JDBCutil;
import util2.JDBCUtils;

public class CustomerDAOImplTest {
	private CustomerDAOImpl dao = new CustomerDAOImpl();
	@Test
	public void testInsert() {
		Connection conn = null;
		try {
			conn = JDBCutil.getConnection();
			Customer cust = new Customer(1, "小刘", "xiaoliu@126.com", new Date(43534646435L));
			dao.insert(conn, cust);
			System.out.println("添加ok");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutil.closeResource(conn, null);
		}
	}

	@Test
	public void testDeleteById() {
		Connection conn = null;
		try {
			conn = JDBCutil.getConnection();
			dao.deleteById(conn, 3);
			System.out.println("删除ok");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutil.closeResource(conn, null);
		}
	}

	@Test
	public void testUpdateConnectionCustomer() {
		Connection conn = null;
		try {
			conn = JDBCutil.getConnection();
			dao.deleteById(conn, 2);
			Customer cust = new Customer(2, "", "", new Date(43551667L));
			dao.update(conn, cust);
			System.out.println("修改ok");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutil.closeResource(conn, null);
		}
	}

	@Test
	public void testGetCustomerById() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection3();
			Customer cust = dao.getCustomerById(conn, 3);
			System.out.println(cust);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutil.closeResource(conn, null);
		}
	}

	@Test
	public void testGetAll() {
		Connection conn = null;
		try {
			conn = JDBCutil.getConnection();
			List<Customer> list = dao.getAll(conn);
			list.forEach(System.out :: println);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutil.closeResource(conn, null);
		}
	}

	@Test
	public void testGetCount() {
		Connection conn = null;
		try {
			conn = JDBCutil.getConnection();
			Long count = dao.getCount(conn);
			System.out.println("表中的记录数为" + count);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutil.closeResource(conn, null);
		}
	}

	@Test
	public void testGetMaxBirth() {
		Connection conn = null;
		try {
			conn = JDBCutil.getConnection();
			Date maxBirth = dao.getMaxBirth(conn);
			System.out.println("最大生日为：" + maxBirth);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutil.closeResource(conn, null);
		}
	}

}
