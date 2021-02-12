package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * �������ݿ�Ĺ�����
 * 
 * @author yf
 *
 */
public class JDBCutil {
	/**
	 * ��ȡ����
	 * 
	 * @return
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
		Properties pros = new Properties();
		pros.load(is);
		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");
		// ��������
		Class.forName(driverClass);
		// ��ȡ����
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}

	/**
	 * ʹ��C3P0�����ݿ����ӳؼ���
	 * 
	 * @return
	 * @throws SQLException
	 */
	// ���ݿ����ӳ�ֻ��Ҫһ��
	private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");

	public static Connection getConnection1() throws SQLException {
			Connection conn = cpds.getConnection();
			return conn;
	}
	/**
	 * ʹ��DBCP���ݿ����ӳؼ�����ȡ���ݿ������
	 * @return
	 * @throws Exception
	 */
	private static BasicDataSource source;
	static {
		try {
			Properties pros = new Properties();
			FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
			pros.load(is);
			//����һ��DSCP���ݿ����ӳ�
			source = BasicDataSourceFactory.createDataSource(pros);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection Connection2() throws Exception {
		
		Connection conn = source.getConnection();
		return conn;
	}
	/**
	 * �ر���Դ
	 * 
	 * @param conn
	 * @param ps
	 */
	public static void closeResource(Connection conn, Statement ps) {
		// �ر���Դ
		try {
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ʹ��Druid���ݿ����ӳؼ���
	 */
	private static  BasicDataSource source1;
	static {
		try {
			Properties pros = new Properties();
			InputStream is = ClassLoader.getSystemClassLoader().getSystemResourceAsStream("druid.properties");
			pros.load(is);
			source1 = (BasicDataSource) DruidDataSourceFactory.createDataSource(pros);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection3() throws Exception {
		
		Connection conn = source1.getConnection();
		return conn;
	}

	public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
		// �ر���Դ
		try {
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
