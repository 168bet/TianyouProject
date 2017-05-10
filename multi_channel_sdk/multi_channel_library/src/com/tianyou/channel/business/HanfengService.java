package com.tianyou.channel.business;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

import com.hanfeng.nsdk.NSdk;
import com.hanfeng.nsdk.NSdkListener;
import com.hanfeng.nsdk.NSdkStatusCode;
import com.hanfeng.nsdk.bean.NSAppInfo;
import com.hanfeng.nsdk.bean.NSLoginResult;
import com.hanfeng.nsdk.bean.NSPayInfo;
import com.hanfeng.nsdk.bean.NSRoleInfo;
import com.hanfeng.nsdk.exception.NSdkException;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.LogUtils;

public class HanfengService extends BaseSdkService{
	
	private NSdk mNSdk;

	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		NSAppInfo appInfo = new NSAppInfo();
		appInfo.appId = mChannelInfo.getAppId();
		appInfo.appKey = mChannelInfo.getAppKey();
		LogUtils.d("appId= "+mChannelInfo.getAppId()+",appkey= "+mChannelInfo.getAppKey());
		
		mNSdk = NSdk.getInstance();
		try {
			mNSdk.init(mActivity, appInfo, new NSdkListener<String>() {
				@Override
				public void callback(int code, String msg) {
					if (code == NSdkStatusCode.INIT_SUCCESS) {mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");}
				}
			});
		} catch (NSdkException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		try {
			mNSdk.login(mActivity, new NSdkListener<NSLoginResult>() {

				@Override
				public void callback(int code, NSLoginResult result) {
					switch (code) {
					case NSdkStatusCode.LOGIN_SUCCESS:
						mNSdk.showToolBar(mActivity);
						LoginInfo loginParam = new LoginInfo();
						loginParam.setChannelUserId(result.uid);
						loginParam.setUserToken(mNSdk.getChannel());
						loginParam.setNickname(result.sid);
						loginParam.setIsGuest(mNSdk.getSdkVersion());
						LogUtils.d("sid= "+result.sid+",uid= "+result.uid+",msg:= "+result.msg);
						checkLogin(loginParam);
						break;
						
					case NSdkStatusCode.LOGIN_CANCLE:
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "取消登录");
						break;

					default:
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
						break;
					}
				}
			});
		} catch (NSdkException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doEntryGame() {
		super.doEntryGame();
		NSRoleInfo nsRoleInfo = new NSRoleInfo();
		nsRoleInfo.roleId = mRoleInfo.getRoleId();
		nsRoleInfo.roleName = mRoleInfo.getRoleName();
		nsRoleInfo.roleLevel = mRoleInfo.getRoleLevel();
		nsRoleInfo.zoneId = mRoleInfo.getServerId();
		nsRoleInfo.zoneName = mRoleInfo.getServerName();
		nsRoleInfo.dataType = "1";
		nsRoleInfo.userId = mHanfengUid;
		nsRoleInfo.ext = "";
		mNSdk.submitGameInfo(mActivity, nsRoleInfo);
	}

	@Override
	public void doCreateRole(RoleInfo roleInfo) {
		super.doCreateRole(roleInfo);
		NSRoleInfo nsRoleInfo = new NSRoleInfo();
		nsRoleInfo.roleId = mRoleInfo.getRoleId();
		nsRoleInfo.roleName = mRoleInfo.getRoleName();
		nsRoleInfo.roleLevel = mRoleInfo.getRoleLevel();
		nsRoleInfo.zoneId = mRoleInfo.getServerId();
		nsRoleInfo.zoneName = mRoleInfo.getServerName();
		nsRoleInfo.dataType = "2";
		nsRoleInfo.userId = mHanfengUid;
		nsRoleInfo.ext = "";
		mNSdk.submitGameInfo(mActivity, nsRoleInfo);
		
	}
	
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUpdateRoleInfo(roleInfo);
		NSRoleInfo nsRoleInfo = new NSRoleInfo();
		nsRoleInfo.roleId = mRoleInfo.getRoleId();
		nsRoleInfo.roleName = mRoleInfo.getRoleName();
		nsRoleInfo.roleLevel = mRoleInfo.getRoleLevel();
		nsRoleInfo.zoneId = mRoleInfo.getServerId();
		nsRoleInfo.zoneName = mRoleInfo.getServerName();
		nsRoleInfo.dataType = "3";
		nsRoleInfo.userId = mHanfengUid;
		nsRoleInfo.ext = "";
		mNSdk.submitGameInfo(mActivity, nsRoleInfo);
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		PayInfo productInfo = ConfigHolder.getPayInfo(mActivity, payInfo.getPayCode());
		NSPayInfo nsPayInfo = new NSPayInfo();
		nsPayInfo.gameName = "龙神之光";
		nsPayInfo.productId = productInfo.getProductId();
		nsPayInfo.productName = productInfo.getProductName();
		nsPayInfo.productDesc = productInfo.getProductDesc();
		nsPayInfo.price = Integer.parseInt(productInfo.getMoney());
		nsPayInfo.ratio = orderInfo.getRatio();
		nsPayInfo.buyNum = 1;
		nsPayInfo.coinNum = 0;
		nsPayInfo.serverId = Integer.parseInt(orderInfo.getServerID());
		nsPayInfo.uid = mHanfengUid;
		LogUtils.d("mHanfengUid= "+mHanfengUid);
		nsPayInfo.roleId = mRoleInfo.getRoleId();
		nsPayInfo.roleName = mRoleInfo.getRoleName();
		nsPayInfo.roleLevel = Integer.parseInt(mRoleInfo.getRoleLevel());
		nsPayInfo.giftId = "";
		nsPayInfo.privateField = orderInfo.getOrderID();
		
		try {
			mNSdk.pay(mActivity, nsPayInfo, new NSdkListener<String>() {
				@Override
				public void callback(int code, String msg) {
					switch (code) {
					case NSdkStatusCode.PAY_SUCCESS:
						checkOrder(orderInfo.getOrderID());
						break;
						
					case NSdkStatusCode.PAY_CANCLE:
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "支付取消");
						break;

					default:
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
						break;
					}
				}
			});
		} catch (NSdkException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doLogout() {
		super.doLogout();
		mNSdk.setAccountSwitchListener(new NSdkListener<String>() {

			@Override
			public void callback(int code, String msg) {
				if (code == NSdkStatusCode.SWITCH_SUCCESS) {
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "切换账号成功");
				}
			}
		});
		mNSdk.accountSwitch(mActivity);
	}
	
	@Override
	public void doExitGame() {
		super.doExitGame();
		try {
			mNSdk.exit(mActivity, new NSdkListener<Void>() {
				@Override
				public void callback(int code, Void response) {
					if (code == NSdkStatusCode.EXIT_COMFIRM) {
						mNSdk.logout(mActivity);
						mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "退出游戏");
					} else {
						mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "取消退出");
					}
				}
			});
		} catch (NSdkException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) {
		super.doActivityResult(requestCode, resultCode, data);
		mNSdk.onActivityResult(mActivity, requestCode, resultCode, data);
	}
	
	@Override
	public void doBackPressed() {
		super.doBackPressed();
		mNSdk.onBackPressed(mActivity);
	}
	
	@Override
	public void doPause() {
		super.doPause();
		mNSdk.onPause(mActivity);
	}
	
	@Override
	public void doResume() {
		super.doResume();
		mNSdk.onResume(mActivity);
	}
	
	@Override
	public void doStop() {
		super.doStop();
		mNSdk.onStop(mActivity);
	}
	
	@Override
	public void doDestory() {
		super.doDestory();
		mNSdk.onDestroy(mActivity);
	}
	
	@Override
	public void doRestart() {
		super.doRestart();
		mNSdk.onRestart(mActivity);
	}
	
	@Override
	public void doNewIntent(Intent intent) {
		super.doNewIntent(intent);
		mNSdk.onNewIntent(mActivity, intent);
	}
	
	@Override
	public void doConfigurationChanged(Configuration newConfig) {
		super.doConfigurationChanged(newConfig);
		mNSdk.onConfigurationChanged(mActivity, newConfig);
	}
	
}
