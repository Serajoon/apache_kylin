package com.serajoon.mapper.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KylinJdbc {
	private static Logger logger = LoggerFactory.getLogger("name");
	private static String driver = "org.apache.kylin.jdbc.Driver";
	private static String url = "jdbc:kylin://210.25.24.68:7070/learn_kylin";
	private static String username = "ADMIN";// linux的用户名
	private static String password = "KYLIN";// linux密码
	private static String sql = "select part_dt, sum(price) as total_selled,count(distinct seller_id) as sellers from kylin_sales group by part_dt order by part_dt limit 5";

	public static void main(String[] args) {
		try {
			// 配置登录Kylin的用户名和密码
			Properties info = new Properties();
			info.put("user", username);
			info.put("password", password);
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, info);
			PreparedStatement state = conn.prepareStatement(sql);
			ResultSet resultSet = state.executeQuery();

			System.out.println("part_dt\t" + "\t" + "total_selled" + "\t" + "sellers");

			while (resultSet.next()) {
				String col1 = resultSet.getString(1);
				String col2 = resultSet.getString(2);
				String col3 = resultSet.getString(3);
				System.out.println(col1 + "\t" + col2 + "\t" + col3);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
