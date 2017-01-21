package com.serajoon.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.serajoon.po.KylinSalesPo;

/**
 * Servlet implementation class EcharServlet
 */
public class EcharServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger("name");
	private static String driver = "org.apache.kylin.jdbc.Driver";
	private static String url = "jdbc:kylin://210.25.24.68:7070/learn_kylin";
	private static String username = "ADMIN";// linux的用户名
	private static String password = "KYLIN";// linux密码
	private static String sql = "select part_dt,count(distinct seller_id) as sellers from kylin_sales group by part_dt order by part_dt limit 10";

	/**
	 * Default constructor.
	 */
	public EcharServlet() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String json = getResult();
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().write(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public String getResult() {
		List<KylinSalesPo> list = new ArrayList<KylinSalesPo>();
		try {
			Properties info = new Properties();
			info.put("user", username);
			info.put("password", password);
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, info);
			PreparedStatement state = conn.prepareStatement(sql);
			ResultSet resultSet = state.executeQuery();


			while (resultSet.next()) {
				String col1 = resultSet.getString(1);
				int col2 = resultSet.getInt(2);
				list.add(new KylinSalesPo(col1,col2));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return JSON.toJSONString(list);
	}
	

}
