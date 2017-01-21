package com.serajoon.kylinrest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import sun.misc.BASE64Encoder;

/**
 * 
 *
 */
public class KylinHttpBasic {

	private static String encoding;
	private static final String RESTURL = "http://210.25.24.68:7070/kylin/api";

	/*
	 * 登录
	 */
	public static String login(String user, String passwd) {
		String method = "POST";
		String para = "/user/authentication";
		byte[] key = (user + ":" + passwd).getBytes();
		encoding = new BASE64Encoder().encode(key);
		return excute(para, method, null);
	}

	/*
	 * 执行
	 */
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