package com.tianyou.sdk.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.tianyou.sdk.activity.ExitActivity;
import com.tianyou.sdk.activity.FloatMenu;
import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.activity.PayActivity;
import com.tianyou.sdk.base.FloatControl;
import com.tianyou.sdk.bean.RoleInfo;
import com.tianyou.sdk.bean.ServerInfo;
import com.tianyou.sdk.bean.ServerInfo.ResultBean.CustominfoBean;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.EScenarioType;
import com.umeng.analytics.MobclickAgent.UMAnalyticsConfig;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class TianyouSdk {

	private static TianyouSdk mTianyouSdk;
	public TianyouCallback mTianyouCallback;
	public Activity mActivity;
	
	private TianyouSdk() { }
	
	public static TianyouSdk getInstance() {
		if (mTianyouSdk == null) {
			mTianyouSdk = new TianyouSdk();
		}
		return mTianyouSdk;
	}
	
	// Application初始化
	public void applicationInit(Context context, String gameId, String gameToken, String gameName, boolean isLandscape) {
		LogUtils.d("appliction初始化");
		ConfigHolder.gameId = gameId;
		ConfigHolder.gameName = gameName;
		ConfigHolder.gameToken = gameToken;
		ConfigHolder.isLandscape = isLandscape;
		Map<String, String> configInfo = getAssetsConfigInfo(context);
		if (configInfo != null) {
			LogUtils.d("configInfo:" + configInfo.toString());
			ConfigHolder.channelId = configInfo.get("channel_id");
			ConfigHolder.isOverseas = "1".equals(configInfo.get("is_overseas"));
			ConfigHolder.isOpenLog = "1".equals(configInfo.get("log_switch"));
			ConfigHolder.isUnion = "1".equals(configInfo.get("union_mode"));
		} else {
			LogUtils.d("configInfo:" + "渠道信息解析异常");
		}
		// 友盟统计
		MobclickAgent.setScenarioType(context, EScenarioType.E_UM_NORMAL);// 设置为普通统计场景类型
		String umAppKey = AppUtils.getMetaDataValue(context, "UMENG_APPKEY");
		String umChannel = AppUtils.getMetaDataValue(context, "UMENG_CHANNEL");
		UMAnalyticsConfig umAnalyticsConfig = new UMAnalyticsConfig(context, umAppKey, umChannel);
		MobclickAgent.startWithConfigure(umAnalyticsConfig);
		// 友盟推送
		PushAgent mPushAgent = PushAgent.getInstance(context);
		mPushAgent.setDebugMode(false);
		mPushAgent.register(new IUmengRegisterCallback() {
			@Override
			public void onSuccess(String deviceToken) { }

			@Override
			public void onFailure(String s, String s1) { }
		});
		//Facebook初始化
		if (ConfigHolder.isOverseas) {
			FacebookSdk.sdkInitialize(context);
			AppEventsLogger.activateApp((Application)context);
		}
	}
	
	// Activity初始化
	public void activityInit(Activity activity, TianyouCallback callback) {
		mActivity = activity;
		mTianyouCallback = callback;
		createFloatMenu();
		getServiceInfo();
		showLoginWay();
	}
	
	// 登陆接口
	public void login() {
		if (ConfigHolder.userIsLogin) {
			ToastUtils.show(mActivity, "用户已登录");
		} else {
			mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
		}
	}
	
	// 创建角色
	public void createRole(RoleInfo roleInfo) {
		if (!ConfigHolder.userIsLogin) {
			ToastUtils.show(mActivity, "请先登录！");
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		if (ConfigHolder.isUnion) {
			map.put("appid", ConfigHolder.gameId);
			map.put("token", ConfigHolder.gameToken);
			map.put("userid", ConfigHolder.userId);
			map.put("serverid", roleInfo.getServerId());
			map.put("servername", roleInfo.getServerName());
			map.put("roleid", roleInfo.getRoleId());
			map.put("rolename", roleInfo.getRoleName());
			map.put("profession", roleInfo.getProfession());
			map.put("level", roleInfo.getLevel());
			map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.gameToken + 
					ConfigHolder.userId + roleInfo.getServerId() + roleInfo.getRoleId()));
		} else {
			map.put("appID", ConfigHolder.gameId);
			map.put("userID", ConfigHolder.userId);
			map.put("channel", ConfigHolder.channelId);
			map.put("roleID", roleInfo.getRoleId());
			map.put("roleName", roleInfo.getRoleName());
			map.put("serverID", roleInfo.getServerId());
			map.put("serverName", roleInfo.getServerName());
			map.put("profession", roleInfo.getProfession());
		}
		String url = ConfigHolder.isUnion ? URLHolder.URL_UNION_CREATE_ROLE : URLHolder.URL_CREATE_ROLE;
		HttpUtils.post(mActivity, url, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) { }
		});
	}
	
	// 更新角色信息
	public void updateRoleInfo(RoleInfo roleInfo) {
		if (!ConfigHolder.userIsLogin) {
			ToastUtils.show(mActivity, "请先登录！");
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		if (ConfigHolder.isUnion) {
			map.put("appid", ConfigHolder.gameId);
			map.put("token", ConfigHolder.gameToken);
			map.put("userid", ConfigHolder.userId);
			map.put("serverid", roleInfo.getServerId());
			map.put("servername", roleInfo.getServerName());
			map.put("roleid", roleInfo.getRoleId());
			map.put("rolename", roleInfo.getRoleName());
			map.put("profession", roleInfo.getProfession());
			map.put("level", roleInfo.getLevel());
			map.put("viplevel", roleInfo.getVipLevel());
			map.put("balance", roleInfo.getVipLevel());
			map.put("amount", roleInfo.getAmount());
			map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.gameToken + 
					ConfigHolder.userId + roleInfo.getServerId() + roleInfo.getRoleId()));
			map.put("signtype", "md5");
		} else {
			map.put("appID", ConfigHolder.gameId);
			map.put("userID", ConfigHolder.userId);
			map.put("channel", ConfigHolder.channelId);
			map.put("roleId", roleInfo.getRoleId());
			map.put("roleLevel", roleInfo.getLevel());
			map.put("roleName", roleInfo.getRoleName());
			map.put("serverId", roleInfo.getServerId());
			map.put("serverName", roleInfo.getServerName());
			map.put("vipLevel", roleInfo.getVipLevel());
		}
		HttpUtils.post(mActivity, URLHolder.URL_UPDATE_ROLE_INFO, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) { }
		});
	}
	
	// 支付接口
	public void pay(String payInfo) {
		if (ConfigHolder.userIsLogin) {
			Intent intent = new Intent(mActivity, PayActivity.class);
			intent.putExtra("payInfo", payInfo);
			mActivity.startActivity(intent);
		} else {
			ToastUtils.show(mActivity, "请先登录！");
		}
	}
	
	// 支付接口
	public void pay(String payInfo, int money, String productDesc) {
		if (ConfigHolder.userIsLogin) {
			Intent intent = new Intent(mActivity, PayActivity.class);
			intent.putExtra("payInfo", payInfo);
			intent.putExtra("money", money);
			intent.putExtra("productDesc", productDesc);
			mActivity.startActivity(intent);
		} else {
			ToastUtils.show(mActivity, "请先登录！");
		}
	}

	// 退出游戏接口
	public void exitGame() {
		mActivity.startActivity(new Intent(mActivity, ExitActivity.class));
	}
	
	//获取assets配置信息
	private Map<String,String> getAssetsConfigInfo(Context context) {
		try {
			Map<String,String> map = new HashMap<String, String>();
			InputStream open = context.getAssets().open("tianyou_config.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(open);
			
			Element root = document.getDocumentElement();
			NodeList nodeList = root.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					map.put(node.getNodeName(), node.getTextContent());
				}
			}
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 显示隐藏登录方式
	private void showLoginWay() {
		Map<String,String> map = new HashMap<String, String>();
		if (ConfigHolder.isUnion) {
			map.put("appid", ConfigHolder.gameId);
			map.put("token", ConfigHolder.gameToken);
			map.put("type", "android");
			map.put("imei", AppUtils.getPhoeIMEI(mActivity));
			map.put("sign", ConfigHolder.gameId + ConfigHolder.gameToken);
			map.put("signtype", "md5");
		} else {
			map.put("appID", ConfigHolder.gameId);
			map.put("usertoken", ConfigHolder.gameToken);
		}
		String url = ConfigHolder.isUnion ? URLHolder.URL_UNION_LOGIN_WAY : URLHolder.URL_LOGIN_WAY;
		HttpUtils.post(mActivity, url, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				SPHandler.putString(mActivity, SPHandler.SP_LOGIN_WAY, response);
			}
		});
	}

	// 创建悬浮球接口
	private void createFloatMenu() {
		final Map<String, String> map = new HashMap<String, String>();
		map.put("appID", ConfigHolder.gameId);
		map.put("usertoken", ConfigHolder.gameToken);
		map.put("language", AppUtils.getLanguageSort(mActivity));
		HttpUtils.post(mActivity, URLHolder.URL_FLOAT_CONTROL, map, new HttpUtils.HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				FloatControl control = new Gson().fromJson(response, FloatControl.class);
				if (control.getResult().getCode() == 200) {
					SPHandler.putString(mActivity, SPHandler.SP_FLOAT_CONTROL, response);
					if (control.getResult().getStatus() != 0) {
						new FloatMenu(mActivity).createLogoPopupWindow();
					}
				} else {
					ToastUtils.show(mActivity, control.getResult().getMsg());
				}
			}
		});
	}
	
	// 获取客户服务信息
	private void getServiceInfo() {
		Map<String, String> map = new HashMap<String, String>();
		if (ConfigHolder.isUnion) {
			map.put("appid", ConfigHolder.gameId);
			map.put("token", ConfigHolder.gameToken);
			map.put("type", "android");
			map.put("imei", AppUtils.getPhoeIMEI(mActivity));
			map.put("sign", ConfigHolder.gameId + ConfigHolder.gameToken);
			map.put("signtype", "md5");
		} else {
			map.put("appID", ConfigHolder.gameId);
			map.put("usertoken", ConfigHolder.gameToken);
		}
		String url = ConfigHolder.isUnion ? URLHolder.URL_UNION_SERVER_INFO : URLHolder.URL_SERVER_IMG;
		HttpUtils.post(mActivity, url, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				ServerInfo serverInfo = new Gson().fromJson(response, ServerInfo.class);
    			if (serverInfo.getResult().getCode() == 200) {
    				CustominfoBean customInfo = serverInfo.getResult().getCustominfo();
    				SPHandler.putString(mActivity, SPHandler.SP_URL_PHONE, customInfo.getCall().getImgurl());
    				SPHandler.putString(mActivity, SPHandler.SP_URL_QQ, customInfo.getQq().getImgurl());
    				SPHandler.putString(mActivity, SPHandler.SP_URL_WX, customInfo.getWx().getImgurl());
    				SPHandler.putString(mActivity, SPHandler.SP_TEXT_PHONE, customInfo.getCall().getValue());
    				SPHandler.putString(mActivity, SPHandler.SP_TEXT_QQ, customInfo.getQq().getValue());
    				SPHandler.putString(mActivity, SPHandler.SP_TEXT_WX, customInfo.getWx().getValue());
    			}
			}
		});
	}
		
}
