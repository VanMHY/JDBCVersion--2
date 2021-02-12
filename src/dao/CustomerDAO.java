package dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import bean.Customer;

/*
 * 这个接口用于规划customers表的常用操作
 */
public interface CustomerDAO {
	/**
	 * 将cust对象添加到数据库中
	 * @param conn
	 * @param cust
	 */
	void insert(Connection conn, Customer cust);
	/**
	 * 针对指定的id，删除指定的一条数据
	 * @param conn
	 * @param id
	 */
	void deleteById(Connection conn, int id);
	/**
	 * 针对内存中的cust对象，去修改表中数据
	 * @param conn
	 * @param id
	 */
	void update(Connection conn, Customer cust);
	/**
	 * 针对指定的id，查找得到对应的Customer对象
	 * @param conn
	 * @param id
	 */
	Customer getCustomerById(Connection conn, int id);
	/**
	 * 查询表中所有记录构成的集合
	 * @param conn
	 * @return
	 */
	List<Customer> getAll(Connection conn);
	/**
	 * 返回数据表中的数据条目
	 * @param conn
	 * @return
	 */
	Long getCount(Connection conn);
	/**
	 * 返回数据表中最大的生日
	 * @param conn
	 * @return
	 */
	Date getMaxBirth(Connection conn);
}
	