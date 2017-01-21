package com.serajoon.kylinrest;

import com.serajoon.kylinrest.KylinHttpBasic;

public class KylinRestQuery extends KylinHttpBasic {
	// 列出项目下所有的表
	public static String listQueryableTables(String projectName) {
		String method = "GET";
		String para = "/tables_and_columns?project=" + projectName;
		return excute(para, method, null);
	}
	//传入sql 查询
	public static String query(String body) {
		String method = "POST";
		String para = "/query";

		return excute(para, method, body);
	}
}
