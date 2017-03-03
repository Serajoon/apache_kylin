package com.serajoon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MySqlCreateData {
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/fwwb?useUnicode=true&characterEncoding=utf-8";
	private static String user = "root";
	private static String password = "mysql";

	public static void main(String[] args) throws Exception {
		Connection conn = null;
		PreparedStatement statement = null;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
			int uuid = 0;
			while (true) {
				TimeUnit.SECONDS.sleep(20);
				String sql = "INSERT INTO kylin_test(UUID,cont_money,exec_money,country_name,city_name,provicen_name,partition_timestamp,partition_timestamp_utc,partition_timestamp_sqoop) VALUES("
						+ uuid + "," + uuid + "," + uuid + ",'XX区','XX市','XX省',?,?,?)";
				statement = conn.prepareStatement(sql);
				statement.setTimestamp(1, new Timestamp(new Date().getTime()));
				statement.setTimestamp(2, new Timestamp(getUTCDate().getTime()));
				statement.setTimestamp(3, new Timestamp(getSqoopDate().getTime()));
				statement.executeUpdate();
				uuid++;
			}

		} catch (ClassNotFoundException e) {
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			statement.close();
			conn.close();
		}
	}

	public static Date getUTCDate() {
		Calendar instanceUTC = Calendar.getInstance();
		instanceUTC.add(Calendar.HOUR, -8);
		Date timeUTC = instanceUTC.getTime();
		return timeUTC;
	}

	public static Date getSqoopDate() {
		Date d = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH");
		String format = sd.format(d);
		Date parse = null;
		try {
			parse = sd.parse(format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parse;
	}
}
