package com.tianyou.channel.business;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.yyjia.sdk.center.GMcenter;
import com.yyjia.sdk.data.Information;
import com.yyjia.sdk.listener.InitListener;
import com.yyjia.sdk.listener.LoginListener;
import com.yyjia.sdk.listener.PayListener;

public class ErWuOUSdkService extends BaseSdkService {

	private GMcenter mGMcenter;
	
	private static TianyouCallback mLoginCallback;
	
	// 在Activity里初始化
	@Override
	public void doActivityInit(Activity activity, final TianyouCallback initCallback,
			TianyouCallback logoutCallback) {
		super.doActivityInit(activity, initCallback, logoutCallback);
		
		// 实例化GMcenter对象
		if (mGMcenter == null) mGMcenter = GMcenter.getInstance1(mActivity);
		
		// 初始化sdk
		mGMcenter.init();
		
		// 设置初始化成功的监听
		mGMcenter.setInitListener(new InitListener() {
			@Override
			public void initSuccessed(String code) {
				if (code == Information.INITSUCCESSEDS) {
					initCallback.onSuccess("初始化成功");
				}
			}
		});
		
		// 设置登录登出的监听
		mGMcenter.setLoginListener(new OnLoginListener());
	}
	
	
	// 登录
	@Override
	public void doLogin(TianyouCallback callback) {
		mLoginCallback = callback;
		mGMcenter.checkLogin();
	}
	
	
	@Override
	public void doChannelPay(final String orderId, String price, JSONObject result,
			final TianyouCallback callback) {
		
		mGMcenter.pay(mActivity, Float.parseFloat(price), mPayInfo.getProductName(), 
				mRoleInfo.getServerId(), 
				mRoleInfo.getRoleId(), orderId, 
				mRoleInfo.getCustomInfo(), 
				new PayListener() {
			
			@Override
			public void paySuccessed(String code, String orderID) {
				if (code == Information.PAY_SUSECCED) {
					Log.d("TAG", "pay success-------------code= "+code+",orderID= "+orderID);
					// 支付成功的回调
					checkOrder(orderId, callback);
				} else if (code == Information.PAY_ING) {
					// 支付进行中的回调
					Log.d("TAG", "ing---------------");
				} else {
					// 支付失败的回调
					callback.onFailed("支付失败");
				}
			}
			
			@Override
			public void payGoBack() {
				Log.d("TAG", "goback-----------------");
				// 支付返回的回调
				callback.onFailed("支付返回");
			}
		});
	}
	
	
	// 自定义类实现LoginListener接口，处理登陆登出后的逻辑
	private class OnLoginListener implements LoginListener {

		@Override
		public void loginSuccessed(String code) {
			if (code == Information.LOGIN_SUSECCEDS) {
				// 登录成功的回调
				String sid = mGMcenter.getSid();
				Log.d("TAG", "sid= "+sid);
				checkLogin("", sid, mLoginCallback);
			} else {
				// 登录失败的回调
				mLoginCallback.onFailed("登录失败");
			}
		}
		
		@Override
		public void logcancelSuccessed(String code) {
			if (code == Information.LOGCANCEL_SUSECCED) {
				// 取消登录的回调
				mLoginCallback.onFailed("用户取消登录");
			}
		}


		@Override
		public void logoutSuccessed(String code) {
			if (code == Information.LOGOUT_SUSECCED) {
				// 登出成功的回调
				mLogoutCallback.onSuccess("退出账号成功");
				Log.d("TAG", "logout success----------");
			} else {
				// 登出失败的回调
				mLogoutCallback.onFailed("退出账号失败");
				Log.d("TAG", "logou failed-----------");
			}
		}
		
	}
}
