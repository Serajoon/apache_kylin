package com.serajoon.kylinrest;

public class KylinRestMetaData extends KylinHttpBasic {
	/**
	 * ???
	 * @param tableName
	 * @return
	 */
	public static String getHiveTable(String tableName) {
		String method = "GET";
		String para = "/tables/" + tableName;
		return excute(para, method, null);
	}

	/**
	 * ???
	 * @param tableName
	 *            table name to find.
	 * @return
	 */
	public static String getHiveTableInfo(String tableName) {
		String method = "GET";
		String para = "/tables/" + tableName + "/exd-map";
		return excute(para, method, null);
	}

	/**
	 * 展示与一个项目下的所有的表
	 * @param projectName
	 * will list all tables in the project.
	 * @param extOptional
	 * boolean set true to get extend info of table.
	 * @return
	 */
	public static String getHiveTables(String projectName, boolean extOptional) {
		String method = "GET";
		String para = "/tables?project=" + projectName + "&ext=" + extOptional;
		return excute(para, method, null);
	}

	/**
	 * @param tables 加载需要的hive表，多个hive表之间用逗号分开 数据库.表。
	 * @param project the project which the tables will be loaded into.
	 * @return
	 */
	public static String loadHiveTables(String tables, String project) {
		String method = "POST";
		String para = "/tables/" + tables + "/" + project;
		return excute(para, method, null);
	}
}
