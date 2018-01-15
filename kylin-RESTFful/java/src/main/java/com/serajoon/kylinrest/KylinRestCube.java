package com.serajoon.kylinrest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class KylinRestCube extends KylinHttpBasic {
	/**
	 * 
	 * @param offset
	 *            required int Offset used by pagination
	 * @param limit
	 *            required int Cubes per page.
	 * @param cubeName
	 *            optional string Keyword for cube names. To find cubes whose
	 *            name contains this keyword.
	 * @param projectName
	 *            optional string Project name.
	 * @return
	 */
	public static String listCubes(int offset, int limit, String cubeName, String projectName) {
		String method = "GET";
		String para = "/cubes?offset=" + offset + "&limit=" + limit + "&cubeName=" + cubeName + "&projectName=" + projectName;
		return excute(para, method, null);
	}

	/**
	 * 获得执行cube的描述
	 */
	public static String getCubeDes(String cubeName) {
		String method = "GET";
		String para = "/cube_desc/" + cubeName;
		return excute(para, method, null);

	}

	/**
	 * 获得指定的cube的信息
	 */
	public static String getCube(String cubeName) {
		String method = "GET";
		String para = "/cubes/" + cubeName;
		return excute(para, method, null);

	}

	/**
	 * 
	 * 获得所有的cube
	 */
	public static String getCubes() {
		String method = "GET";
		String para = "/cubes";
		return excute(para, method, null);
	}

	/**
	 * 获得执行model的信息
	 */
	public static String getDataModel(String modelName) {
		String method = "GET";
		String para = "/model/" + modelName;
		return excute(para, method, null);

	}

	/**
	 * startTime - required long Start timestamp of data to build, e.g.
	 * 1388563200000 for 2014-1-1 endTime - required long End timestamp of data
	 * to build buildType - required string Supported build type:
	 * ‘BUILD’,‘MERGE’, ‘REFRESH’
	 */
	public static String buildCube(String cubeName, String body) {
		String method = "PUT";
		String para = "/cubes/" + cubeName + "/build";
		return excute(para, method, body);
	}

	/**
	 * 清空cube,清空cube之前必须将cube disable
	 */
	public static String purgeCube(String cubeName) {
		String disableCubeJson = disableCube(cubeName);
		JSONObject json = JSON.parseObject(disableCubeJson);
		String status = (String) json.get("status");
		if ("DISABLED".equals(status)) {
			String method = "PUT";
			String para = "/cubes/" + cubeName + "/purge";
			return excute(para, method, null);
		} else {
			return null;
		}

	}

	/**
	 * 禁用cube
	 */
	public static String disableCube(String cubeName) {

		String method = "PUT";
		String para = "/cubes/" + cubeName + "/disable";
		return excute(para, method, null);

	}

	/**
	 * 使cube可用
	 */
	public static String enableCube(String cubeName) {
		String method = "PUT";
		String para = "/cubes/" + cubeName + "/enable";
		return excute(para, method, null);

	}

}
