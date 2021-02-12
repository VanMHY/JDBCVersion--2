package dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import bean.Customer;

/*
 * ����ӿ����ڹ滮customers��ĳ��ò���
 */
public interface CustomerDAO {
	/**
	 * ��cust������ӵ����ݿ���
	 * @param conn
	 * @param cust
	 */
	void insert(Connection conn, Customer cust);
	/**
	 * ���ָ����id��ɾ��ָ����һ������
	 * @param conn
	 * @param id
	 */
	void deleteById(Connection conn, int id);
	/**
	 * ����ڴ��е�cust����ȥ�޸ı�������
	 * @param conn
	 * @param id
	 */
	void update(Connection conn, Customer cust);
	/**
	 * ���ָ����id�����ҵõ���Ӧ��Customer����
	 * @param conn
	 * @param id
	 */
	Customer getCustomerById(Connection conn, int id);
	/**
	 * ��ѯ�������м�¼���ɵļ���
	 * @param conn
	 * @return
	 */
	List<Customer> getAll(Connection conn);
	/**
	 * �������ݱ��е�������Ŀ
	 * @param conn
	 * @return
	 */
	Long getCount(Connection conn);
	/**
	 * �������ݱ�����������
	 * @param conn
	 * @return
	 */
	Date getMaxBirth(Connection conn);
}
	