package com.tianyou.sdk.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.tianyou.sdk.activity.ExitActivity;
import com.tianyou.sdk.activity.FloatMenu;
import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.activity.PayActivity;
import com.tianyou.sdk.base.FloatControl;
import com.tianyou.sdk.bean.PayWayControl;
import com.tianyou.sdk.bean.ServerInfo;
import com.tianyou.sdk.bean.PayWayControl.ResultBean;
import com.tianyou.sdk.bean.ServerInfo.ResultBean.CustominfoBean;
import com.tianyou.sdk.fragment.pay.NetworkFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.interfaces.TianyouCallback.LoginCallback;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpCallback;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ToastUtils;
import com.tianyou.sdk.utils.XMLParser;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.EScenarioType;
import com.umeng.analytics.MobclickAgent.UMAnalyticsConfig;

public class Tianyouxi {

	public static Activity mActivity;
	public static TianyouCallback mExitGameCallback;
	public static LoginCallback mLoginCallback;
	public static TianyouCallback mPayCallback;
	
	// 初始化接口
	@SuppressWarnings("unchecked")
	public static void init(Context context, String gameId, String gameToken, boolean isLandscape) {
		LogUtils.d("appliction初始化");
		ConfigHolder.isLandscape = isLandscape;
		InputStream is;
		try {
			is = context.getAssets().open("tianyou_config.xml");
		} catch (IOException e) {
			e.printStackTrace();
			is = null;
		}
		if (is == null) LogUtils.e("没有找到配置文件");
		Map<String, Object> mm = XMLParser.paraserXML(is);
		ConfigHolder.CHANNEL_ID = (String) ((Map<String, Object>) mm.get("infos")).get("channel_id");
		ConfigHolder.IS_OVERSEAS = "1".equals((String) ((Map<String, Object>) mm.get("infos")).get("is_overseas"));
		ConfigHolder.IS_OPEN_LOG = "1".equals((String) ((Map<String, Object>) mm.get("infos")).get("log_switch"));
		ConfigHolder.GAME_ID = gameId;
		ConfigHolder.GAME_TOKEN = gameToken;

		// 友盟统计
		MobclickAgent.setScenarioType(context, EScenarioType.E_UM_NORMAL);// 设置为普通统计场景类型
		String umAppKey = AppUtils.getMetaDataValue(context, "UMENG_APPKEY");
		String umChannel = AppUtils.getMetaDataValue(context, "UMENG_CHANNEL");
		UMAnalyticsConfig umAnalyticsConfig = new UMAnalyticsConfig(context, umAppKey, umChannel);
		MobclickAgent.startWithConfigure(umAnalyticsConfig);

		// 友盟推送
//		PushAgent mPushAgent = PushAgent.getInstance(context);
//		mPushAgent.setDebugMode(false);
//		mPushAgent.register(new IUmengRegisterCallback() {
//			@Override
//			public void onSuccess(String deviceToken) { }
//
//			@Override
//			public void onFailure(String s, String s1) { }
//		});
		
		if (ConfigHolder.IS_OVERSEAS) {
			//facebook初始化
			FacebookSdk.sdkInitialize(context);
			AppEventsLogger.activateApp((Application)context);
		}

	}

	private static void getPayWay(){
		Map<String,String> map = new HashMap<String, String>();
    	map.put("appID", ConfigHolder.GAME_ID);
		map.put("usertoken", ConfigHolder.GAME_TOKEN);
		HttpUtils.post(mActivity, URLHolder.URL_PAY_WAY_CONTROL, map, new HttpCallback() {
			@Override
			public void onSuccess(String response) {
				SPHandler.putString(mActivity, SPHandler.SP_PAY_WAY, response);
			}
			
			@Override
			public void onFailed() {
//				mActivity.switchFragment(new NetworkFragment(), "NoNetworkFragment");
			}
		});
	}
	
