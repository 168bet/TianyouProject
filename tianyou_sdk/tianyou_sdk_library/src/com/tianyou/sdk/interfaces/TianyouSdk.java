package com.tianyou.sdk.interfaces;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.tianyou.sdk.activity.ExitActivity;
import com.tianyou.sdk.activity.FloatMenu;
import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.FloatControl;
import com.tianyou.sdk.bean.PayInfo;
import com.tianyou.sdk.bean.RoleInfo;
import com.tianyou.sdk.bean.ServerInfo;
import com.tianyou.sdk.bean.ServerInfo.ResultBean.CustominfoBean;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.PayHandler;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
//		MobclickAgent.setSecret(mActivity, "");
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
		getPayWay();
		getPayValue();
	}

	// 充值金额数值
	private void getPayValue() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("paytype", "game");
		map.put("imei", AppUtils.getPhoeIMEI(mActivity));
		map.put("sign", ConfigHolder.gameId + ConfigHolder.gameToken + ConfigHolder.userId);
		HttpUtils.post(mActivity, URLHolder.URL_MONEY_VALUE, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				SPHandler.putString(mActivity, SPHandler.SP_PAY_MONEY, response);
			}
		});
	}

	// 支付方式控制
	private void getPayWay() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("imei", AppUtils.getPhoeIMEI(mActivity));
		map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.gameToken + ConfigHolder.userId));
		HttpUtils.post(mActivity, URLHolder.URL_PAY_WAY, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				SPHandler.putString(mActivity, SPHandler.SP_PAY_WAY, response);
			}
		});
	}
	
	// 显示隐藏登录方式
	private void showLoginWay() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("sign", ConfigHolder.gameId + ConfigHolder.gameToken);
		HttpUtils.post(mActivity, URLHolder.URL_LOGIN_WAY, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				SPHandler.putString(mActivity, SPHandler.SP_LOGIN_WAY, response);
			}
		});
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
		map.put("userid", ConfigHolder.userId);
		map.put("serverid", roleInfo.getServerId());
		map.put("servername", roleInfo.getServerName());
		map.put("roleid", roleInfo.getRoleId());
		map.put("rolename", roleInfo.getRoleName());
		map.put("profession", roleInfo.getProfession());
		map.put("level", roleInfo.getLevel());
		map.put("sociaty", roleInfo.getSociaty());
		map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.gameToken + 
				ConfigHolder.userId + roleInfo.getServerId() + roleInfo.getRoleId()));
		HttpUtils.post(mActivity, URLHolder.URL_UNION_CREATE_ROLE, map, new HttpsCallback() {
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
		map.put("sociaty", roleInfo.getSociaty());
		map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.gameToken + 
				ConfigHolder.userId + roleInfo.getServerId() + roleInfo.getRoleId()));
		HttpUtils.post(mActivity, URLHolder.URL_UNION_UPDATE_ROLE, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) { }
		});
	}
	
	// 支付接口
	public void pay(PayInfo payInfo) {
		if (ConfigHolder.userIsLogin) {
			PayHandler.getInstance(mActivity).doPay(payInfo, false);
		} else {
			ToastUtils.show(mActivity, "请先登录！");
		}
	}
	
	// 支付接口
	public void pay(PayInfo payInfo, boolean isShowChooseMoney) {
		if (ConfigHolder.userIsLogin) {
			PayHandler.getInstance(mActivity).doPay(payInfo, isShowChooseMoney);
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
	
	// 创建悬浮球接口
	private void createFloatMenu() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.gameToken));
		HttpUtils.post(mActivity, URLHolder.URL_FLOAT_CONTROL, map, new HttpUtils.HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				FloatControl control = new Gson().fromJson(response, FloatControl.class);
				if (control.getResult().getCode() == 200) {
					com.tianyou.sdk.base.FloatControl.ResultBean.CustominfoBean custominfo = control.getResult().getCustominfo();
					if (custominfo.getLockstatus() == 1) {
						SPHandler.putString(mActivity, SPHandler.SP_FLOAT_CONTROL, response);
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
		map.put("sign", ConfigHolder.gameId + ConfigHolder.gameToken);
		HttpUtils.post(mActivity, URLHolder.URL_SERVER_INFO, map, new HttpsCallback() {
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
