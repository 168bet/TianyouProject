package com.multi.channel;

import org.json.JSONException;
import org.json.JSONObject;

import com.iqiyi.sdk.listener.LoginListener;
import com.iqiyi.sdk.listener.PayListener;
import com.iqiyi.sdk.platform.GamePlatform;
import com.iqiyi.sdk.platform.GamePlatformInitListener;
import com.iqiyi.sdk.platform.GameUser;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;

import android.app.Activity;
import android.util.Log;

public class ChannelService extends BaseSdkService {

	private static String gameId = "10003";
	private static GamePlatform platform; 
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		platform = GamePlatform.getInstance();
		platform.initPlatform(mActivity, gameId,new GamePlatformInitListener() {
			@Override
			public void onSuccess() {
				doNoticeGame(TianyouCallback.CODE_INIT, "");
				platform.addLoginListener(new LoginListener() {
					@Override
					public void exitGame() {
						doNoticeGame(TianyouCallback.CODE_QUIT_SUCCESS, "");
					}
					
					@Override
					public void logout() {
						doNoticeGame(TianyouCallback.CODE_LOGOUT, "");
					}
					
					@Override
					public void loginResult(int arg0, GameUser arg1) {
						doNoticeGame(TianyouCallback.CODE_QUIT_SUCCESS, "");
						platform.initSliderBar(mActivity);
						getUserType();
					}
				});
				platform.addPaymentListener(new PayListener() {
					@Override
					public void paymentResult(int arg0) {
						Log.d("SDKPlatfrom", "Payment paymentResult : " + arg0);
					}
					@Override
					public void leavePlatform() {
						Log.d("SDKPlatfrom", "Payment leavePlatform");
					}
				});
			}
			
			@Override
			public void onFail(String msg) {
				doNoticeGame(TianyouCallback.CODE_PAY_FAILED, "");
			}
		});  
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		platform.iqiyiUserLogin(mActivity);
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		platform.iqiyiPayment(mActivity, Integer.parseInt(mPayInfo.getMoney()), 
				mRoleInfo.getRoleId(), "ppsmobile_s1", "cp message");
	}
	
	@Override
	public void doLogout() {
		super.doLogout();
		platform.iqiyiChangeAccount(mActivity);
	}
	
	@Override
	public void doExitGame() {
		platform.iqiyiUserLogout(mActivity);
	}

	/**
	 * 获取用户的VIP等级
	 */
	private void getUserType() {
		try {
			JSONObject json = platform.getQIYIType();
			if (json != null) {
				String type = json.getString("type"); // 0：非会员， 1：会员
				int level = json.getInt("level"); // 会员等级
				Log.d("SDKPlatfrom", "VIP : " + type + ", levele : " + level);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
