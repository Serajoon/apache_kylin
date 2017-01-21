package com.serajoon.kylinrest.query;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.serajoon.kylinrest.KylinRestQuery;

public class KylinRestQueryTest {

	@Before
	public void setUp() throws Exception {
		KylinRestQuery.login("ADMIN", "KYLIN");
	}

	@Test
	public void query() {
		String sql = "select * from KYLIN_SALES";
		Map<String, String> map = new HashMap<String, String>();
		map.put("sql", sql);
		map.put("offset", "0");
		map.put("limit", "50");
		map.put("acceptPartial", "false");
		map.put("project", "learn_kylin");
		String body = JSON.toJSONString(map);
		String query = KylinRestQuery.query(body);
		System.out.println(query);
	}
	
	@Test
	public void listQueryableTables(){
		String listQueryableTables = KylinRestQuery.listQueryableTables("learn_kylin");
		System.out.println(listQueryableTables);
	}

}
