package com.tianyou.channel.business;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;

import com.game.sdk.YTSDKManager;
import com.game.sdk.domain.LoginErrorMsg;
import com.game.sdk.domain.LogincallBack;
import com.game.sdk.domain.OnLoginListener;
import com.game.sdk.domain.OnPaymentListener;
import com.game.sdk.domain.PaymentCallbackInfo;
import com.game.sdk.domain.PaymentErrorMsg;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.URLHolder;

public class HSZSdkService extends BaseSdkService {

	private YTSDKManager sdkManager;
	private String user_id;
	private String user_token;
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		sdkManager = YTSDKManager.getInstance(mActivity);
		sdkManager.setIsPortrait(0);
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "");
	}
	
	@Override
	public void doLogin() {
		sdkManager.showLogin(mActivity, true, new OnLoginListener() {
			@Override
			public void loginSuccess(LogincallBack logincallback) {
				user_id = logincallback.mem_id;
				user_token = logincallback.user_token;
				checkLogin(user_id, user_token);
			}

			@Override
			public void loginError(LoginErrorMsg errorMsg) {
				int code = errorMsg.code;// 登录失败的状态码
				String msg = errorMsg.msg;// 登录失败的消息提示
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, code + msg);
			}
		});
	}
	
	@Override
	public void doChannelPay(final OrderinfoBean orderInfo) {
		sdkManager.showPay(mActivity, mRoleInfo.getRoleName(), orderInfo.getMoNey(), mRoleInfo.getServerId(), 
				mPayInfo.getProductName(), mPayInfo.getProductDesc(), "", orderInfo.getOrderID(), new OnPaymentListener() {
			@Override
			public void paymentSuccess(PaymentCallbackInfo callbackInfo) {
				checkOrder(orderInfo.getOrderID());	
			}

			@Override
			public void paymentError(PaymentErrorMsg errorMsg) {
				int code = errorMsg.code;
				String msg = errorMsg.msg;
				LogUtils.d("code=" + code + ",msg=" + msg);
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "code=" + code + ",msg=" + msg);
			}
		});
	}
	
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		Map<String, String> map = new HashMap<String, String>();
		map.put("server_id", roleInfo.getServerId());
		map.put("server", roleInfo.getServerName());
		map.put("role", roleInfo.getRoleName());
		map.put("level", roleInfo.getRoleLevel());
		map.put("uid", user_id);
		map.put("user_token", user_token);
		HttpUtils.post(mActivity, URLHolder.UPLOAD_ROLE_INFO, map, new HttpCallback() {
			@Override
			public void onSuccess(String data) { }
			
			@Override
			public void onFailed(String code) { }
		});
	}
	
	@Override
	public void doResume() {
		sdkManager.showFloatView();
	}
	
	@Override
	public void doStop() {
		sdkManager.removeFloatView();
	}
	
	@Override
	public void doDestory() {
		sdkManager.recycle();
	}
	
}
