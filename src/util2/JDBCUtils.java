package util2;

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
import org.apache.commons.dbutils.DbUtils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCUtils {
	/**
	 * 获取连接
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
		// 加载驱动
		Class.forName(driverClass);
		// 获取连接
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}

	/**
	 * 使用C3P0的数据库连接池技术
	 * 
	 * @return
	 * @throws SQLException
	 */
	// 数据库连接池只需要一个
	private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");

	public static Connection getConnection1() throws SQLException {
		Connection conn = cpds.getConnection();
		return conn;
	}

	/**
	 * 使用DBCP数据库连接池技术获取数据库的连接
	 * 
	 * @return
	 * @throws Exception
	 */
	private static BasicDataSource source;
	static {
		try {
			Properties pros = new Properties();
			FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
			pros.load(is);
			// 创建一个DSCP数据库连接池
			source = BasicDataSourceFactory.createDataSource(pros);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection2() throws Exception {

		Connection conn = source.getConnection();
		return conn;
	}
	/**
	 * 使用Druid数据库连接池技术
	 */
	private static  DataSource source1;
	static {
		try {
			Properties pros = new Properties();
			ClassLoader.getSystemClassLoader();
			InputStream is = ClassLoader.getSystemResourceAsStream("druid.properties");
			pros.load(is);
			source1 =  DruidDataSourceFactory.createDataSource(pros);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection3() throws Exception {
		
		Connection conn = source1.getConnection();
		return conn;
	}

	/**
	 * 关闭资源
	 * 
	 * @param conn
	 * @param ps
	 */
	public static void closeResource(Connection conn, Statement ps) {
		// 关闭资源
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

	public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
		// 关闭资源
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
	/**
	 * 使用dbutil.jar中的DbUtils工具类实现资源关闭
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public static void closeResource1(Connection conn, Statement ps, ResultSet rs) {
		// 关闭资源
//		try {
//			DbUtils.close(conn);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			DbUtils.close(ps);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			DbUtils.close(rs);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		DbUtils.closeQuietly(conn);
		DbUtils.closeQuietly(ps);
		DbUtils.closeQuietly(rs);
	}
}
