package com.tianyou.channel.business;

import android.app.Activity;
import android.content.Context;

import com.le.accountoauth.utils.LeUserInfo;
import com.le.legamesdk.LeGameSDK;
import com.le.legamesdk.LeGameSDK.ActionCallBack;
import com.le.legamesdk.LeGameSDK.ExitCallback;
import com.le.legamesdk.LeGameSDK.PayCallback;
import com.letv.lepaysdk.smart.LePayInfo;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.PayInfo;

import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.LogUtils;

public class LeshiSpecialSdkService extends BaseSdkService{
	
	private LeGameSDK mLeGameSDK;
	private String leUserId;
	private String leToken;
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		LeGameSDK.init(context);
		
	}
	
	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		mLeGameSDK = LeGameSDK.getInstance();
		mLeGameSDK.onCreate(mActivity, new ActionCallBack() {
			@Override
			public void onExitApplication() { mActivity.finish();}
		});
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		mLeGameSDK.doLogin(mActivity, mLoginCallback, false);
	}
	
	@Override
	public void doLogout() {
		super.doLogout();
		mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "");
		mLeGameSDK.doLogin(mActivity, mLoginCallback, true);
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		PayInfo productInfo = ConfigHolder.getPayInfo(mActivity, payInfo.getPayCode());
		final LePayInfo lePayInfo = new LePayInfo();
		lePayInfo.setLetv_user_access_token(leToken);
		lePayInfo.setLetv_user_id(leUserId);
		lePayInfo.setNotify_url(orderInfo.getNotifyurl());
		lePayInfo.setCooperator_order_no(orderInfo.getOrderID());
		lePayInfo.setPrice(orderInfo.getMoNey());
		lePayInfo.setProduct_name(productInfo.getProductName());
		lePayInfo.setProduct_desc(productInfo.getProductDesc());
		lePayInfo.setPay_expire(orderInfo.getDeadline());
		lePayInfo.setProduct_id(productInfo.getProductId());
		lePayInfo.setCurrency("RMB");
		lePayInfo.setExtro_info(payInfo.getCustomInfo());
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mLeGameSDK.doPay(mActivity, lePayInfo, new PayCallback() {
					
					@Override
					public void onPayResult(String status, String msg) {
						if (status.equals("SUCCESS") || status.equals("PAYED")) {
							checkOrder(orderInfo.getOrderID());
						} else if (status.equals("CANCEL")) {
							mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "");
						} else {
							mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "");
						}
					}
				});
			}
		});
	}
	
	@Override
	public void doExitGame() {
		super.doExitGame();
		mLeGameSDK.onExit(mActivity, new ExitCallback() {
			
			@Override
			public void onSdkExitConfirmed() { mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "退出游戏");}
			
			@Override
			public void onSdkExitCanceled() { mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "退出取消");}
		});
	}
	
	@Override
	public void doResume() { mLeGameSDK.onResume(mActivity);}
	
	@Override
	public void doPause() { mLeGameSDK.onPause(mActivity);}
	
	@Override
	public void doDestory() { mLeGameSDK.onDestory(mActivity);}
	
	private LeGameSDK.LoginCallback mLoginCallback = new LeGameSDK.LoginCallback() {
		
		@Override
		public void onLoginSuccess(LeUserInfo leUserInfo) {
			if (leUserInfo != null) {
				leToken = leUserInfo.getAccessToken();
				leUserId = leUserInfo.getUserId();
				LoginInfo loginParam = new LoginInfo();
				loginParam.setChannelUserId(leUserId);
				loginParam.setUserToken(leToken);
				LogUtils.d("login success userId= "+leUserId+",accessToken= "+leToken+",userName= "+leUserInfo.getUserName());
				checkLogin(loginParam);
			} else {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
			}
		}
		
		@Override
		public void onLoginFailure(int code, String msg) {
			mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
		}
		
		@Override
		public void onLoginCancel() {
			mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "取消登录");
		}
	};
	
}
