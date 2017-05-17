package com.tianyou.channel.business;

import org.json.JSONException;
import org.json.JSONObject;

import com.game.dqsdk.DQSDKManager;
import com.game.dqsdk.domain.LoginErrorMsg;
import com.game.dqsdk.domain.LogincallBack;
import com.game.dqsdk.domain.OnLoginListener;
import com.game.dqsdk.domain.OnPaymentListener;
import com.game.dqsdk.domain.PaymentCallbackInfo;
import com.game.dqsdk.domain.PaymentErrorMsg;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;

import android.app.Activity;
import android.widget.Toast;

public class DuoquSdkService extends BaseSdkService {

	public DQSDKManager sdkmanager;

	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		sdkmanager = DQSDKManager.getInstance(mActivity);
		sdkmanager.setIsPortrait(2);
	}

	@Override
	public void doLogin() {
		super.doLogin();
		sdkmanager.showLogin(mActivity, true, new OnLoginListener() {
			@Override
			public void loginSuccess(LogincallBack logincallback) {
				sdkmanager.showFloatView();// 显示浮点
				try {
					LoginInfo loginInfo = new LoginInfo();
					loginInfo.setChannelUserId(logincallback.username);
					loginInfo.setUserToken(logincallback.token);
					checkLogin(loginInfo);
				} catch (Exception e) {
					// 处理异常
				}
			}

			@Override
			public void loginError(LoginErrorMsg errorMsg) {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, errorMsg.msg);
			}
		});
	}
	
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		JSONObject jsonExData = new JSONObject();
		try {
			jsonExData.put("service", roleInfo.getServerName());
			jsonExData.put("role", roleInfo.getRoleName());
			jsonExData.put("grade", roleInfo.getRoleLevel());
			DQSDKManager.getInstance(mActivity).submitExtendData(1, jsonExData);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		sdkmanager.showPay(mActivity, mRoleInfo.getRoleId(), mPayInfo.getMoney(), mRoleInfo.getServerId(), 
				mPayInfo.getProductName(), mPayInfo.getProductDesc(), payInfo.getCustomInfo(), new OnPaymentListener() {
			@Override
			public void paymentSuccess(PaymentCallbackInfo callbackInfo) {
				checkOrder(orderInfo.getOrderID());
			}

			@Override
			public void paymentError(PaymentErrorMsg errorMsg) {
				Toast.makeText(mActivity, "充值失败：code:" + errorMsg.code + "  ErrorMsg:"
						+ errorMsg.msg + "  预充值的金额：" + errorMsg.money, Toast.LENGTH_LONG).show();
			}
		});
	}
	
	@Override
	public void doStop() {
		super.doStop();
		sdkmanager.removeFloatView();
	}
	
	@Override
	public void doResume() {
		super.doResume();
		sdkmanager.showFloatView();
	}
	
	@Override
	public void doExitGame() {
		sdkmanager.recycle();
	}
}
