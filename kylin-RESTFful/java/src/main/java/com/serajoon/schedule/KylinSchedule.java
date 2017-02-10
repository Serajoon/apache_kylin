package com.serajoon.schedule;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;

import sun.misc.BASE64Encoder;

public class KylinSchedule {

	private static String encoding;
	private static final String RESTURL = "http://210.25.24.68:7070/kylin/api";

	public static void main(String[] args) {
		login("ADMIN", "KYLIN");// 登录

		Runnable runnable = new Runnable() {
			public void run() {
				Date d = new Date();
				long endTimeStamp = d.getTime();
				Map<String, String> map = new HashMap<String, String>();
				map.put("endTime", "" + endTimeStamp);
				// buildType : BUILD MERGE REFRESH
				map.put("buildType", "BUILD");
				buildCube("prj0207_cube", JSON.toJSONString(map));
				System.out.println("开始创建cube 创建时间：" + d.toString());
			}
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
		service.scheduleAtFixedRate(runnable, 0, 180, TimeUnit.SECONDS);
	}

	public static String login(String user, String passwd) {
		String method = "POST";
		String para = "/user/authentication";
		byte[] key = (user + ":" + passwd).getBytes();
		encoding = new BASE64Encoder().encode(key);
		return excute(para, method, null);
	}

	public static String buildCube(String cubeName, String body) {
		String method = "PUT";
		String para = "/cubes/" + cubeName + "/rebuild";
		return excute(para, method, body);
	}

	protected static String excute(String para, String method, String body) {

		StringBuilder out = new StringBuilder();
		try {
			URL url = new URL(RESTURL + para);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			connection.setRequestProperty("Content-Type", "application/json");
			if (body != null) {
				byte[] outputInBytes = body.getBytes("UTF-8");
				OutputStream os = connection.getOutputStream();
				os.write(outputInBytes);
				os.close();
			}
			InputStream content = (InputStream) connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(content));
			String line;
			while ((line = in.readLine()) != null) {
				out.append(line);
			}
			in.close();
			connection.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return out.toString();
	}
}
