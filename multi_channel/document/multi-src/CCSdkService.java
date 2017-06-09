package com.tianyou.channel.business;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.lion.ccpay.app.application.CCPayApplication;
import com.lion.ccpay.sdk.CCPaySdk;
import com.lion.ccpay.sdk.CCPaySdkApplicationUtils;
import com.lion.ccpay.sdk.OnAccountPwdChangeListener;
import com.lion.ccpay.sdk.OnLoginCallBack;
import com.lion.ccpay.sdk.OnLoginOutAction;
import com.lion.ccpay.sdk.OnPayListener;
import com.lion.ccpay.sdk.Stats;
import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.PayInfo;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.AppUtils;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;
import com.tianyou.channel.utils.URLHolder;

public class CCSdkService extends BaseSdkService {
	
	private static String tyAppID;
	private static String promotion;
	private static String uid;
	
	private RoleInfo mRoleInfo;

	// 在Application里的初始化方法
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		
		CCPaySdkApplicationUtils.getInstance((Application) context);
	}
	
	// 在Activity里的初始化方法
	@Override
	public void doActivityInit(Activity activity, TianyouCallback initCallback,
			TianyouCallback logoutCallback) {
		super.doActivityInit(activity, initCallback, logoutCallback);
		
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(mActivity);
		tyAppID = channelInfo.getGameId();
		promotion = channelInfo.getChannelId();
		
		CCPaySdk.getInstance().init(mActivity);
		CCPaySdk.getInstance().setOnAccountPwdChangeListener(new OnAccountPwdChangeListener() {
			
			@Override
			public void onAccountPwdChange() {
				CCPaySdk.getInstance().onOffline();
				mLogoutCallback.onSuccess("修改密码成功");
				Log.d("TAG", "onAccountPwdChangeListener-----------------");
			}
		});
		
		CCPaySdk.getInstance().setOnLoginOutAction(new OnLoginOutAction() {
			
			@Override
			public void onLoginOut() {
				mLogoutCallback.onSuccess("注销登录成功");
				Log.d("TAG", "onLoginOutAction--------------------");
			}
		});
	}
	
	// 登录
	@Override
	public void doLogin(final TianyouCallback callback) {
		CCPaySdk.getInstance().login(new OnLoginCallBack() {
			
			@Override
			public void onLoginSuccess(String sid, String token, String userName) {
				Log.d("TAG", "uid= "+sid+",token= "+token+",userName= "+userName);
				checkLogin(sid,token,callback);
			}
			
			@Override
			public void onLoginFail() {
				callback.onFailed("登录失败");
			}
			
			@Override
			public void onLoginCancel() {
				callback.onFailed("登录取消");
			}
		});
	}
	
	// 上传游戏信息
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		mRoleInfo = roleInfo;
	}
	
	// 支付
	@Override
	public void doPay(TianyouCallback callback, String payCode) {
		PayInfo payInfo = ConfigHolder.getPayInfo(mActivity, payCode);
		String productID = payInfo.getProductId();
		String productName = payInfo.getProductName();
		String productDesc = payInfo.getProductDesc();
		String money = payInfo.getPayMoney();
		
		Map<String, String> payParam = new HashMap<String, String>();
		payParam.put("userId", uid);
		payParam.put("appID", tyAppID);
		payParam.put("roleId", mRoleInfo.getRoleId());
		payParam.put("serverID", mRoleInfo.getServerId());
		payParam.put("serverName", mRoleInfo.getServerName());
		payParam.put("customInfo", mRoleInfo.getCustomInfo());
		payParam.put("productId",productID);
		payParam.put("productName", productName);
		payParam.put("productDesc", productDesc);
		payParam.put("moNey",money);
		payParam.put("promotion", promotion);
		Log.d("TAG", "lenovo pay param= "+payParam);
		createOrder(payParam,callback);
	}
	
	// 退出游戏
	@Override
	public void doExitGame(TianyouCallback callback) {
		super.doExitGame(callback);
		CCPaySdk.getInstance().onLogOutApp();
	}
	
	// 生命周期方法
	@Override
	public void doResume() {
		Stats.onResume(mActivity);
	}
	
	@Override
	public void doPause() {
		Stats.onPause(mActivity);
	}
	
	
	// 检查登录
	private void checkLogin(String sid,String token,final TianyouCallback callback) {
		String phoneIMEI = AppUtils.getPhoeIMEI(mActivity);
		String mdSignature = AppUtils.MD5("session="+token+"&uid="+sid+"&appid="+tyAppID);
		Map<String, String> loginParam = new HashMap<String, String>();
		loginParam.put("uid",sid);
		loginParam.put("session",token);
		loginParam.put("imei",phoneIMEI);
		loginParam.put("appid",tyAppID);
		loginParam.put("promotion",promotion);
		loginParam.put("signature", mdSignature);
		Log.d("TAG","lenovo login param= "+loginParam);
		HttpUtils.post(mActivity, URLHolder.LOCAL_LOGIN, loginParam, new HttpCallback() {
			
			@Override
			public void onSuccess(String data) {
				Log.d("TAG", "cc login ty check success data= "+data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONObject result = jsonObject.getJSONObject("result");
					String code = result.getString("code");
					if ("200".equals(code)){
						uid = result.getString("uid");
						callback.onSuccess(uid);
					} else {
						callback.onFailed(result.getString("msg"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					callback.onFailed(e.getMessage());
				}
			}
			
			@Override
			public void onFailed(String code) {
				Log.d("TAG", "cc login ty check failed code= "+code);
				callback.onFailed(code);
			}
		});
	}
	
	// 创建订单
	private void createOrder(Map<String, String> payParam,final TianyouCallback callback) {
		Log.d("TAG", "cc pay ty createOrder param= "+payParam);
		HttpUtils.post(mActivity, URLHolder.BASE_URL+URLHolder.CREATE_ORDER_URL, payParam, new HttpCallback() {
			
			@Override
			public void onSuccess(String data) {
				Log.d("TAG", "cc create order success data= "+data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONObject result = (JSONObject) jsonObject.get("result");
					String code = result.getString("code");
					if ("200".equals(code)){
						String orderID = result.getJSONObject("orderinfo").getString("orderID");
						String price = result.getJSONObject("orderinfo").getString("moNey");
						String waresid = result.getJSONObject("orderinfo").getString("waresid");
						doCCPay(waresid,orderID,price,callback);
					} else {
						callback.onFailed(data);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					callback.onFailed(e.getMessage());
				}
			}
			
			@Override
			public void onFailed(String code) {
				Log.d("TAG", "lenovo create order failed code= "+code);
			}
		});
	}
	
	// 虫虫支付
	private void doCCPay(String waresid,final String orderID,String money,final TianyouCallback callback){
		Log.d("TAG", "productID= "+waresid+",orderID= "+orderID+",money= "+money);
		
		CCPaySdk.getInstance().pay(waresid, orderID, money, new OnPayListener() {
			
			@Override
			public void onPayResult(int status, String orderId, String money) {
				Log.d("TAG", "status= "+status+",orderID= "+orderId+",money= "+money);
				
				Map<String, String> checkParam = new HashMap<String, String>();
				checkParam.put("orderID",orderID);
				checkParam.put("userId",uid);
				checkParam.put("promotion", promotion);
				switch (status) {
					case OnPayListener.CODE_SUCCESS:
						Log.d("TAG", "cc check order success param= "+checkParam);
						checkOrder(checkParam,callback);
						break;
						
					case OnPayListener.CODE_UNKNOW:
						Log.d("TAG", "cc check order unknown param= "+checkParam);
						checkOrder(checkParam,callback);
						break;
						
					case OnPayListener.CODE_FAIL:
						callback.onFailed("支付失败");
						break;
						
					case OnPayListener.CODE_CANCEL:
						callback.onFailed("支付失败");
						break;
				}
			}
		});
	}
	
	// 查单
	private void checkOrder(Map<String, String> checkParam,final TianyouCallback callback){
		HttpUtils.post(mActivity, URLHolder.BASE_URL+URLHolder.CHECK_ORDER_URL, checkParam, new HttpCallback() {
			
			@Override
			public void onSuccess(String data) {
				Log.d("TAG", "cc check order success data= "+data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONObject result = jsonObject.getJSONObject("result");
					String code = result.getString("code");
					String msg = result.getString("msg");
					if ("200".equals(code)){
						callback.onSuccess(msg);
					} else {
						callback.onFailed(data);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					callback.onFailed(e.getMessage());
				}
			}

			@Override
			public void onFailed(String code) {
				Log.d("TAG", "cc check order failed code= "+code);
				callback.onFailed(code);
			}
		});
	}
	
}