	// 创建悬浮球接口
	public static void createFloatMenu(final Activity activity) {
		final Map<String, String> map = new HashMap<String, String>();
		map.put("appID", ConfigHolder.GAME_ID);
		map.put("usertoken", ConfigHolder.GAME_TOKEN);
		map.put("language", AppUtils.getLanguageSort(activity));
		HttpUtils.post(activity, URLHolder.URL_FLOAT_CONTROL, map, new HttpUtils.HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				FloatControl control = new Gson().fromJson(response, FloatControl.class);
				if (control.getResult().getCode() == 200) {
					if (control.getResult().getStatus() != 0) {
						new FloatMenu(activity).createLogoPopupWindow();
//						new Handler().postDelayed(new Runnable() {
//							@Override
//							public void run() {
//								
//							}
//						}, 1000);
					}
					SPHandler.putString(activity, SPHandler.SP_FLOAT_CONTROL, response);
				} else {
					ToastUtils.show(mActivity, control.getResult().getMsg());
				}
			}
		});
		getServiceInfo(activity);
	}
	
	private static void getServiceInfo(final Activity activity) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("appID", ConfigHolder.GAME_ID);
		map.put("usertoken", ConfigHolder.GAME_TOKEN);
		HttpUtils.post(activity, URLHolder.URL_SERVER_IMG, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				ServerInfo serverInfo = new Gson().fromJson(response, ServerInfo.class);
    			if (serverInfo.getResult().getCode() == 200) {
    				CustominfoBean customInfo = serverInfo.getResult().getCustominfo();
    				SPHandler.putString(activity, SPHandler.SP_URL_PHONE, customInfo.getCall().getImgurl());
    				SPHandler.putString(activity, SPHandler.SP_URL_QQ, customInfo.getQq().getImgurl());
    				SPHandler.putString(activity, SPHandler.SP_URL_WX, customInfo.getWx().getImgurl());
    				SPHandler.putString(activity, SPHandler.SP_TEXT_PHONE, customInfo.getCall().getValue());
    				SPHandler.putString(activity, SPHandler.SP_TEXT_QQ, customInfo.getQq().getValue());
    				SPHandler.putString(activity, SPHandler.SP_TEXT_WX, customInfo.getWx().getValue());
    			}
			}
		});
	}

	// 登陆接口
	public static void login(Activity activity, String gameName, LoginCallback callback) {
		mActivity = activity;
		mLoginCallback = callback;
		ConfigHolder.GAME_NAME = gameName;
		activity.startActivity(new Intent(activity, LoginActivity.class));
	}

	// 支付接口
	public static void pay(Context context, String payInfo, TianyouCallback callback) {
		mPayCallback = callback;
		if (ConfigHolder.IS_LOGIN) {
			Intent intent = new Intent(context, PayActivity.class);
			intent.putExtra("payInfo", payInfo);
			context.startActivity(intent);
		} else {
			ToastUtils.show(context, "请先登录！");
		}
	}
	
	// 支付接口
	public static void pay(Context context, String payInfo, int money, String productDesc, TianyouCallback callback) {
		mPayCallback = callback;
		if (ConfigHolder.IS_LOGIN) {
			Intent intent = new Intent(context, PayActivity.class);
			intent.putExtra("payInfo", payInfo);
			intent.putExtra("money", money);
			intent.putExtra("productDesc", productDesc);
			context.startActivity(intent);
		} else {
			ToastUtils.show(context, "请先登录！");
		}
	}

	// 进入游戏
	public static void enterGame(Context context, String jsonData) {
		if (!ConfigHolder.IS_LOGIN) {
			ToastUtils.show(context, "请先登录！");
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("appID", ConfigHolder.GAME_ID);
		map.put("userID", ConfigHolder.USER_ID);
		map.put("channel", ConfigHolder.CHANNEL_ID);
		try {
			JSONObject roleInfo;
			roleInfo = new JSONObject(jsonData);
			map.put("roleID", roleInfo.getString("roleId"));
			map.put("roleLevel", roleInfo.getString("roleLevel"));
			map.put("serverID", roleInfo.getString("serverId"));
			map.put("serverName", roleInfo.getString("serverName"));
			map.put("vipLevel", roleInfo.getString("vipLevel"));
		} catch (JSONException e) {
			e.printStackTrace();
			ToastUtils.show(mActivity, "角色信息解析错误");
		}
		HttpUtils.post(mActivity, URLHolder.URL_ENTER_GAME, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) { }
		});
	}

	// 创建角色
	public static void createRole(Context context, String jsonData) {
		if (!ConfigHolder.IS_LOGIN) {
			ToastUtils.show(context, "请先登录！");
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("appID", ConfigHolder.GAME_ID);
		map.put("userID", ConfigHolder.USER_ID);
		map.put("channel", ConfigHolder.CHANNEL_ID);
		try {
			JSONObject roleInfo;
			roleInfo = new JSONObject(jsonData);
			map.put("roleID", roleInfo.getString("roleId"));
			map.put("roleName", roleInfo.getString("roleName"));
			map.put("serverID", roleInfo.getString("serverId"));
			map.put("serverName", roleInfo.getString("serverName"));
			map.put("profession", roleInfo.getString("profession"));
		} catch (JSONException e) {
			e.printStackTrace();
			ToastUtils.show(mActivity, "角色信息解析错误");
		}
		HttpUtils.post(mActivity, URLHolder.URL_CREATE_ROLE, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) { }
		});
	}

	// 退出游戏接口
	public static void exitGame(Context context, TianyouCallback callback) {
		mExitGameCallback = callback;
		context.startActivity(new Intent(context, ExitActivity.class));
	}
}
