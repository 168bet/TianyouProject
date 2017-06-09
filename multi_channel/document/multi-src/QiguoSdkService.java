package com.tianyou.channel.business;

import android.app.Activity;
import android.os.Handler;

import com.kding.api.QiGuoApi;
import com.kding.api.QiGuoCallBack;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.PayInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.LogUtils;

public class QiguoSdkService extends BaseSdkService{

	private QiGuoApi mQiguoInstance;
	private String orderID;
	
	/**
	 * Activity初始化接口
	 */
	@Override
	public void doActivityInit(Activity activity,TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		mQiguoInstance = QiGuoApi.INSTANCE;		// 实例化QiGuoApi对象
		
		mQiguoInstance.initSdk(mActivity, ConfigHolder.getChannelInfo(mActivity).getAppId(), mInitCallBack);	// SDK初始化
		
	}
	
	/**
	 * 登录接口
	 */
	@Override
	public void doLogin() { super.doLogin(); mQiguoInstance.showLogin(mLoginCallBack);}
	
	/**
	 * 支付接口
	 */
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		PayInfo prodcutInfo = ConfigHolder.getPayInfo(mActivity, payInfo.getPayCode());
		orderID = orderInfo.getOrderID();
		mQiguoInstance.pay(prodcutInfo.getProductName(), prodcutInfo.getProductDesc(), orderInfo.getMoNey(), orderID, mPayCallBack);
	}
	
	/**
	 * 生命周期方法
	 */
	@Override
	public void doResume() { mQiguoInstance.onResume();}
	
	@Override
	public void doPause() { mQiguoInstance.onPause();}
	
	/************************		回调接口		*************************************/

	// SDK初始化回调接口
	private QiGuoCallBack mInitCallBack = new QiGuoCallBack() {
		
		@Override
		public void onSuccess() { mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");}		// 初始化成功的回调
		
		@Override
		public void onFailure(String msg) { LogUtils.e("SDK初始化失败	msg= "+msg);}	// 初始化失败的回调
		
		@Override
		public void onCancel() { LogUtils.d("SDK初始化取消");}	// 初始化取消的回调
		
	};
	
	// SDK登录回调接口
	private QiGuoCallBack mLoginCallBack = new QiGuoCallBack() {
		
		@Override
		public void onSuccess() {
			String qiguoUid = mQiguoInstance.getUserId();
//			LoginInfo loginParam = new LoginInfo();
//			loginParam.setChannelUserId(qiguoUid);
			mLoginInfo.setChannelUserId(qiguoUid);
			checkLogin();
		}
		
		@Override
		public void onFailure(String msg) {	LogUtils.e("登录失败 msg= "+msg);	mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");}
		
		@Override
		public void onCancel() { LogUtils.d("登录取消"); mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "登录取消");}
		
	};
	
	//	SDK支付回到接口
	private QiGuoCallBack mPayCallBack = new QiGuoCallBack() {
		
		@Override
		public void onSuccess() { 
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					checkOrder(orderID);
				}
			}, 1000);
		}
		
		@Override
		public void onFailure(String msg) { LogUtils.e("支付失败 msg= "+msg); mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");}
		
		@Override
		public void onCancel() { LogUtils.d("支付取消");}
	};
	
}
