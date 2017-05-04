package com.tianyou.channel.business;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.sogou.gamecenter.sdk.SogouGamePlatform;
import com.sogou.gamecenter.sdk.bean.SogouGameConfig;
import com.sogou.gamecenter.sdk.bean.UserInfo;
import com.sogou.gamecenter.sdk.listener.InitCallbackListener;
import com.sogou.gamecenter.sdk.listener.LoginCallbackListener;
import com.sogou.gamecenter.sdk.listener.OnExitListener;
import com.sogou.gamecenter.sdk.listener.PayCallbackListener;
import com.sogou.gamecenter.sdk.listener.SwitchUserListener;
import com.sogou.gamecenter.sdk.views.FloatMenu;
import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.OrderInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.LogUtils;

public class SogouSdkService extends BaseSdkService {
	
	private SogouGamePlatform mPlatform = SogouGamePlatform.getInstance();
	private FloatMenu mFloatMenu;
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(context);
		
		SogouGameConfig config = new SogouGameConfig();
		config.devMode = false;
		config.gid = Integer.parseInt(channelInfo.getAppId());
		config.appKey = channelInfo.getAppKey();
		config.gameName = channelInfo.getCpId();
		mPlatform.prepare(context, config);
	}
	
	@Override
	public void doApplicationTerminate() {
		mPlatform.onTerminate();
	}
	
	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		mPlatform.init(mActivity, new InitCallbackListener() {
			
			@Override
			public void initSuccess() {
				mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化成功");
				mFloatMenu = mPlatform.createFloatMenu(mActivity, true);
				mFloatMenu.setParamsXY(10, 100);
				mFloatMenu.setSwitchUserListener(new SwitchUserListener() {
					@Override
					public void switchSuccess(int code, final UserInfo userInfo) {
						Log.d("TAG", "logout success -----------");
//						mLogoutCallback.onSuccess("退出账号");
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "注销成功");
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								LoginInfo loginParam = new LoginInfo();
								loginParam.setChannelUserId(userInfo.getUserId()+"");
								loginParam.setUserToken(userInfo.getSessionKey());
								checkLogin(loginParam);
							}
						}, 2000);
					}
					
					@Override
					public void switchFail(int code, String msg) {
						Log.d("TAG", "logout failed--------------");
//						mLogoutCallback.onFailed("退出账号失败");
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "用户取消登录");
					}
				});
			}
			
			@Override
			public void initFail(int code, String msg) {
				LogUtils.e("搜狗SDK初始化失败: "+msg);
			}
		});
	}
	
	@Override
	public void doLogin() {
		mPlatform.login(mActivity, new LoginCallbackListener() {
			
			@Override
			public void loginSuccess(int code, UserInfo userInfo) {
				mFloatMenu.show();
				Log.d("TAG", "sid= "+userInfo.getUserId()+",session= "+userInfo.getSessionKey());
				LoginInfo loginParam = new LoginInfo();
				loginParam.setChannelUserId(userInfo.getUserId()+"");
				loginParam.setUserToken(userInfo.getSessionKey());
				checkLogin(loginParam);
			}
			
			@Override
			public void loginFail(int code, String msg) {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败: "+msg);
			}
		});
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		Map<String, Object> optionData = new HashMap<String, Object>();
		
		optionData.put("currency",orderInfo.getCurrency());
		optionData.put("rate", Float.parseFloat(orderInfo.getRate()));
		optionData.put("amount", Integer.parseInt(orderInfo.getMoNey()));
		optionData.put("product_name", mPayInfo.getProductName());
		optionData.put("app_data",orderInfo.getOrderID());
		optionData.put("appmodes", false);
		
		mPlatform.pay(mActivity, optionData, new PayCallbackListener() {
			
			@Override
			public void paySuccess(String orderId, String appData) {
				Log.d("TAG", "pay success orderID= "+orderId+",appdata= "+appData);
				checkOrder(appData);
			}
			
			@Override
			public void payFail(int code, String orderId, String appData) {
				Log.d("TAG", "pay failed orderID= "+orderId+",appdata= "+appData);
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
			}
		});
	}
	
	@Override
	public void doExitGame() {
//		super.doExitGame(callback);
		mPlatform.exit(new OnExitListener(mActivity) {
			
			@Override
			public void onCompleted() {
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "退出游戏");
			}
		});
	}
	
	

}
