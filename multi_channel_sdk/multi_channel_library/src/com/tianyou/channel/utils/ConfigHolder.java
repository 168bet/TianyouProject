package com.tianyou.channel.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.PayInfo;

/**
 * 获取渠道配置信息类
 * 
 * @author itstrong
 * 
 */
public class ConfigHolder {
	
	private static ChannelInfo mChannelInfo;
	private static PayInfo mPayInfo;
	
	public static ChannelInfo getChannelInfo(Context context) {
		if (mChannelInfo != null) {
			return mChannelInfo;
		}
		try {
			InputStream input = context.getAssets().open("channel_info.json");
			InputStreamReader reader = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(reader);
			String json = br.readLine();
            reader.close();
            JSONObject channelInfo = new JSONObject(json);
            JSONObject result = channelInfo.getJSONObject("result");
            if (result.getInt("code") == 200) {
            	JSONObject channelinfo = result.getJSONObject("channelinfo");
            	mChannelInfo = new ChannelInfo();
            	mChannelInfo.setChannelId(channelinfo.getString("channel_type"));
            	mChannelInfo.setAppId(channelinfo.getString("appid"));
            	mChannelInfo.setAppKey(channelinfo.getString("appkey"));
            	mChannelInfo.setMerchantId(channelinfo.getString("merchant_id"));
            	mChannelInfo.setGameId(channelinfo.getString("game_id"));
            	mChannelInfo.setCpId(channelinfo.getString("cp_id"));
            	mChannelInfo.setPayId(channelinfo.getString("pay_id"));
            	mChannelInfo.setBuoSecret(channelinfo.getString("buoy_secret"));
            	mChannelInfo.setPayRsaPrivate(channelinfo.getString("pay_private"));
            	mChannelInfo.setPayRsaPublic(channelinfo.getString("pay_public"));
            	mChannelInfo.setAppSecret(channelinfo.getString("appsecret"));
            	return mChannelInfo;
			} else {
				result.getString("msg");
			}
		} catch (IOException e) {
			LogUtils.d("配置文件不存在");
		} catch (JSONException e) {
			LogUtils.d("渠道信息解析异常");
		}
		return null;
	}
	
	@SuppressWarnings("resource")
	public static PayInfo getPayInfo(Context context, String payCode) {
		try {
			InputStream input = context.getAssets().open("pay_info.json");
			InputStreamReader reader = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(reader);
			String json = br.readLine();
            reader.close();
            JSONObject channelInfo = new JSONObject(json);
            JSONObject result = channelInfo.getJSONObject("result");
            if (result.getInt("code") == 200) {
            	JSONArray payinfos = result.getJSONArray("payinfo");
            	for (int i = 0; i < payinfos.length(); i++) {
            		JSONObject payinfo = payinfos.getJSONObject(i);
            		if (payCode.equals(payinfo.getString("id"))) {
            			mPayInfo = new PayInfo();
            			mPayInfo.setId(payinfo.getString("id"));
            			mPayInfo.setMoney(payinfo.getString("money"));
            			mPayInfo.setProductId(payinfo.getString("product_id"));
            			mPayInfo.setProductName(payinfo.getString("product_name"));
            			mPayInfo.setProductDesc(payinfo.getString("product_desc"));
                		return mPayInfo;
					}
				}
			} else {
				result.getString("msg");
			}
		} catch (IOException e) {
			LogUtils.d("配置文件不存在");
		} catch (JSONException e) {
			LogUtils.d("渠道信息解析异常");
		}
		return null;
	}
}
