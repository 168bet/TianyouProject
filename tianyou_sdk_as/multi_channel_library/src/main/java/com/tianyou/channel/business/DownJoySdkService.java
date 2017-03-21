package com.tianyou.channel.business;

import android.app.Activity;
import android.util.Log;

import com.downjoy.CallbackListener;
import com.downjoy.CallbackStatus;
import com.downjoy.Downjoy;
import com.downjoy.InitListener;
import com.downjoy.LoginInfo;
import com.downjoy.LogoutListener;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;

public class DownJoySdkService extends BaseSdkService{

	private String merchantId; // 当乐分配的MERCHANT_ID
	private String appId; // 当乐分配的APP_ID
	private String appKey; // 当乐分配的 APP_KEY
	private String serverSeqNum = "1";
	private Downjoy downjoy;
	private static String sid;
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		appId = mChannelInfo.getAppId();
		appKey = mChannelInfo.getAppKey();
		merchantId = mChannelInfo.getMerchantId();
		downjoy = Downjoy.getInstance(mActivity, merchantId, appId,serverSeqNum, appKey, new InitListener() {
            @Override
            public void onInitComplete() {
            	downjoy.showDownjoyIconAfterLogined(true);
            	downjoy.setInitLocation(Downjoy.LOCATION_LEFT_CENTER_VERTICAL);
            }
		});
		downjoy.setLogoutListener(mLogoutListener);
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化成功");
	}
	
	
	private LogoutListener mLogoutListener = new LogoutListener() {
        @Override
        public void onLogoutSuccess() {
//        	mLogoutCallback.onSuccess("注销成功");
        	mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "注销成功");
        	Log.d("TAG", "dangle logout success--------");
        }

        @Override
        public void onLogoutError(String msg) {
//        	mLogoutCallback.onFailed("注销失败");
        	LogUtils.e("注销失败");
        }
    };
	
	@Override
	public void doLogin() {
		downjoy.openLoginDialog(mActivity, new CallbackListener<LoginInfo>() {
			@Override
			public void callback(int status, LoginInfo data) {
				switch (status) {
					case CallbackStatus.SUCCESS:
						sid = data.getUmid();
						String session = data.getToken();
						checkLogin(sid, session);
						break;
	
					case CallbackStatus.FAIL:
						// 登录失败
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
						break;
						
					case CallbackStatus.CANCEL:
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL,"用户取消登录");
						break;
				}
			}
		});
	}
	
	@Override
	public void doChannelPay(OrderinfoBean orderInfo) {
		String price = orderInfo.getMoNey();
		final String orderID = orderInfo.getOrderID();
		String productName = mPayInfo.getProductName();
		String productDesc = mPayInfo.getProductDesc();
		String serverName = mRoleInfo.getServerName();
		String playerName = mRoleInfo.getRoleName();
		downjoy.openPaymentDialog(mActivity, Float.parseFloat(price), productName, productDesc, orderID, serverName, playerName, new CallbackListener<String>() {
			@Override
			public void callback(int status, String data) {
				switch (status) {
					case CallbackStatus.SUCCESS:
						checkOrder(orderID);
						break;

					case CallbackStatus.CANCEL:
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "用户取消支付");
						break;

					case CallbackStatus.FAIL:
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
						break;
				}
			}
		});
	}
	
	@Override
	public void doExitGame() {
		downjoy.openExitDialog(mActivity, new CallbackListener<String>() {
			@Override
			public void callback(int status, String data) {
				if (status == CallbackStatus.SUCCESS){
					mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "用户退出");
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "用户取消退出");
				}
			}
		});
	}
	
	@Override
	public void doResume() {
		if (downjoy != null) {
	        downjoy.resume(mActivity);
	    }
	}
	
	@Override
	public void doPause() {
		if (downjoy != null) {
	        downjoy.pause();
	    }
	}
	
	@Override
	public void doDestory() {
		if (downjoy != null) {
	        downjoy.destroy();
	        downjoy = null;
	    }
	}
}
