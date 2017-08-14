package com.tianyou.channel.business;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.pengyouwan.sdk.api.ISDKEventCode;
import com.pengyouwan.sdk.api.ISDKEventExtraKey;
import com.pengyouwan.sdk.api.OnSDKEventListener;
import com.pengyouwan.sdk.api.PYWPlatform;
import com.pengyouwan.sdk.api.PayConstant;
import com.pengyouwan.sdk.api.SDKConfig;
import com.pengyouwan.sdk.api.User;
import com.pengyouwan.sdk.utils.FloatViewTool;
import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ConfigHolder;

public class PYWSdkService extends BaseSdkService {
	
	
	private static TianyouCallback mLoginCallback;
	private static TianyouCallback mPayCallback;
	private static TianyouCallback mExitCallback;
	private String orderID;
	
	// 在Application里进行初始化
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(context);
		
		// sdk参数配置
		SDKConfig config = new SDKConfig();
		config.setGameKey(channelInfo.getAppId());
		// 初始化sdk
		PYWPlatform.initSDK(context,config, new SDKEventListener());
	}

	// 登录
	@Override
	public void doLogin(TianyouCallback callback) {
		mLoginCallback = callback;
		PYWPlatform.openLogin(mActivity);
	}
	
	@Override
	public void doChannelPay(String orderId, String price,JSONObject result,TianyouCallback callback) {
		orderID = orderId;
		mPayCallback = callback;
		
		String waresid = "";
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(PayConstant.PAY_PRODUCE_NAME, mPayInfo.getProductName());
		paramMap.put(PayConstant.PAY_MONEY, Integer.parseInt(price));
		paramMap.put(PayConstant.PAY_ORDER_ID,orderId);
		
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.accumulate("custominfo", mRoleInfo.getCustomInfo());
			waresid = result.getJSONObject("orderinfo").getString("waresid");
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramMap.put(PayConstant.PAY_PRODUCT_ID,waresid);
		paramMap.put(PayConstant.PAY_EXTRA, jsonObject);
		
		PYWPlatform.openChargeCenter(mActivity, paramMap, false);
		
	}
	
	@Override
	public void doExitGame(TianyouCallback callback) {
		mExitCallback = callback;
		PYWPlatform.exit(mActivity);
	}
	
	
	//自定义类实现OnSDKEventListener,统一处理各个事件的回调
	private class SDKEventListener implements OnSDKEventListener {

		@Override
		public void onEvent(int eventCode, Bundle data) {
			switch (eventCode) {
			
			case ISDKEventCode.CODE_LOGIN_SUCCESS:
				doLoginSuccess(data);
				break;
				
			case ISDKEventCode.CODE_LOGIN_FAILD:
				mLoginCallback.onFailed("登录失败");
				break;
				
			case ISDKEventCode.CODE_LOGOUT:
				Log.d("TAG", "logout----------------");
				mLogoutCallback.onSuccess("注销账号成功");
				break;
				
			case ISDKEventCode.CODE_CHARGE_SUCCESS:
				Log.d("TAG", "orderID= "+orderID);
				checkOrder(orderID, mPayCallback);
				break;
				
			case ISDKEventCode.CODE_CHARGE_FAIL:
				mPayCallback.onFailed("支付失败");
				break;
				
			case ISDKEventCode.CODE_CHARGE_CANCEL:
				mPayCallback.onFailed("支付取消");
				break;
				
			case ISDKEventCode.CODE_EXIT:
				mExitCallback.onSuccess("退出游戏");
				break;
				
			}
		}
	}
	
	// 处理登录结果
	private void doLoginSuccess(Bundle data){
		if (data != null){
			User userInfo = (User) data.getSerializable(ISDKEventExtraKey.EXTRA_USER);
			if (userInfo != null) {
				String sid = userInfo.getUserId();
				String token = userInfo.getToken();
				FloatViewTool.instance(mActivity).showFloatView();
				Log.d("TAG", "pengyouwan login sid= "+sid+",token= "+token);
				checkLogin(sid, token, mLoginCallback);
			} else {
				mLoginCallback.onFailed("登录失败");
			}
		} else {
			mLoginCallback.onFailed("登录失败");
		}
	}
}
