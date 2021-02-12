package connection;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public class C3P0Test {
	//��ʽһ��
	@Test
	public void testGetConnection() throws Exception {
		//��ȡc3p0���ݿ������ӳ�
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver            
		cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
		cpds.setUser("root");                                  
		cpds.setPassword("123456"); 
		//ͨ��������صĲ��������ӳؽ��й���
		
		//���ó�ʼ�����ݿ����ӳص�������
		cpds.setInitialPoolSize(10);
		Connection conn = cpds.getConnection();
		System.out.println(conn);
		//����c3p0���ݿ����ӳ�
		//DataSources.destroy(cpds);
	}
	//��ʽ��:ʹ�������ļ�
	@Test
	public void testGetConnection1() throws SQLException{
		ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
		Connection conn = cpds.getConnection();
		System.out.println(conn);
	}
}
