package com.tianyou.channel.business;

import android.app.Activity;
import android.content.Intent;

import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.AppUtils;
import com.tianyou.channel.utils.LogUtils;
import com.u8.sdk.IU8SDKListener;
import com.u8.sdk.InitResult;
import com.u8.sdk.PayParams;
import com.u8.sdk.PayResult;
import com.u8.sdk.U8Code;
import com.u8.sdk.U8SDK;
import com.u8.sdk.UserExtraData;
import com.u8.sdk.plugin.U8Pay;
import com.u8.sdk.plugin.U8User;
import com.u8.sdk.verify.UToken;

public class AsyxSdkService extends BaseSdkService{
	
	private String tyOrderID;	// 天游订单号，校验支付时使用
	
	/**
	 * Activity初始化接口
	 */
	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		U8SDK.getInstance().setSDKListener(mIu8sdkListener);	// 设置回调监听
		
		U8SDK.getInstance().init(mActivity);	// 初始化SDK
		
		U8SDK.getInstance().onCreate();		// 生命周期方法
	}
	
	/**
	 * 登录接口
	 */
	@Override
	public void doLogin() {
		super.doLogin();
		U8SDK.getInstance().runOnMainThread(new Runnable() {	//主线程中调用SDK接口
			@Override
			public void run() { U8User.getInstance().login();}
		});
	}
	
	/**
	 * 上传角色信息接口
	 */
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		U8User.getInstance().submitExtraData(getUserExtraData(1, mRoleInfo));	// 调用SDK上传角色接口
	}
	
	/**
	 * 创建角色接口
	 */
	@Override
	public void doCreateRole(RoleInfo roleInfo) {
		super.doCreateRole(roleInfo);
		U8User.getInstance().submitExtraData(getUserExtraData(2, mRoleInfo));	// 调用SDK上传角色接口
	}
	
	/**
	 * 进入游戏接口
	 */
	@Override
	public void doEntryGame() {
		super.doEntryGame();
		U8User.getInstance().submitExtraData(getUserExtraData(3, mRoleInfo));	// 调用SDK上传角色接口
	}
	
	/**
	 * 角色等级提升
	 */
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUpdateRoleInfo(roleInfo);
		U8User.getInstance().submitExtraData(getUserExtraData(4, mRoleInfo));	// 调用SDK上传角色接口
	}
	
	
	/**
	 * 支付接口
	 */
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		tyOrderID = orderInfo.getCustomInfo();
		U8SDK.getInstance().runOnMainThread(new Runnable() {	// 主线程中调用SDK支付接口
			@Override
			public void run() { U8Pay.getInstance().pay(getPayParams(orderInfo));}
		});
	}
	
	/**
	 * 游戏退出接口
	 */
	@Override
	public void doExitGame() {
		LogUtils.d("调用退出游戏接口");
		if (mRoleInfo != null) U8User.getInstance().submitExtraData(getUserExtraData(5, mRoleInfo));	// 调用SDK上传角色接口
		U8User.getInstance().exit();	// 调用SDK退出游戏接口
	}
	

	/**
	 * 获取角色信息
	 * @param dataType	调用时机标识(1:选择服务器; 2:创建角色; 3:进入游戏; 4:等级提升; 5:退出游戏)
	 * @param roleInfo	角色信息
	 * @return
	 */
	private UserExtraData getUserExtraData (int dataType,RoleInfo roleInfo) {
		UserExtraData userExtraData = new UserExtraData();
		userExtraData.setDataType(dataType);	// 调用时机标识
		userExtraData.setServerID(Integer.parseInt(roleInfo.getServerId()));	// 玩家所在区服ID
		userExtraData.setServerName(roleInfo.getServerName());	// 玩家所在区服名称
		userExtraData.setRoleID(roleInfo.getRoleId());	// 玩家角色ID
		userExtraData.setRoleName(roleInfo.getRoleName());	// 玩家角色名称
		userExtraData.setRoleLevel(roleInfo.getRoleLevel());	// 玩家角色等级
		userExtraData.setMoneyNum(Integer.parseInt(roleInfo.getBalance()));	// 当前角色身上剩余游戏币数量
		userExtraData.setRoleCreateTime(Long.parseLong(roleInfo.getCreateTime()));	// 角色创建时间
		String roleLevelUpTime = (roleInfo.getRoleLevelUpTime() == null ? AppUtils.getSystemTime() : roleInfo.getRoleLevelUpTime());
		LogUtils.d("roleLevelUpTime= "+roleLevelUpTime);
		userExtraData.setRoleLevelUpTime(Long.parseLong(roleLevelUpTime));	// 角色等级变化时间
		userExtraData.setVip(roleInfo.getVipLevel());	// 角色VIP等级
		return userExtraData;
	}
	
	/**
	 * 获取渠道所需支付参数
	 * @param orderInfo	天游订单信息
	 * @return
	 */
	private PayParams getPayParams(OrderinfoBean orderInfo) {
		PayParams payParams = new PayParams();
		payParams.setProductId(orderInfo.getProductId());		// 商品ID
		payParams.setProductName(orderInfo.getProductName());	// 商品名称
		payParams.setProductDesc(orderInfo.getProductDesc());	// 商品描述
		payParams.setPrice(Integer.parseInt(orderInfo.getMoNey()));	// 充值金额
		payParams.setRatio(orderInfo.getRatio());	// 兑换比例（暂时无用途）
		payParams.setBuyNum(1);	// 购买数量（一般为1）
		payParams.setCoinNum(Integer.parseInt(mRoleInfo.getBalance()));	// 玩家当前身上剩余游戏币数量
		payParams.setServerId(mRoleInfo.getServerId());	// 玩家所在区服ID
		payParams.setServerName(mRoleInfo.getServerName());	// 玩家所在区服名称
		payParams.setRoleId(mRoleInfo.getRoleId());	// 玩家角色ID
		payParams.setRoleName(mRoleInfo.getRoleName());	// 玩家角色名称
		payParams.setRoleLevel(Integer.parseInt(mRoleInfo.getRoleLevel()));	// 玩家角色等级
		payParams.setVip(mRoleInfo.getVipLevel());	// 玩家VIP等级
		payParams.setPayNotifyUrl(orderInfo.getNotifyurl());	// 支付回调地址
		payParams.setOrderID(orderInfo.getOrderID());	// U8订单号，下单时U8Server返回的
		payParams.setExtension(orderInfo.getCustomInfo());	// 透传参数（天游订单号）
		return payParams;
	}
	
	/************************************	生命周期接口调用	*************************************************/
	
	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) { U8SDK.getInstance().onActivityResult(requestCode, resultCode, data);}
	
	@Override
	public void doStart() { U8SDK.getInstance().onStart();}
	
	@Override
	public void doPause() { U8SDK.getInstance().onPause();}
	
	@Override
	public void doResume() { U8SDK.getInstance().onResume();}
	
	@Override
	public void doNewIntent(Intent intent) { U8SDK.getInstance().onNewIntent(intent);}
	
	@Override
	public void doStop() { U8SDK.getInstance().onStop();}
	
	@Override
	public void doRestart() { U8SDK.getInstance().onRestart();}
	
	@Override
	public void doDestory() { U8SDK.getInstance().onDestroy();}
	
	
	/***************************	回调接口		******************************************/
	
	private IU8SDKListener mIu8sdkListener = new IU8SDKListener() {
		
		@Override
		public void onInitResult(InitResult initResult) {
			// TODO SDK初始化完成
			LogUtils.d("init success -------------");
		}
		
		@Override
		public void onSwitchAccount(String msg) {
			LogUtils.d("switchAccout with msg= "+msg);
		}
		
		@Override
		public void onSwitchAccount() {
			LogUtils.d("switchAccount without msg");
		}
		
		@Override
		public void onPayResult(PayResult payResult) {
			LogUtils.d("on pay result");
		}
		
		@Override
		public void onLogout() {
			LogUtils.d("on logout--------");
		}
		
		@Override
		public void onLoginResult(String msg) {
			LogUtils.d("on login result msg= "+msg);
		}
		
		@Override
		public void onAuthResult(UToken token) {
			String sdkUserID = token.getSdkUserID();
			String sdkUsername = token.getSdkUsername();
			String sdkToken = token.getToken();
			int userID = token.getUserID();
			String username = token.getUsername();
			LogUtils.d("on authRestul sdkUserID= "+sdkUserID+",sdkUsername= "+sdkUsername+",sdkToken= "+sdkToken+",userID= "+userID+",userName= "+username);
			mLoginInfo.setChannelUserId(userID+"");
			mLoginInfo.setUserToken(sdkToken);
			checkLogin();
		}
		
		@Override
		public void onResult(int code, String msg) {
			LogUtils.d("on result code= "+code+",msg= "+msg);
			switch (code) {
			case U8Code.CODE_INIT_SUCCESS:
				mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");	// SDK初始化完成的回调
				break;
			case U8Code.CODE_LOGIN_FAIL:
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");		// 登录失败的回调
				break;
			case U8Code.CODE_PAY_FAIL:
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");		// 支付失败的回调
				break;
			case U8Code.CODE_PAY_SUCCESS:
				if (tyOrderID !=null) checkOrder(tyOrderID);	// 支付成功的客户端回调，此处进行后台校验订单操作
				break;
			case U8Code.CODE_LOGOUT_SUCCESS:
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "用户注销登录");	// 用户注销账号回调
				break;
			}
		}
	};

}
