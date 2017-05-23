package com.tianyou.channel.business;

import android.app.Activity;
import android.content.Intent;

import com.quicksdk.Extend;
import com.quicksdk.Payment;
import com.quicksdk.QuickSDK;
import com.quicksdk.Sdk;
import com.quicksdk.User;
import com.quicksdk.entity.GameRoleInfo;
import com.quicksdk.entity.OrderInfo;
import com.quicksdk.entity.UserInfo;
import com.quicksdk.notifier.ExitNotifier;
import com.quicksdk.notifier.InitNotifier;
import com.quicksdk.notifier.LoginNotifier;
import com.quicksdk.notifier.LogoutNotifier;
import com.quicksdk.notifier.PayNotifier;
import com.quicksdk.notifier.SwitchAccountNotifier;
import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.PayInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.LogUtils;

public class QuickSdkService extends BaseSdkService{
	 
	private QuickSDK mQuickSDK;
	
	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(mActivity);
		
		mQuickSDK = QuickSDK.getInstance();	// 实例化QuickSDK
		
		setNotifiers();	// 设置通知，用于监听初始化、登录、注销、支付以及退出的回调
		
		Sdk.getInstance().init(mActivity,channelInfo.getAppId() , channelInfo.getAppKey());	// SDK初始化
		
