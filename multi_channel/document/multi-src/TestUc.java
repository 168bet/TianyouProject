package com.tianyou.channel.business;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import cn.uc.gamesdk.UCGameSdk;
import cn.uc.gamesdk.even.SDKEventKey;
import cn.uc.gamesdk.even.SDKEventReceiver;
import cn.uc.gamesdk.even.Subscribe;
import cn.uc.gamesdk.exception.AliLackActivityException;
import cn.uc.gamesdk.exception.AliNotInitException;
import cn.uc.gamesdk.open.GameParamInfo;
import cn.uc.gamesdk.open.OrderInfo;
import cn.uc.gamesdk.open.UCOrientation;
import cn.uc.gamesdk.param.SDKParamKey;
import cn.uc.gamesdk.param.SDKParams;

import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.LogUtils;

public class TestUc extends BaseSdkService{
	
	public boolean mRepeatCreate = false;
	private boolean isLand = true;
	private String mSid;
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		isLand = island;
	}
	
	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		// 注册时间通知接口
		UCGameSdk.defaultSdk().registerSDKEventReceiver(mEventReceiver);
		
		/**
		 * app拉起设置
		 */
		// 1.读取拉起参数
		Intent intent = mActivity.getIntent();
		String pullupInfo = intent.getDataString();
		
		// 2.初始化SDK时传递拉起参数
		SDKParams sdkParams = new SDKParams();
		sdkParams.put(SDKParamKey.PULLUP_INFO, pullupInfo);
		try {UCGameSdk.defaultSdk().initSdk(mActivity, sdkParams);} catch (AliLackActivityException e) {e.printStackTrace();}
		
		// 3.生命周期回调函数修改
		if ((mActivity.getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			mRepeatCreate = true;
			mActivity.finish();
			return;
		}
		
		// 初始化SDK
		GameParamInfo gpi = new GameParamInfo();
		gpi.setGameId(Integer.parseInt(mChannelInfo.getAppId()));
		gpi.setEnableUserChange(true);
		gpi.setOrientation((isLand ? UCOrientation.LANDSCAPE : UCOrientation.PORTRAIT));
		
		SDKParams initSdkParams = new SDKParams();
		initSdkParams.put(SDKParamKey.DEBUG_MODE, false);
		initSdkParams.put(SDKParamKey.GAME_PARAMS, gpi);
		try {
			UCGameSdk.defaultSdk().initSdk(mActivity, initSdkParams);
		} catch (AliLackActivityException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		try {
			UCGameSdk.defaultSdk().login(mActivity, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		SDKParams paySdkParams = new SDKParams();
		paySdkParams.put(SDKParamKey.AMOUNT, orderInfo.getMoNey());
		paySdkParams.put(SDKParamKey.NOTIFY_URL, orderInfo.getNotifyurl());
		paySdkParams.put(SDKParamKey.CP_ORDER_ID, orderInfo.getOrderID());
		paySdkParams.put(SDKParamKey.ACCOUNT_ID, mSid);
		paySdkParams.put(SDKParamKey.SIGN_TYPE, "MD5");
		paySdkParams.put(SDKParamKey.SIGN, orderInfo.getSigninfo());
		paySdkParams.put(SDKParamKey.CALLBACK_INFO, payInfo.getCustomInfo());
		try {
			UCGameSdk.defaultSdk().pay(mActivity, paySdkParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doStart() { if (mRepeatCreate) return; }
	
	@Override
	public void doRestart() { if (mRepeatCreate) LogUtils.d("onRestart is repeat activity!"); }
	
	@Override
	public void doStop() { if (mRepeatCreate) return; }
	
	@Override
	public void doNewIntent(Intent intent) { if (mRepeatCreate) LogUtils.d("onNewIntent is repeat activity!"); }
	
	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) { if (mRepeatCreate) return;	}
	
	@Override
	public void doResume() { if (mRepeatCreate) return;	}
	
	@Override
	public void doPause() { if (mRepeatCreate) return;	}
	
	@Override
	public void doDestory() { 
		UCGameSdk.defaultSdk().unregisterSDKEventReceiver(mEventReceiver); 
		if (mRepeatCreate) return;
	}
	
	private SDKEventReceiver mEventReceiver = new SDKEventReceiver(){
		
		@Subscribe(event = SDKEventKey.ON_INIT_SUCC)	// 初始化完成
		private void onInitSuccess() { mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "初始化完成");}
		
		@Subscribe(event = SDKEventKey.ON_LOGIN_SUCC)	// 登录成功
		private void onLoginSuccess(String sid) {
			mSid = sid;
			LoginInfo loginParam = new LoginInfo();
			loginParam.setChannelUserId(sid);
			loginParam.setUserToken(sid);
			checkLogin(loginParam);
		}
		
		@Subscribe(event = SDKEventKey.ON_LOGIN_FAILED)	// 登录失败
		private void onLoginFailed() { mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");}
		
		@Subscribe(event = SDKEventKey.ON_CREATE_ORDER_SUCC)
		private void onCreateOderSuccess(OrderInfo or){}
		
	};
	
	

}
