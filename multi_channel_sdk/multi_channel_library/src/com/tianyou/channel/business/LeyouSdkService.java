package com.tianyou.channel.business;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

import com.moge.sdk.MGSDKManager;
import com.moge.sdk.listener.LoginResult;
import com.moge.sdk.listener.OnExitListener;
import com.moge.sdk.listener.OnLoginListener;
import com.moge.sdk.listener.OnPaymentListener;
import com.moge.sdk.listener.OnUserLogoutListener;
import com.moge.sdk.listener.PaymentResult;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.PayInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.LogUtils;

public class LeyouSdkService extends BaseSdkService{
	
	private MGSDKManager mSDKManager;
	
	@Override
	public void doActivityInit(Activity activity,TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		mSDKManager = MGSDKManager.getInstance(mActivity);
		mSDKManager.init(mActivity);
		mSDKManager.onCreate();
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
		mSDKManager.setUserLogoutListener(new OnUserLogoutListener() {
			
			@Override
			public void onLogoutSuccess(int code, String msg) { mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, msg);}
			
			@Override
			public void onLogoutFailure(int code, String msg) {LogUtils.d("logout failed code= "+code+",msg= "+msg);}
		});
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		mSDKManager.getInstance(mActivity).login(mActivity, true, new OnLoginListener() {
			
			@Override
			public void onLoginSuccess(LoginResult loginResult) {
				LogUtils.d("loginResult: username= "+loginResult.username+",memkey= "+loginResult.memkey+",sign= "+loginResult.sign+",loginTime= "+loginResult.logintime);
				LoginInfo loginParam = new LoginInfo();
				loginParam.setChannelUserId(loginResult.username);
				loginParam.setUserToken(loginResult.memkey);
				checkLogin(loginParam);
			}
			
			@Override
			public void onLoginFailure(int code, String msg) {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
			}
		});
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		PayInfo productInfo = ConfigHolder.getPayInfo(mActivity, payInfo.getPayCode());
		mSDKManager.pay(mActivity, mRoleInfo.getRoleId(), mRoleInfo.getRoleName(), mRoleInfo.getRoleLevel(), 
				orderInfo.getMoNey(), orderInfo.getServerID(), productInfo.getProductName(), productInfo.getProductDesc(), orderInfo.getOrderID(),
				new OnPaymentListener() {
					
					@Override
					public void onPaySuccess(PaymentResult paymentResult) {
						checkOrder(orderInfo.getOrderID());
					}
					
					@Override
					public void onPayFailure(int code, String msg, String money) {
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
					}
				});
	}
	
	@Override
	public void doCreateRole(RoleInfo roleInfo) {
		super.doCreateRole(roleInfo);
		com.moge.sdk.bean.RoleInfo mogeRoleInfo = getMogeRoleInfo(true, mRoleInfo);
		mSDKManager.updateGameRoleData(mActivity, mogeRoleInfo);
	}
	
	@Override
	public void doEntryGame() {
		super.doEntryGame();
		com.moge.sdk.bean.RoleInfo mogeRoleInfo = getMogeRoleInfo(false, mRoleInfo);
		mSDKManager.updateGameRoleData(mActivity, mogeRoleInfo);
	}
	
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUpdateRoleInfo(roleInfo);
		com.moge.sdk.bean.RoleInfo mogeRoleInfo = getMogeRoleInfo(false, mRoleInfo);
		mSDKManager.updateGameRoleData(mActivity, mogeRoleInfo);
	}
	
	private com.moge.sdk.bean.RoleInfo getMogeRoleInfo (boolean isCreateRole,RoleInfo roleInfo){
		com.moge.sdk.bean.RoleInfo mogeRoleInfo = new com.moge.sdk.bean.RoleInfo();
		mogeRoleInfo.setCreateRole(isCreateRole);
		mogeRoleInfo.setPartyName(roleInfo.getParty());
		mogeRoleInfo.setRoleBalance(roleInfo.getBalance());
		mogeRoleInfo.setRoleCreateTime(roleInfo.getCreateTime());
		mogeRoleInfo.setRoleID(roleInfo.getRoleId());
		mogeRoleInfo.setRoleLevel(roleInfo.getRoleLevel());
		mogeRoleInfo.setRoleName(roleInfo.getRoleName());
		mogeRoleInfo.setServerID(roleInfo.getServerId());
		mogeRoleInfo.setServerName(roleInfo.getServerName());
		mogeRoleInfo.setVipLevel(roleInfo.getVipLevel());
		return mogeRoleInfo;
	}
	
	@Override
	public void doLogout() {
		super.doLogout();
		mSDKManager.logout();
	}
	
	@Override
	public void doExitGame() {
		super.doExitGame();
		mSDKManager.exit(mActivity, new OnExitListener() {
			
			@Override
			public void onSuccess() { mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS,"退出游戏");}
			
			@Override
			public void onFailure() { mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "");}
		});
	}
	
	@Override
	public void doConfigurationChanged(Configuration newConfig) { mSDKManager.onConfigurationChanged(newConfig);}
	
	@Override
	public void doNewIntent(Intent intent) { mSDKManager.onNewIntent(intent);}
	
	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) { mSDKManager.onActivityResult(requestCode, resultCode, data);}
	
	@Override
	public void doPause() { mSDKManager.onPause();}
	
	@Override
	public void doDestory() { mSDKManager.onDestroy();}
	
	@Override
	public void doStop() { mSDKManager.onStop();}
	
	@Override
	public void doStart() { mSDKManager.onStart();}
	
	@Override
	public void doResume() { mSDKManager.onResume();}
	
	@Override
	public void doRestart() { mSDKManager.onRestart();}
	
	@Override
	public void doBackPressed() { mSDKManager.onBackPressed();}

}
