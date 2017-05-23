package com.tianyou.channel.business;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.haima.loginplugin.ZHErrorInfo;
import com.haima.loginplugin.ZHPayUserInfo;
import com.haima.loginplugin.callback.OnCheckUpdateListener;
import com.haima.loginplugin.callback.OnLoginCancelListener;
import com.haima.loginplugin.callback.OnLoginListener;
import com.haima.payPlugin.callback.OnPayCancelListener;
import com.haima.payPlugin.callback.OnPayListener;
import com.haima.payPlugin.infos.ZHPayOrderInfo;
import com.haima.plugin.haima.HMPay;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;

public class HaiMaSdkService extends BaseSdkService{
	
	private static MyOnLoginListener mOnLoginListener;
	private static MyOnLoginCancleListener mOnLoginCancleListener;
 	private static MyOnPayCancleListener payCancleListener;
	private static boolean isLand;
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		isLand = island;
	}
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		HMPay.init(mActivity, isLand, mChannelInfo.getAppId(),new OnCheckUpdateListener() {
			@Override
			public void onCheckUpdateSuccess(boolean arg0, boolean arg1, boolean arg2) {
				Log.d("TAG", "on check update success ");
				mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
			}
			
			@Override
			public void onCheckUpdateFailed(ZHErrorInfo arg0, int arg1) {
//				initCallback.onFailed("初始化失败");
				LogUtils.e("SDK初始化失败");
			}
		}, false, HMPay.CHECKUPDATE_FAILED_SHOW_NONE);
		mOnLoginListener = new MyOnLoginListener();
		mOnLoginCancleListener = new MyOnLoginCancleListener();
		HMPay.setLoginListener(mOnLoginListener);
		HMPay.setLoginCancelListener(mOnLoginCancleListener);
		HMPay.setPayCancelListener(new MyOnPayCancleListener());
	}
	
	
	@Override
	public void doLogin() {
		Log.d("TAG", "login-----------------------");
		
		HMPay.login(mActivity, mOnLoginListener);
	}
	
	@Override
	public void doExitGame() {
		super.doExitGame();
	}
	
	@Override
	public void doChannelPay(OrderinfoBean info) {
		super.doChannelPay(info);
		
		final String orderID = info.getOrderID();
		ZHPayOrderInfo orderInfo = new ZHPayOrderInfo();
		orderInfo.gameName = "龙神之光";
		orderInfo.goodName = mPayInfo.getProductName();
		orderInfo.goodPrice = Float.parseFloat(info.getMoNey());
		orderInfo.orderNo = orderID; 
		orderInfo.userParam = mRoleInfo.getCustomInfo();
		
		HMPay.pay(orderInfo, mActivity,new OnPayListener() {
			
			@Override
			public void onPaySuccess(ZHPayOrderInfo info) {
				checkOrder(orderID);
			}
			
			@Override
			public void onPayFailed(ZHPayOrderInfo info, ZHErrorInfo errorInfo) {
				Log.d("TAG", "pay failed----------"+info.toString()+",erroinfo= "+errorInfo.toString());
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED,"支付失败");
			}
		});
		
	}
	
	private class MyOnLoginListener implements OnLoginListener {

		@Override
		public void onLogOut() {
//			mLogoutCallback.onSuccess("注销成功");
			mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT,"注销成功");
		}

		@Override
		public void onLoginFailed(ZHErrorInfo arg0) {
			Log.d("TAG", "login failed-----------");
			mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
		}

		@Override
		public void onLoginSuccess(ZHPayUserInfo userInfo) {
			String sid = userInfo.getUserId();
			String token = userInfo.getLoginToken();
//			String userName = userInfo.getUserName();
			checkLogin(sid, token);
		}
		
	}
	
	private class MyOnLoginCancleListener implements OnLoginCancelListener {
		
		@Override
		public void onLoginCancel() {
//			callback.onFailed("用户取消登录");
			mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "用户取消登录");
		}
		
	}
	
	private class MyOnPayCancleListener implements OnPayCancelListener {
		
		@Override
		public void onPayCancel() {
//			callback.onFailed("用户取消支付");
			mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "用户取消支付");
		}
		
	}
	
	@Override
	public void doResume() {
		HMPay.onResume(mActivity);
	}
	
	@Override
	public void doPause() {
		HMPay.onPause();
	}
	
	@Override
	public void doDestory() {
		HMPay.removeLoginListener(mOnLoginListener);
		HMPay.removeLoginCancelListener(mOnLoginCancleListener);
		HMPay.removePayCancelListener(payCancleListener);
	}
	

}
