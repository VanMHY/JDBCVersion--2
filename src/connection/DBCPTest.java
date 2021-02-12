package connection;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;

public class DBCPTest {
	/**
	 * 测试DBCP数据库连接池技术
	 * @throws SQLException 
	 */
	@Test
	public void testGetConnection() throws SQLException {
		//创建了DBCP数据库连接池
		@SuppressWarnings("resource")
		BasicDataSource source = new BasicDataSource();
		//设置基本信息
		source.setDriverClassName("com.mysql.jdbc.Driver");
		source.setUrl("jdbc:mysql://localhost:3306/test");
		source.setUsername("root");
		source.setPassword("123456");
		//设置其他涉及数据库连接池管理的相关属性
		source.setInitialSize(10);	 
		Connection conn = source.getConnection();
		System.out.println(conn);
	}
	@Test
	public void testGetConnection1() throws Exception {
		Properties pros = new Properties();
		//方式一：
		//InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
		//方式二：
		FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
		pros.load(is);
		BasicDataSource source = BasicDataSourceFactory.createDataSource(pros);
		Connection conn = source.getConnection();
		System.out.println(conn);
	}
}
