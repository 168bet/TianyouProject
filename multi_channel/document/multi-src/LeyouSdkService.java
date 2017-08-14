package com.tianyou.channel.business;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

import com.moge.sdk.MGSDKManager;
import com.moge.sdk.listener.LoginResult;
import com.moge.sdk.listener.OnExitListener;
import com.moge.sdk.listener.OnLoginListener;
import com.moge.sdk.listener.OnPaymentListener;
import com.moge.sdk.listener.OnSwitchAccountListener;
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
	
	private MGSDKManager mSDKManager;		// 声明乐游SDK的全局变量
	private String tyOrderID;	// 天游订单号
	
	/**
	 * Activity初始化接口,onCreate生命周期方法
	 */
	@Override
	public void doActivityInit(Activity activity,TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		mSDKManager = MGSDKManager.getInstance(mActivity);		// 获取乐游SDK实例
		
		mSDKManager.init(mActivity);							// 乐游SDK初始化方法
		
		mSDKManager.onCreate();									// 乐游SDK的onCreate生命周期方法
		
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");		// SDK初始化完成通知游戏
		
		mSDKManager .setUserLogoutListener(mOnUserLogoutListener);		// 设置注销登录事件的监听
		
		mSDKManager.setSwitchAccountListener(mOnSwitchAccountListener);		// 设置切换账号的监听,仅监听SDK浮标内的切换账号操作
	}
	
	/**
	 * 登录接口
	 */
	@Override
	public void doLogin() {
		super.doLogin();
		mSDKManager.getInstance(mActivity).login(mActivity, true, mOnLoginListener);	// 乐游SDK登录接口
	}
	
	/**
	 * 支付接口
	 */
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		PayInfo productInfo = ConfigHolder.getPayInfo(mActivity, payInfo.getPayCode());
		tyOrderID = orderInfo.getOrderID();		// 获取天游订单号
		mSDKManager.pay(mActivity, mRoleInfo.getRoleId(), mRoleInfo.getRoleName(), mRoleInfo.getRoleLevel(), 
				orderInfo.getMoNey(), orderInfo.getServerID(), productInfo.getProductName(), productInfo.getProductDesc(), orderInfo.getOrderID(),
				mOnPaymentListener);
	}
	
	/**
	 * 创建角色接口
	 */
	@Override
	public void doCreateRole(RoleInfo roleInfo) {
		super.doCreateRole(roleInfo);
		com.moge.sdk.bean.RoleInfo mogeRoleInfo = getMogeRoleInfo(true, mRoleInfo);		//获取乐游RoleInfo对象
		mSDKManager.updateGameRoleData(mActivity, mogeRoleInfo);	// 上传角色信息
	}
	
	/**
	 * 进入游戏接口
	 */
	@Override
	public void doEntryGame() {
		super.doEntryGame();
		com.moge.sdk.bean.RoleInfo mogeRoleInfo = getMogeRoleInfo(false, mRoleInfo);		// 获取乐游RoleInfo对象
		mSDKManager.updateGameRoleData(mActivity, mogeRoleInfo);		// 上传角色信息
	}
	
	/**
	 * 更新角色信息接口,游戏内角色信息发生（升级等）时调用
	 */
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUpdateRoleInfo(roleInfo);
		com.moge.sdk.bean.RoleInfo mogeRoleInfo = getMogeRoleInfo(false, mRoleInfo);		// 获取乐游RoleInfo对象
		mSDKManager.updateGameRoleData(mActivity, mogeRoleInfo);		// 上传角色信息
	}
	
	/**
	 * 获取乐游角色信息统一接口,得到乐游RoleInfo对象
	 * @param isCreateRole	标识是否是创建角色时调用
	 * @param roleInfo		天游RoleInfo对象,包含角色信息
	 * @return
	 */
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
	
	/**
	 * 注销接口
	 */
	@Override
	public void doLogout() {	super.doLogout();	mSDKManager.logout();	}
	
	/**
	 * 退出游戏接口
	 */
	@Override
	public void doExitGame() {	LogUtils.d("调用退出游戏接口");		mSDKManager.exit(mActivity, mOnExitListener);}
	
	
	/********************************	生命周期方法	**********************************************/
	
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
	
	/***********************************	回调接口	*******************************************************/
	
	/**
	 * 登录的回调接口
	 */
	private OnLoginListener mOnLoginListener = new OnLoginListener() {
		
		@Override
		public void onLoginSuccess(LoginResult loginResult) {
			LogUtils.d("loginResult: username= "+loginResult.username+",memkey= "+loginResult.memkey+",sign= "+loginResult.sign+",loginTime= "+loginResult.logintime);
			mLoginInfo.setChannelUserId(loginResult.username);
			mLoginInfo.setUserToken(loginResult.memkey);
			checkLogin();
		}
		
		@Override
		public void onLoginFailure(int code, String msg) { mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");}
	};
	
	/**
	 * 支付的回调接口
	 */
	private OnPaymentListener mOnPaymentListener = new OnPaymentListener() {
		
		@Override
		public void onPaySuccess(PaymentResult paymentResult) { checkOrder(tyOrderID);}
		
		@Override
		public void onPayFailure(int code, String msg, String money) { mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");}
	};
	
	/**
	 * 切换账号的回调接口(仅监听部分渠道浮标内的切换,并不监听logout)
	 */
	private OnSwitchAccountListener mOnSwitchAccountListener = new OnSwitchAccountListener() {
		
		@Override
		public void onSuccess(LoginResult loginResult) {
			mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "用户切换账号成功");		// 切换账号成功的回调里通知游戏返回登录页
			mLoginInfo.setChannelUserId(loginResult.username);
			mLoginInfo.setUserToken(loginResult.memkey);
			checkLogin();
		}
		
		@Override
		public void onFailure(int code, String msg) { LogUtils.d("切换账号失败  code= "+code+",msg= "+msg);}
		
		@Override
		public void onCancel() {LogUtils.d("切换账号取消");}
	};
	
	/**
	 * 注销登录的回调接口
	 */
	private OnUserLogoutListener mOnUserLogoutListener = new OnUserLogoutListener() {
		@Override
		public void onLogoutSuccess(int code, String msg) { mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, msg);}
		
		@Override
		public void onLogoutFailure(int code, String msg) {LogUtils.d("logout failed code= "+code+",msg= "+msg);}
	};
	
	/**
	 * 退出的回调接口
	 */
	private OnExitListener mOnExitListener = new OnExitListener() {
		
		@Override
		public void onSuccess() { mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS,"退出游戏");}
		
		@Override
		public void onFailure() { mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "");}
	};
	

}
