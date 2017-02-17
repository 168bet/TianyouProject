package com.tianyou.channel.business;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;

import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.sdk.interfaces.Tianyouxi;
import com.tianyou.sdk.utils.AppUtils;

public class TianyouSdkService extends BaseSdkService {
    
    @Override
    public void doApplicationCreate(Context context, boolean island) {
    	String gameId = "1021";
		String gameToken = "0768281a05da9f27df178b5c39a51263";
		/**
		 * gameId：app唯一标识，非常重要，请认真填写，确保正确
		 * gameToken：appkey
		 * isLandscape：游戏横屏为true，竖屏为false
		 */
		Tianyouxi.init(context, gameId, gameToken, false);
    }
    
    @Override
    public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
    	super.doActivityInit(activity, tianyouCallback);
    	Tianyouxi.createFloatMenu(mActivity);
    	mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "");
    }
    
    @Override
    public void doLogin() {
    	super.doLogin();
    	Tianyouxi.login(mActivity, "龙神捕鱼", new com.tianyou.sdk.interfaces.TianyouCallback.LoginCallback() {
			@Override
			public void onSuccess(String userId, String userToken) {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_SUCCESS, userId);
			}
			
			@Override
			public void onFailed(String resultMsg) {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, resultMsg);
			}
		});
    }
    
    @Override
    public void doEntryGame() {
    	super.doEntryGame();
    	try {
			JSONObject roleInfo = new JSONObject();
			roleInfo.put("roleId", "1000");
			roleInfo.put("roleLevel", "100");
			roleInfo.put("serverId", "1000");
			roleInfo.put("serverName", "sName");
			roleInfo.put("vipLevel", "100");
			Tianyouxi.enterGame(mActivity, roleInfo.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public void doCreateRole(RoleInfo roleInfo) {
    	super.doCreateRole(roleInfo);
    	try {
			JSONObject roleInfos = new JSONObject();
			roleInfos.put("roleId", "1000");
			roleInfos.put("roleName", "tom");
			roleInfos.put("serverId", "1000");
			roleInfos.put("serverName", "sName");
			roleInfos.put("profession", "剑圣");
			Tianyouxi.createRole(mActivity, roleInfos.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
    	super.doChannelPay(payInfo, orderInfo);
    	Tianyouxi.pay(mActivity, getPayParam(), 15, "超值大礼包", new com.tianyou.sdk.interfaces.TianyouCallback() {
    		@Override
    		public void onSuccess(String resultMsg) {
    			mTianyouCallback.onResult(TianyouCallback.CODE_PAY_SUCCESS, resultMsg);
    		}

    		@Override
    		public void onFailed(String resultMsg) {
    			mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, resultMsg);
    		}
    	});
    }
    
    private String getPayParam() {
		try {
			JSONObject payInfo = new JSONObject();
			String roleId = "13141654";
			String serverId = "10281";
			payInfo.put("roleId", roleId);
			payInfo.put("serverId", serverId);
			payInfo.put("serverName", "国内Android测试服");
			payInfo.put("customInfo", "21689575c5284a334ca8f6630127915f9058");
			payInfo.put("productId", "scom.tianyouxi.skszj.p1");
			payInfo.put("productName", "60金钻");
			payInfo.put("gameName", "寻龙剑");
			payInfo.put("sign", AppUtils.MD5(roleId + serverId));
			payInfo.put("signType", "MD5");
			return payInfo.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