		Sdk.getInstance().onCreate(mActivity);	// quicksdk生命周期方法
	}
	
	// 登录接口
	@Override
	public void doLogin() {
		super.doLogin();
		User.getInstance().login(mActivity);
	}

	// 支付接口
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		PayInfo productInfo = ConfigHolder.getPayInfo(mActivity, payInfo.getPayCode());
		// 订单信息
		OrderInfo quickoOrderInfo = new OrderInfo();
		quickoOrderInfo.setGoodsID(productInfo.getProductId());
		quickoOrderInfo.setGoodsName(orderInfo.getCurrency());
		quickoOrderInfo.setCpOrderID(orderInfo.getOrderID());
		quickoOrderInfo.setCount(Integer.parseInt(orderInfo.getMoNey())*Integer.parseInt(orderInfo.getRate()));
		quickoOrderInfo.setAmount(Double.parseDouble(orderInfo.getMoNey()));
		quickoOrderInfo.setExtrasParams(orderInfo.getOrderID());
		LogUtils.d("quickOrderInfo goodsID= "+quickoOrderInfo.getGoodsID()+",goodsName= "+quickoOrderInfo.getGoodsName()
				+",cpOrderID= "+quickoOrderInfo.getCpOrderID()+",count= "+quickoOrderInfo.getCount()+",amount= "+quickoOrderInfo.getAmount());
		
		// 角色信息
		GameRoleInfo gameRoleInfo = new GameRoleInfo();
		gameRoleInfo.setServerID(mRoleInfo.getServerId());
		gameRoleInfo.setServerName(mRoleInfo.getServerName());
		gameRoleInfo.setGameRoleName(mRoleInfo.getRoleName());
		gameRoleInfo.setGameRoleID(mRoleInfo.getRoleId());
		gameRoleInfo.setGameUserLevel(mRoleInfo.getRoleLevel());
		gameRoleInfo.setVipLevel(mRoleInfo.getVipLevel());
		gameRoleInfo.setGameBalance(mRoleInfo.getBalance());
		gameRoleInfo.setPartyName(mRoleInfo.getParty());
		
		Payment.getInstance().pay(mActivity, quickoOrderInfo, gameRoleInfo);
	}
	
	// 注销接口
	@Override
	public void doLogout() {
		super.doLogout();
		User.getInstance().logout(mActivity);
	}
	
	// 是否显示退出界面
	@Override
	public boolean isShowExitGame() {
		return !Sdk.getInstance().isSDKShowExitDialog();
	}
	
	// 退出接口
	@Override
	public void doExitGame() {
		LogUtils.d("调用退出接口");
		Sdk.getInstance().exit(mActivity);
	}
	
	// 上传角色信息
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		User.getInstance().setGameRoleInfo(mActivity, getGameRoleInfo(mRoleInfo), false);
	}
	
	// 创建角色
	@Override
	public void doCreateRole(RoleInfo roleInfo) {
		super.doCreateRole(roleInfo);
		User.getInstance().setGameRoleInfo(mActivity, getGameRoleInfo(mRoleInfo), true);
	}
	
	// 进入游戏
	@Override
	public void doEntryGame() {
		super.doEntryGame();
		User.getInstance().setGameRoleInfo(mActivity, getGameRoleInfo(mRoleInfo), false);
	}
	
	// 角色升级
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUpdateRoleInfo(roleInfo);
		User.getInstance().setGameRoleInfo(mActivity, getGameRoleInfo(mRoleInfo), false);
	}
	
	// 获取角色信息统一方法
	private GameRoleInfo getGameRoleInfo(RoleInfo roleInfo){
		GameRoleInfo gameRoleInfo = new GameRoleInfo();
		gameRoleInfo.setServerID(roleInfo.getServerId());
		gameRoleInfo.setServerName(roleInfo.getServerName());
		gameRoleInfo.setGameRoleName(roleInfo.getRoleName());
		gameRoleInfo.setGameRoleID(roleInfo.getRoleId());
		gameRoleInfo.setGameBalance(roleInfo.getBalance());
		gameRoleInfo.setVipLevel(roleInfo.getVipLevel());
		gameRoleInfo.setGameUserLevel(roleInfo.getRoleLevel());
		gameRoleInfo.setPartyName(roleInfo.getParty());
		gameRoleInfo.setRoleCreateTime(roleInfo.getCreateTime());
		gameRoleInfo.setPartyId("0");
		gameRoleInfo.setGameRoleGender("无");
		gameRoleInfo.setGameRolePower("0");
		gameRoleInfo.setPartyRoleId("0");
		gameRoleInfo.setPartyRoleName("无");
		gameRoleInfo.setProfessionId("0");
		gameRoleInfo.setProfession(roleInfo.getProfession());
		gameRoleInfo.setFriendlist("无");
		return gameRoleInfo;
	}
	
	private void setNotifiers(){
		
		mQuickSDK.setInitNotifier(mInitNotifier);	// 设置初始化通知
		
		mQuickSDK.setLoginNotifier(mLoginNotifier);	// 设置登录通知
		
		mQuickSDK.setLogoutNotifier(mLogoutNotifier);	//设置注销通知
		
		mQuickSDK.setSwitchAccountNotifier(mSwitchAccountNotifier);	//设置切换账号通知
		
		mQuickSDK.setPayNotifier(mPayNotifier);	// 设置支付通知 
		
		mQuickSDK.setExitNotifier(mExitNotifier);	// 设置退出游戏通知
		
	}

	// 初始化回调通知
	private InitNotifier mInitNotifier = new InitNotifier() {
		@Override
		public void onSuccess() { mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");}
		
		@Override
		public void onFailed(String msg, String trace) { LogUtils.d("SDK 初始化失败 msg= "+msg+", trace= "+trace);}
	};
	
	// 登录回调通知
	private LoginNotifier mLoginNotifier = new LoginNotifier() {
		
		@Override
		public void onSuccess(UserInfo userInfo) {
			String uid = userInfo.getUID();
			String userName = userInfo.getUserName();
			String token = userInfo.getToken();
			int channelType = Extend.getInstance().getChannelType();
			LogUtils.d("login success uid= "+uid+",userName= "+userName+",token= "+token+",channeltype= "+channelType);
			LoginInfo loginParam = new LoginInfo();
			loginParam.setChannelUserId(uid);
			loginParam.setNickname(userName);
			loginParam.setUserToken(token);
			loginParam.setIsGuest(channelType+"");
			checkLogin(loginParam);
		}
		
		@Override
		public void onFailed(String msg, String trace) { 
			LogUtils.d("登录失败 msg= "+msg+",tace= "+trace);
			mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
		}
		
		@Override
		public void onCancel() { mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "登录取消");}
	};
	
	// 注销回调通知
	private LogoutNotifier mLogoutNotifier = new LogoutNotifier() {
		
		@Override
		public void onSuccess() { mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "用户注销成功");}
		
		@Override
		public void onFailed(String msg, String trace) { LogUtils.d("注销失败 msg= "+msg+",trace= "+trace);}
	};
	
	// 切换账号回调通知
	private SwitchAccountNotifier mSwitchAccountNotifier = new SwitchAccountNotifier() {
		
		@Override
		public void onSuccess(UserInfo userInfo) {
			String uid = userInfo.getUID();
			String userName = userInfo.getUserName();
			String token = userInfo.getToken();
			int channelType = Extend.getInstance().getChannelType();
			LogUtils.d("switchAccount success uid= "+uid+",userName= "+userName+",token= "+token+",channeltype= "+channelType);
			LoginInfo loginParam = new LoginInfo();
			loginParam.setChannelUserId(uid);
			loginParam.setNickname(userName);
			loginParam.setUserToken(token);
			loginParam.setIsGuest(channelType+"");
			checkLogin(loginParam);
		}
		
		@Override
		public void onFailed(String msg, String trace) { mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");}
		
		@Override
		public void onCancel() { mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "登录取消");}
	};
	
	// 支付回调通知
	private PayNotifier mPayNotifier = new PayNotifier() {
		
		@Override
		public void onSuccess(String sdkOrderID, String cpOrderID, String extrasParams) { 
			LogUtils.d("pay success cpOrderID= "+cpOrderID);
			checkOrder(cpOrderID);
		}
		
		@Override
		public void onFailed(String cpOderID, String msg, String trace) { mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");}
		
		@Override
		public void onCancel(String cpOrderID) { mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "支付取消");}
	};
	
	// 退出游戏回调通知
	private ExitNotifier mExitNotifier = new ExitNotifier() {
		
		@Override
		public void onSuccess() { mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "退出游戏");}
		
		@Override
		public void onFailed(String msg, String trace) { mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "退出失败");}
	};
	
	// 生命周期方法
	@Override
	public void doStart() { Sdk.getInstance().onStart(mActivity);}
	
	@Override
	public void doRestart() { Sdk.getInstance().onRestart(mActivity);}
	
	@Override
	public void doPause() { Sdk.getInstance().onPause(mActivity);}
	
	@Override
	public void doResume() { Sdk.getInstance().onResume(mActivity);}
	
	@Override
	public void doStop() { Sdk.getInstance().onStop(mActivity);}
	
	@Override
	public void doDestory() { Sdk.getInstance().onDestroy(mActivity);}
	
	@Override
	public void doNewIntent(Intent intent) { Sdk.getInstance().onNewIntent(intent);}
	
	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) { Sdk.getInstance().onActivityResult(mActivity, requestCode, resultCode, data);}
}
