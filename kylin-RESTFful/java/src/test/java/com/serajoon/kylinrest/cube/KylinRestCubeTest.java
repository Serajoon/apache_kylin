package com.serajoon.kylinrest.cube;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.serajoon.kylinrest.KylinRestCube;

public class KylinRestCubeTest {
	@Before
	public void setUp() throws Exception {
		KylinRestCube.login("ADMIN", "KYLIN");
	}

	@Test
	public void listCubes() {
		String listCubes = KylinRestCube.listCubes(0, 50, "loadtest3", "loadtest");
		JSONArray jsonArray = JSON.parseArray(listCubes);
		for (Object o : jsonArray) {
			Object object = ((JSONObject) o).get("segments");
			System.out.println("segment数量：" + JSON.parseArray(object.toString()).size());
		}
		System.out.println(listCubes);
	}

	@Test
	public void getCubeDes() {
		String cubeDes = KylinRestCube.getCubeDes("loadtest1");
		System.out.println(cubeDes);
	}

	@Test
	public void getCubes() {
		String cubes = KylinRestCube.getCubes();
		System.out.println(cubes);
	}

	@Test
	public void getDataModel() {
		String dataModel = KylinRestCube.getDataModel("loadtest1_cube");
		System.out.println(dataModel);
	}

	@Test
	public void buildCube() {
		try {
			System.setProperty("user.timezone", "GMT+08");
			TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
			String startTime = "2017-01-01 08:00:00";
			String endTime = "2017-01-08 08:00:00";
			long startTimeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startTime).getTime();
			long endTimeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endTime).getTime();

			Map<String, String> map = new HashMap<String, String>();
			map.put("startTime", "" + startTimeStamp);
			map.put("endTime", "" + endTimeStamp);
			// buildType : BUILD  MERGE REFRESH
			map.put("buildType", "MERGE");
			KylinRestCube.buildCube("loadtest3", JSON.toJSONString(map));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void disableCube() {
		String disableCube = KylinRestCube.disableCube("fwwb_test1");
		System.out.println(disableCube);
	}

	@Test
	public void enableCube() {
		String enableCube = KylinRestCube.enableCube("increament_cube");
		System.out.println(enableCube);
	}

	@Test
	public void purgeCube() {
		String fwwb_test1 = KylinRestCube.purgeCube("increament_cube");
		System.out.println(fwwb_test1);
	}

}
