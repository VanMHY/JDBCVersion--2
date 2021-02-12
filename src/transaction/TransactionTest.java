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
	// ͨ�õ���ɾ�Ĳ��� --version 1.0
	public int upDate(String sql, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// ��ȡ���ݿ�����
			conn = JDBCutil.getConnection();
			// Ԥ����sql��䣬����prepareStatementʵ��
			ps = conn.prepareStatement(sql);
			// ���ռλ��
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);// С�Ĳ�����������
			}
			// ִ��sql
			return ps.executeUpdate();
			// �ر���Դ
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				//�޸�Ϊ�Զ��ύ����
				//��Ҫ���ʹ�����ݿ����ӳص�ʹ��
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JDBCutil.closeResource(conn, ps);
		}
		return 0;
	}
	//***********************���ǵ�������ת�˲���**********************
	@Test
	public void testUpdateWithTx(){
		Connection conn = null;
		try {
			conn = JDBCutil.getConnection();
			//System.out.println(conn.getAutoCommit());//ture
			//ȡ���Զ��ύ
			conn.setAutoCommit(false);
			String sql1 = "update user_table set balance = balance - 100 where user = ?";
			update(conn, sql1, "AA");
			System.out.println(10 / 0);
			String sql2 = "update user_table set balance = balance + 100 where user = ?";
			update(conn, sql2, "BB");
			System.out.println("ת�˳ɹ�");
			//�ύ����
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			//�ع�����
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			
			JDBCutil.closeResource(conn, null);
		}
		
	}
	//ͨ�õ���ɾ�Ĳ��� --version 2.0
	public int update(Connection conn,String sql, Object... args) {
		PreparedStatement ps = null;
		try {
			// Ԥ����sql��䣬����prepareStatementʵ��
			ps = conn.prepareStatement(sql);
			// ���ռλ��
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);// С�Ĳ�����������
			}
			// ִ��sql
			return ps.executeUpdate();
			// �ر���Դ
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
		//��ȡ��ǰ���ӵĸ��뼶��
		System.out.println(conn.getTransactionIsolation());
		//�������ݿ���뼶��
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		//ȡ���Զ��ύ����
		conn.setAutoCommit(false);
		String sql = "select user,password,balance from user_table where user = ?";
		User user = getInstance(conn, User.class, sql, "CC");
		System.out.println(user);
	}
	@Test
	public void testTransactionUpdate() throws Exception {
		Connection conn = JDBCutil.getConnection();
		//ȡ���Զ��ύ����
		conn.setAutoCommit(false);
		String sql = "update user_table set balance = ? where user = ?";
		update(conn, sql, 5000, "CC");
		Thread.sleep(15000);
		System.out.println("�޸Ľ���");
	}
	/**
	 * ʵ����Բ�ͬ���ͨ�õ�һ����ѯ����(version--2.0:����������)
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
			// ִ�У���ȡ�����
			rs = ps.executeQuery();
			// ��ȡ�����Ԫ����
			ResultSetMetaData rsmd = rs.getMetaData();
			// ��ȡ����
			int columnCount = rsmd.getColumnCount();
			if (rs.next()) {
				T t = clazz.newInstance();
				for (int i = 0; i < columnCount; i++) {
					// ��ȡÿ���е���ֵ:ͨ��ResultSet
					Object columnValue = rs.getObject(i + 1);
					// ��ȡÿ���е�������ͨ��ResultSetMetaData
					// String columnName = rsmd.getColumnName(i + 1);
					// ��ȡ�еı���:getColumnLabel
					String columnLabel = rsmd.getColumnLabel(i + 1);

					// ͨ������,������ָ����columnName�����Ը�ֵΪָ����columnValue
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
