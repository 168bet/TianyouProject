package com.tianyou.channel.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.PayInfo;

import android.app.Activity;
import android.content.Context;

/**
 * 获取渠道配置信息类
 * 
 * @author itstrong
 * 
 */
public class ConfigHolder {

	private static ChannelInfo mChannelInfo;
	private static ArrayList<PayInfo> mPayInfoList;
	private static Map<String, String> mChannelMap;

	public static Map<String, String> getChannelMap(Context context) {
		if (mChannelMap != null)
			return mChannelMap;
		mChannelMap = new HashMap<String, String>();
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		InputStream inputStream = null;
		factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
			inputStream = context.getResources().getAssets().open("channel_info.xml");
			document = builder.parse(inputStream);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
		}
		Element root = document.getDocumentElement();
		NodeList nodes = root.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node item = nodes.item(i);
			String textContent = item.getTextContent().trim();
			if (!textContent.isEmpty()) {
				mChannelMap.put(item.getNodeName(), textContent);
			}
		}
		LogUtils.d("渠道信息：" + mChannelMap);
		return mChannelMap;
	}
	
	public static ChannelInfo getChannelInfo(Context context) {
		if (mChannelInfo != null)
			return mChannelInfo;
		String channelInfo = readFileData(context, "channel_info.json");
		JSONObject info;
		try {
			info = new JSONObject(channelInfo);
		} catch (JSONException e) {
			ToastUtils.show(context, "渠道信息解析异常");
			return null;
		}
		mChannelInfo = new ChannelInfo();
		try {
			mChannelInfo.setChannelId(info.getString("channel_id"));
		} catch (JSONException e) {
			e.printStackTrace();
			LogUtils.d("channel_id为空");
		}
		try {
			mChannelInfo.setChannelName(info.getString("channel_name"));
		} catch (JSONException e) {
			LogUtils.d("channel_name为空");
		}
		try {
			mChannelInfo.setAppId(info.getString("app_id"));
		} catch (JSONException e) {
			LogUtils.d("app_id为空");
		}
		try {
			mChannelInfo.setAppToken(info.getString("app_token"));
		} catch (JSONException e) {
			LogUtils.d("app_token为空");
		}
		try {
			mChannelInfo.setGameName(info.getString("game_name"));
		} catch (JSONException e) {
			LogUtils.d("game_name为空");
		}
		try {
			mChannelInfo.setGameId(info.getString("game_id"));
		} catch (JSONException e) {
			LogUtils.d("game_id为空");
		}
		try {
			mChannelInfo.setGameToken(info.getString("game_token"));
		} catch (JSONException e) {
			LogUtils.d("game_token为空");
		}
		try {
			mChannelInfo.setAppKey(info.getString("app_key"));
		} catch (JSONException e) {
			LogUtils.d("app_key为空");
		}
		try {
			mChannelInfo.setPrivateKey(info.getString("private_key"));
		} catch (JSONException e1) {
			LogUtils.d("private_key为空");
		}
		try {
			mChannelInfo.setPublicKey(info.getString("public_key"));
		} catch (JSONException e1) {
			LogUtils.d("public_key为空");
		}
		try {
			mChannelInfo.setClientId(info.getString("client_id"));
		} catch (JSONException e) {
			LogUtils.d("client_id为空");
		}
		try {
			mChannelInfo.setClientSecret(info.getString("client_secret"));
		} catch (JSONException e) {
			LogUtils.d("client_secret为空");
		}
		return mChannelInfo;
	}

	public static PayInfo getPayInfo(final Activity activity, String payCode) {
		LogUtils.d("getPayInfo0");
		if (mPayInfoList == null) {
			LogUtils.d("getPayInfo1");
			String json = readFileData(activity, "pay_info.json");
			LogUtils.d("getPayInfo2");
			try {
				LogUtils.d("getPayInfo3");
				JSONObject jsonInfo = new JSONObject(json);
				LogUtils.d("getPayInfo4");
				mPayInfoList = new ArrayList<PayInfo>();
				LogUtils.d("getPayInfo5");
				JSONArray payArray = jsonInfo.getJSONArray("payinfo");
				LogUtils.d("getPayInfo6");
				for (int i = 0; i < payArray.length(); i++) {
					LogUtils.d("getPayInfo7");
					PayInfo payInfo = new PayInfo();
					JSONObject info = payArray.getJSONObject(i);
					payInfo.setId(info.getString("id"));
					payInfo.setMoney(info.getString("money"));
					payInfo.setProductId(info.getString("product_id"));
					payInfo.setProductName(info.getString("product_name"));
					payInfo.setProductDesc(info.getString("product_desc"));
					mPayInfoList.add(payInfo);
					LogUtils.d("getPayInfo8");
				}
				LogUtils.d("支付信息：" + mPayInfoList);
			} catch (JSONException e1) {
				ToastUtils.show(activity, "支付信息解析异常");
			}
		}
		for (PayInfo payInfo : mPayInfoList) {
			if (payCode.equals(payInfo.getId())) {
				return payInfo;
			}
		}
		return null;
	}

	private static String readFileData(Context context, String fileName) {
		try {
			InputStream input = context.getAssets().open(fileName);
			InputStreamReader reader = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(reader);
			String results = "";
			String newLine = "";
			while ((newLine = br.readLine()) != null) {
				results += newLine;
			}
			reader.close();
			return results;
		} catch (IOException e) {
			ToastUtils.show(context, "配置文件" + fileName + "不存在");
		}
		return null;
	}
}
