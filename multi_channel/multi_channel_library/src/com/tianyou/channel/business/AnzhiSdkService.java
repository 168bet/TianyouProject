package com.tianyou.channel.business;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.anzhi.sdk.middle.manage.AnzhiSDK;
import com.anzhi.sdk.middle.manage.AnzhiSDKExceptionHandler;
import com.anzhi.sdk.middle.manage.GameCallBack;
import com.anzhi.sdk.middle.util.MD5;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.AppUtils;
import com.tianyou.channel.utils.Des3Util;
import com.tianyou.channel.utils.LogUtils;

public class AnzhiSdkService extends BaseSdkService{
	
	private AnzhiSDK anzhiSDK;
	private String appKey;
	private static String appSecret;
	private AnzhiGameCallBack mAnzhiCallBack;
	private String orderID;
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		try {
			
			Object localObject;
			if ((localObject = (localObject = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 128).applicationInfo.metaData)) != null) {
				boolean bool;
				if ((bool = Boolean.valueOf(((Bundle) localObject).getBoolean("ANZHI_DEBUG")).booleanValue())) {
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Thread.setDefaultUncaughtExceptionHandler(new AnzhiSDKExceptionHandler(context));
		}
		
	}
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		mAnzhiCallBack = new AnzhiGameCallBack();
		appKey = mChannelInfo.getAppKey();
		appSecret = mChannelInfo.getAppSecret();
		anzhiSDK = AnzhiSDK.getInstance();
		anzhiSDK.init(mActivity, appKey, appSecret, mAnzhiCallBack);
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "");
	}
	
	@Override
	public void doLogin() {
		anzhiSDK.login(mActivity);
	}
	
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		String gameInfo = "{gameAreaID:"+mRoleInfo.getServerId()+",gameArea:"+mRoleInfo.getServerName()+
				",gameLevel:"+mRoleInfo.getRoleLevel()+",roleId:"+mRoleInfo.getRoleId()
				+",userRole:"+mRoleInfo.getRoleName()+"}";
		anzhiSDK.subGameInfo(gameInfo);
	}
	
	@Override
	public void doLogout() {
		anzhiSDK.logout();
	}
	
	
	
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		orderID = orderInfo.getOrderID();
        try {
        	JSONObject json = new JSONObject();
            json.put("cpOrderId", orderID); // 游戏方生成的订单号,可以作为与安智订单进行关联
            json.put("cpOrderTime", orderInfo.getCreate_time()); // 下单时间
            json.put("amount", Integer.parseInt(orderInfo.getMoNey())); // 支付金额(单位：分)
            json.put("cpCustomInfo", payInfo.getCustomInfo()); // 游戏方自定义数据(非json格式)，支付回调数据中会返回该数据
            json.put("productName",mPayInfo.getProductName()); // 游戏方商品名称
            json.put("productCode", mPayInfo.getProductId()); // 游戏方商品代码
            anzhiSDK.pay(Des3Util.encrypt(json.toString(), appSecret), MD5.encodeToString(appSecret));
        } catch (JSONException e) {
            e.printStackTrace();
        }
		
	}
	
	@Override
	public void doExitGame() {
		anzhiSDK.exitGame(mActivity);
	}
	
	private class AnzhiGameCallBack implements GameCallBack{
		@Override
		public void callBack(int type, String data) {
			LogUtils.d("type:" + type);
			switch (type) {
			case AnzhiGameCallBack.SDK_TYPE_INIT:
				anzhiSDK.addPop(mActivity);
				break;
				case AnzhiGameCallBack.SDK_TYPE_LOGIN:
					Log.d("sdk", "data=" + data);
					try {
						JSONObject loginObject = new JSONObject(data);
						String sid = loginObject.getString("uid");
						String session = loginObject.getString("sid");
						LoginInfo loginParam = new LoginInfo();
						loginParam.setChannelUserId(sid);
						loginParam.setUserToken(session);
						checkLogin(loginParam);
					} catch (JSONException e) {
						e.printStackTrace();
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "");
					}
					break;
				case AnzhiGameCallBack.SDK_TYPE_LOGOUT:
					LogUtils.d("用户注销成功");
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "");
					break;
				case AnzhiGameCallBack.SDK_TYPE_PAY:
					checkOrder(orderID);
					break;
				case AnzhiGameCallBack.SDK_TYPE_CANCEL_PAY:
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "");
					break;
				case AnzhiGameCallBack.SDK_TYPE_CANCEL_LOGIN:
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "");
					break;
				case AnzhiGameCallBack.SDK_TYPE_EXIT_GAME:
					mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "");
					break;
			}
		}
	}
		
	@Override
	public void doNewIntent(Intent intent) {
		anzhiSDK.onNewIntentInvoked(intent);
	}
	
	@Override
	public void doResume() {
		anzhiSDK.onResumeInvoked();
	}
	
	@Override
	public void doStart() {
		anzhiSDK.onStartInvoked();
	}
	
	@Override
	public void doStop() {
		anzhiSDK.onStopInvoked();
	}
	
	@Override
	public void doPause() {
		anzhiSDK.onPauseInvoked();
	}
	
	@Override
	public void doDestory() {
		anzhiSDK.onDestoryInvoked();
	}

}
