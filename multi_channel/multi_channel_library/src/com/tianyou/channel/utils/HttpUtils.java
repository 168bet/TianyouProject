package com.tianyou.channel.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;

public class HttpUtils {

	public static void get(final String path, final HttpCallback callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET"); // 必须大写
					conn.setConnectTimeout(5000);
					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream in = conn.getInputStream();
						String result = inputStreamToString(in);
						LogUtils.d("请求结果：" + result);
						callback.onSuccess(result);
					} else {
						callback.onFailed("code=" + code);
					}
				} catch (Exception e) {
					e.printStackTrace();
					callback.onFailed(e.getMessage());
				}
			}
		}).start();
	}

	public static void post(final Activity activity, final String path, final Map<String, String> data, final HttpCallback callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					LogUtils.d("请求URL：" + path);
		            URL url = new URL(path);
		            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		            conn.setRequestMethod("POST"); // 必须大写
		            conn.setConnectTimeout(5000);
		            StringBuilder form = new StringBuilder();
		            if (data != null) {
		            	LogUtils.d("请求参数：" + data.toString());
		            	for (Entry<String, String> entry : data.entrySet()) {
		            		form.append(entry.getKey() + "=" + entry.getValue() + "&");
		            	}
					}
		            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		            byte[] bytes = form.toString().getBytes();
		            conn.setRequestProperty("Content-Length", bytes.length + "");
		            conn.setDoOutput(true);
		            conn.getOutputStream().write(form.toString().getBytes());
					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream in = conn.getInputStream();
						final String result = inputStreamToString(in);
						LogUtils.d("请求结果：" + result);
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								callback.onSuccess(result);
							}
						});
					} else {
						callback.onFailed("code=" + code);
						LogUtils.d("请求结果：" + code);
					}
				} catch (Exception e) {
					e.printStackTrace();
					callback.onFailed(e.getMessage());
				}
			}
		}).start();
	}
	
	private static String inputStreamToString(InputStream inputStream) throws IOException {
		if (inputStream != null) {
			StringBuilder sb = new StringBuilder();
			try {
				String line;
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				inputStream.close();
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	public interface HttpCallback {

		void onSuccess(String data);

		void onFailed(String code);
	}
}
