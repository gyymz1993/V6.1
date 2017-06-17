package com.lsjr.zizisteward.ly.activity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.lsjr.zizisteward.http.HttpConfig;

public class HttpConn {
	public static String UserName = ""; // �û���

	public StringBuffer postData2(String portName, String content) {
		try {
			String path = HttpConfig.IMAGEHOST + portName;
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setReadTimeout(6000);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.connect();
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.write(content.getBytes());
			out.flush();
			out.close();

			InputStreamReader reader = new InputStreamReader(conn.getInputStream());
			char[] c = new char[1024];
			StringBuffer result = new StringBuffer();
			int length = -1;
			while ((length = reader.read(c)) != -1) {
				result.append(c, 0, length);
			}
			reader.close();
			return result;
		} catch (IOException e) {
			return new StringBuffer();
		}
	}
}
