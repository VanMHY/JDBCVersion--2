package transaction;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Test;

import util.JDBCutil;

public class TransactionTest {
	// 通用的增删改操作 --version 1.0
	public int upDate(String sql, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// 获取数据库连接
			conn = JDBCutil.getConnection();
			// 预编译sql语句，返回prepareStatement实例
			ps = conn.prepareStatement(sql);
			// 填充占位符
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);// 小心参数申明错误
			}
			// 执行sql
			return ps.executeUpdate();
			// 关闭资源
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				//修改为自动提交数据
				//主要针对使用数据库连接池的使用
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JDBCutil.closeResource(conn, ps);
		}
		return 0;
	}
	//***********************考虑到事务后的转账操作**********************
	@Test
	public void testUpdateWithTx(){
		Connection conn = null;
		try {
			conn = JDBCutil.getConnection();
			//System.out.println(conn.getAutoCommit());//ture
			//取消自动提交
			conn.setAutoCommit(false);
			String sql1 = "update user_table set balance = balance - 100 where user = ?";
			update(conn, sql1, "AA");
			System.out.println(10 / 0);
			String sql2 = "update user_table set balance = balance + 100 where user = ?";
			update(conn, sql2, "BB");
			System.out.println("转账成功");
			//提交数据
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			//回滚数据
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			
			JDBCutil.closeResource(conn, null);
		}
		
	}
	//通用的增删改操作 --version 2.0
	public int update(Connection conn,String sql, Object... args) {
		PreparedStatement ps = null;
		try {
			// 预编译sql语句，返回prepareStatement实例
			ps = conn.prepareStatement(sql);
			// 填充占位符
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);// 小心参数申明错误
			}
			// 执行sql
			return ps.executeUpdate();
			// 关闭资源
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCutil.closeResource(null, ps);
		}
		return 0;
	}
	//******************* *****************************
	@Test
	public void testTransactionSelect() throws Exception {
		Connection conn = JDBCutil.getConnection();
		//获取当前连接的隔离级别
		System.out.println(conn.getTransactionIsolation());
		//设置数据库隔离级别
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		//取消自动提交数据
		conn.setAutoCommit(false);
		String sql = "select user,password,balance from user_table where user = ?";
		User user = getInstance(conn, User.class, sql, "CC");
		System.out.println(user);
	}
	@Test
	public void testTransactionUpdate() throws Exception {
		Connection conn = JDBCutil.getConnection();
		//取消自动提交数据
		conn.setAutoCommit(false);
		String sql = "update user_table set balance = ? where user = ?";
		update(conn, sql, 5000, "CC");
		Thread.sleep(15000);
		System.out.println("修改结束");
	}
	/**
	 * 实现针对不同表的通用的一条查询操作(version--2.0:考虑上事务)
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public <T> T getInstance(Connection conn,Class<T> clazz, String sql, Object... args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			// 执行，获取结果集
			rs = ps.executeQuery();
			// 获取结果集元数据
			ResultSetMetaData rsmd = rs.getMetaData();
			// 获取列数
			int columnCount = rsmd.getColumnCount();
			if (rs.next()) {
				T t = clazz.newInstance();
				for (int i = 0; i < columnCount; i++) {
					// 获取每个列的列值:通过ResultSet
					Object columnValue = rs.getObject(i + 1);
					// 获取每个列的列名：通过ResultSetMetaData
					// String columnName = rsmd.getColumnName(i + 1);
					// 获取列的别名:getColumnLabel
					String columnLabel = rsmd.getColumnLabel(i + 1);

					// 通过反射,将对象指定名columnName的属性赋值为指定的columnValue
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columnValue);
				}
				return t;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			JDBCutil.closeResource(null, ps, rs);
		}
		return null;
	}
}
