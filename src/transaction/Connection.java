package transaction;

import org.junit.Test;

import util.JDBCutil;

public class Connection {
	@Test
	public void testGetConnection() throws Exception {
		java.sql.Connection conn = JDBCutil.getConnection();
		System.out.println(conn);
	}
}
