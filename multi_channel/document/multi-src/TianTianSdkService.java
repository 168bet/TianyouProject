package com.tianyou.channel.business;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.ltsdkgame.sdk.SDKManager;
import com.ltsdkgame.sdk.SDKState;
import com.ltsdkgame.sdk.listener.LTCallback;
import com.ltsdkgame.sdk.model.GameUserInfo;
import com.ltsdkgame.sdk.model.PayInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.ToastUtils;

public class TianTianSdkService extends BaseSdkService{
	
	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		SDKManager.iniSDK(mActivity,false, new LTCallback() {
			
			@Override
			public void callback(int code, String msg) {
				switch (code) {
				
				case SDKState.RESULT_CODE_LOGOUT: mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "用户注销登录"); break;
					
				case SDKState.RESULT_CODE_INITIALIZED: mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成"); break;
				
				}
			}
		});
	}
	
	@Override
	public void doLogin() {
		SDKManager.userLogin(mActivity, new LTCallback() {
			
			@Override
			public void callback(int code, String msg) {
				switch (code) {
				case SDKState.SUCCESS: 
					LogUtils.d("msg= "+msg);
					try {
						JSONObject jsonObject = new JSONObject(msg);
						String id = jsonObject.getString("id");
						checkLogin(id, id);
					} catch (JSONException e) {
						e.printStackTrace();
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
					}
				break;

				case SDKState.ERROR: 
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");	
					ToastUtils.show(mActivity, msg);
				break;
				}
			}
		});
	}
	
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		GameUserInfo userInfo = new GameUserInfo();
		userInfo.setRoleName(mRoleInfo.getRoleName());
		userInfo.setZoneName(mRoleInfo.getServerName());
		SDKManager.submitZoneInfo(userInfo, new LTCallback() {
			
			@Override
			public void callback(int code, String msg) {
				LogUtils.d("submit zone info success");
			}
		});
	}
	
	@Override
	public void doChannelPay(OrderinfoBean orderInfo) {
		super.doChannelPay(orderInfo);
		final String orderID = orderInfo.getOrderID();
		PayInfo payInfo = new PayInfo();
		payInfo.setCpTradeNo(orderID);
		payInfo.setZoneName(mRoleInfo.getServerName());
		payInfo.setRoleName(mRoleInfo.getRoleName());
		payInfo.setPrice(Double.parseDouble(orderInfo.getMoNey()));
		payInfo.setWaresname(mPayInfo.getProductName());
		payInfo.setCpprivateinfo(mRoleInfo.getCustomInfo());
		payInfo.setNotifyUrl(orderInfo.getNotifyurl());
		SDKManager.startPay(mActivity, payInfo, new LTCallback() {
			
			@Override
			public void callback(int code, String msg) {
				switch (code) {
				case SDKState.PAY_CANCEL: mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "支付取消"); break;

				case SDKState.PAY_SUCCESS: checkOrder(orderID); break;
				
				default: mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败"); break;
				}
			}
		});
	}
	
	@Override
	public void doLogout() {
//		super.doLogout();
		SDKManager.logout(new LTCallback() {
			
			@Override
			public void callback(int code, String msg) {
				switch (code) {
				case SDKState.SUCCESS: mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "用户注销登录"); break;
				}
			}
		});
	}
	
	@Override
	public boolean isShowLogout() {
		return false;
	}
	
}
