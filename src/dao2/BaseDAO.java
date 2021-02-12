package dao2;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.JDBCutil;

/*
 * DAO: data(base) access object
 * ��װ����������ݱ��ͨ�ò���
 */
public abstract class BaseDAO<T> {
	private Class<T> clazz = null;
//	public BaseDAO() {
//		
//	}
	{
		//��ȡ��ǰBaseDAO������̳и����еķ���
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) genericSuperclass;
		Type[] typeArguments = paramType.getActualTypeArguments();//��ȡ���෺�Ͳ���
		clazz = (Class<T>) typeArguments[0];//���͵ĵ�һ������
	}
	// ͨ�õ���ɾ�Ĳ��� --version 2.0(���ǵ�����)
	public int update(Connection conn, String sql, Object... args) {
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

	/**
	 * ʵ����Բ�ͬ���ͨ�õ�һ����ѯ����(version--2.0:����������)
	 * 
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public T getInstance(Connection conn, String sql, Object... args) {
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
			e.printStackTrace();
		} finally {

			JDBCutil.closeResource(null, ps, rs);
		}
		return null;
	}
	//ʵ����Բ�ͬ���ͨ�õĲ�ѯ�������ɵļ���(version--2.0:����������)
	public List<T> getForList(Connection conn, String sql, Object... args) {
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
			// �������϶���
			ArrayList<T> list = new ArrayList<T>();
			while (rs.next()) {
				@SuppressWarnings("deprecation")
				T t = clazz.newInstance();
				// ��������һ�������е�ÿһ�У���t����ָ��һ�����Ը�ֵ
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
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			JDBCutil.closeResource(null, ps, rs);
		}
		return null;
	}
	//���ڲ�ѯһЩ����ֵ�ķ���
	public <E> E getValues(Connection conn, String sql, Object...args){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			for(int i = 0; i<args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			if(rs.next()) {
				return (E) rs.getObject(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			
			JDBCutil.closeResource(null, ps, rs);
		}
		return null;
	}
}
