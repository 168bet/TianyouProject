package com.tianyou.channel.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.PayInfo;

import android.content.Context;

/**
 * 获取渠道配置信息类
 * @author itstrong
 * 
 */
public class ConfigHolder {
	
	private static ChannelInfo mChannelInfo;
	private static ArrayList<PayInfo> mPayInfoList;
	
	public static ChannelInfo getChannelInfo(Context context) {
		if (mChannelInfo != null) return mChannelInfo;
		String channelInfo = readFileData(context, "channel_info.json");
		try {
			JSONObject info = new JSONObject(channelInfo);
			mChannelInfo = new ChannelInfo();
			mChannelInfo.setChannelId(info.getString("channel_id"));
			mChannelInfo.setChannelName(info.getString("channel_name"));
			mChannelInfo.setAppId(info.getString("app_id"));
			mChannelInfo.setAppToken(info.getString("app_token"));
			mChannelInfo.setGameName(info.getString("game_name"));
			mChannelInfo.setGameId(info.getString("game_id"));
			mChannelInfo.setGameToken(info.getString("game_token"));
			mChannelInfo.setAppKey(info.getString("app_key"));
		} catch (JSONException e1) {
			ToastUtils.show(context, "渠道信息解析异常");
		}
		return mChannelInfo;
	}
	
	public static PayInfo getPayInfo(Context context, String payCode) {
		if (mPayInfoList == null) {
			String json = readFileData(context, "pay_info.json");
			try {
				JSONObject jsonInfo = new JSONObject(json);
				mPayInfoList = new ArrayList<PayInfo>();
				JSONArray payArray = jsonInfo.getJSONArray("payinfo");
				for (int i = 0; i < payArray.length(); i++) {
					PayInfo payInfo = new PayInfo();
					JSONObject info = payArray.getJSONObject(i);
					payInfo.setId(info.getString("id"));
					payInfo.setMoney(info.getString("money"));
					payInfo.setProductId(info.getString("product_id"));
					payInfo.setProductName(info.getString("product_name"));
					payInfo.setProductDesc(info.getString("product_desc"));
					mPayInfoList.add(payInfo);
				}
			} catch (JSONException e1) {
				ToastUtils.show(context, "渠道信息解析异常");
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
            while((newLine = br.readLine()) != null) {
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
