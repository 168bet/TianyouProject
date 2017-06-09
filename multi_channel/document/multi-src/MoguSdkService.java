package com.tianyou.channel.business;

import android.app.Activity;
import android.content.Context;

import com.game.sdk.YTSDKManager;
import com.game.sdk.domain.LoginErrorMsg;
import com.game.sdk.domain.LogincallBack;
import com.game.sdk.domain.OnLoginListener;
import com.game.sdk.domain.OnPaymentListener;
import com.game.sdk.domain.PaymentCallbackInfo;
import com.game.sdk.domain.PaymentErrorMsg;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;

public class MoguSdkService extends BaseSdkService{
	
	private YTSDKManager mSDKManager;
	private boolean isLand;
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		isLand = island;
	}
	
	
	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		mSDKManager = YTSDKManager.getInstance(mActivity);
		if (isLand) {
			mSDKManager.setIsPortrait(0);
		} else {
			mSDKManager.setIsPortrait(1);
		}
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
	}
	
	@Override
	public void doLogin() {
//		super.doLogin();
		mSDKManager.showLogin(mActivity, true, new OnLoginListener() {
			
			@Override
			public void loginSuccess(LogincallBack logincallBack) {
				LogUtils.d("login success: "+logincallBack.toString());
				String uid = logincallBack.mem_id;
				String token = logincallBack.user_token;
				checkLogin(uid, token);
				mSDKManager.showFloatView();
			} 
			
			@Override
			public void loginError(LoginErrorMsg loginErrorMsg) {
				LogUtils.d("login error: "+loginErrorMsg.toString());
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
			}
		});
	}
	
	@Override
	public void doChannelPay(final OrderinfoBean orderInfo) {
		super.doChannelPay(orderInfo);
		
		mSDKManager.showPay(mActivity, mRoleInfo.getRoleId(), orderInfo.getMoNey(), mRoleInfo.getServerId(), 
				mPayInfo.getProductName(), mPayInfo.getProductDesc(), orderInfo.getNotifyurl(), 
				orderInfo.getOrderID(), new OnPaymentListener() {
					
					@Override
					public void paymentSuccess(PaymentCallbackInfo callbackInfo) {
						LogUtils.d("pay success: "+callbackInfo.toString());
						checkOrder(orderInfo.getOrderID());
					}
					
					@Override
					public void paymentError(PaymentErrorMsg errorMsg) {
						LogUtils.d("pay error: "+errorMsg.toString());
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
					}
				});
	}
	
	@Override
	public void doExitGame() {
		mSDKManager.recycle();
		super.doExitGame();
	}
	
	@Override
	public void doStop() {
		mSDKManager.removeFloatView();
	}
	
	@Override
	public void doResume() {
		mSDKManager.showFloatView();
	}
	
	@Override
	public void doDestory() {
		mSDKManager.recycle();
	}

}
