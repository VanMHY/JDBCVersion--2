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
 * 封装了针对于数据表的通用操作
 */
public abstract class BaseDAO<T> {
	private Class<T> clazz = null;
//	public BaseDAO() {
//		
//	}
	{
		//获取当前BaseDAO的子类继承父类中的泛型
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) genericSuperclass;
		Type[] typeArguments = paramType.getActualTypeArguments();//获取父类泛型参数
		clazz = (Class<T>) typeArguments[0];//泛型的第一个参数
	}
	// 通用的增删改操作 --version 2.0(考虑到事务)
	public int update(Connection conn, String sql, Object... args) {
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

	/**
	 * 实现针对不同表的通用的一条查询操作(version--2.0:考虑上事务)
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
			e.printStackTrace();
		} finally {

			JDBCutil.closeResource(null, ps, rs);
		}
		return null;
	}
	//实现针对不同表的通用的查询操作构成的集合(version--2.0:考虑上事务)
	public List<T> getForList(Connection conn, String sql, Object... args) {
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
			// 创建集合对象
			ArrayList<T> list = new ArrayList<T>();
			while (rs.next()) {
				@SuppressWarnings("deprecation")
				T t = clazz.newInstance();
				// 处理结果集一行数据中的每一列：给t对象指定一个属性赋值
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
	//用于查询一些特需值的方法
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
