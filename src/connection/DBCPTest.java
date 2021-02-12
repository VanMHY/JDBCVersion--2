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
	 * ����DBCP���ݿ����ӳؼ���
	 * @throws SQLException 
	 */
	@Test
	public void testGetConnection() throws SQLException {
		//������DBCP���ݿ����ӳ�
		@SuppressWarnings("resource")
		BasicDataSource source = new BasicDataSource();
		//���û�����Ϣ
		source.setDriverClassName("com.mysql.jdbc.Driver");
		source.setUrl("jdbc:mysql://localhost:3306/test");
		source.setUsername("root");
		source.setPassword("123456");
		//���������漰���ݿ����ӳع�����������
		source.setInitialSize(10);	 
		Connection conn = source.getConnection();
		System.out.println(conn);
	}
	@Test
	public void testGetConnection1() throws Exception {
		Properties pros = new Properties();
		//��ʽһ��
		//InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
		//��ʽ����
		FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
		pros.load(is);
		BasicDataSource source = BasicDataSourceFactory.createDataSource(pros);
		Connection conn = source.getConnection();
		System.out.println(conn);
	}
}
