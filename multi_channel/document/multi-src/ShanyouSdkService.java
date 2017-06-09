package com.tianyou.channel.business;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.AppUtils;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.URLHolder;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;
import com.vqs.sdk.VqsManager;
import com.vqs.sdk.http.LoginListener;
import com.vqs.sdk.http.PayListener;

public class ShanyouSdkService extends BaseSdkService{

	private VqsManager mManager;

	@Override
	public void doActivityInit(Activity activity,TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		mManager = VqsManager.getInstance();
		mManager.init(mActivity);
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
		LogUtils.d("SDK初始化完成");
	}
	
	@Override
	public void doLogin() { 
		mManager.setLoginListener(new MyLoginListener()); 
		mManager.play(); 
	}

	@Override
	public void doChannelPay(OrderinfoBean orderInfo) {
		super.doChannelPay(orderInfo);
		String orderID = orderInfo.getOrderID();
		mManager.setPayListener(new MyPayListener(orderID));
		mManager.Pay(orderInfo.getMoNey(), orderInfo.getProductName(), orderID,orderID );
	}
	
	// 生命周期方法
	
	@Override
	public void doResume() {	mManager.onResume();	}
	
	@Override
	public void doPause() {	mManager.onPause();	}
	
	@Override
	public void doStop() {	mManager.onStop();	}
	
	@Override
	public void doDestory() {	mManager.onDestroy();	}
	
	// 监听登录的接口
	private class MyLoginListener implements LoginListener {

		@Override
		public void LoginCanle(String msg) { mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "登录取消"); }

		@Override
		public void LoginFailure(String msg) { mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败"); }

		@Override
		public void LoginSuccess(String result, String userID, String userName,String loginTime, String sign) { SYCheckLogin(userID, sign, loginTime); }
	}
	
	// 监听支付的接口
	private class MyPayListener implements PayListener {

		private String orderID;

		public MyPayListener (String orderID) {
			this.orderID = orderID;
		}

		@Override
		public void PayCanle(String msg) { mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "支付取消"); }

		@Override
		public void PayFailure(String msg) { mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败"); }

		@Override
		public void PaySuccess(String msg) { checkOrder(orderID); }
		
	}
	
	// 验证登录
	private void SYCheckLogin(String uid,String session,String loginTime) {
		mChannelUserId = uid;
    	String gameId = mChannelInfo.getGameId();
		Map<String, String> param = new HashMap<String, String>();
		param.put("uid", uid);
		param.put("session", session);
		param.put("imei", AppUtils.getPhoeIMEI(mActivity));
		param.put("appid", gameId);
		param.put("promotion", mChannelInfo.getChannelId());
		param.put("logintime", loginTime);
		param.put("signature", AppUtils.MD5("session=" + session + "&uid=" + uid + "&appid=" + gameId));
		LogUtils.d("session:" + param.get("session"));
		HttpUtils.post(mActivity, URLHolder.CHECK_LOGIN_URL, param, new HttpCallback() {
			@Override
			public void onSuccess(String data) {
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONObject result = (JSONObject) jsonObject.get("result");
					String code = result.getString("code");
					if ("200".equals(code)) {
						mUserId = result.getString("uid");
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_SUCCESS, mUserId);
						LogUtils.d("login success mUserID= "+mUserId);
					} else {
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
				}
			}
			
			@Override
			public void onFailed(String code) {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败" + code);
			}
		});
	}

}
