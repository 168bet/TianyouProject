package com.tianyou.channel.business;

import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.miaole.sdk.IU8SDKListener;
import com.miaole.sdk.InitResult;
import com.miaole.sdk.MLCode;
import com.miaole.sdk.MLSDK;
import com.miaole.sdk.PayParams;
import com.miaole.sdk.PayResult;
import com.miaole.sdk.UserExtraData;
import com.miaole.sdk.log.Log;
import com.miaole.sdk.plugin.MLPay;
import com.miaole.sdk.plugin.MLUser;
import com.miaole.sdk.verify.UToken;
import com.miaole.sdk.verify.UserInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;

/**
 * U8SDK
 * @author itstrong
 *
 */
public class ShenqiSdkService extends BaseSdkService {

	private String mOrderID;
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		MLSDK.getInstance().onAppCreate((Application) context);
	}
	
	@Override
	public void doApplicationAttach(Context base) {
		MultiDex.install(base);
	    Log.init(base);
	    MLSDK.getInstance().onAppAttachBaseContext((Application) base.getApplicationContext(), base);
	}
	
	@Override
	public void doApplicationConfigurationChanged(Application application, Configuration newConfig) {
		MLSDK.getInstance().onAppConfigurationChanged(application, newConfig);
	}
	
	@Override
	public void doApplicationTerminate() {
		MLSDK.getInstance().onTerminate();
		Log.destory();
	}

	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		MLSDK.getInstance().setSDKListener(new IU8SDKListener() {
			@Override
			public void onUserList(List<UserInfo> userInfo) {
				LogUtils.d("onUserList:userInfo=" + userInfo.toString());
			}
			
			@Override
			public void onSwitchAccount(String data) {
				LogUtils.d("onSwitchAccount:data=" + data);
			}
			
			@Override
			public void onSwitchAccount() {
				LogUtils.d("onSwitchAccount");
			}
			
			@Override
			public void onResult(int code, String msg) {
//				ToastUtils.showToast(mActivity, "onResult:code=" + code + ",msg=" + msg);
				LogUtils.d("onResult:code=" + code + ",msg=" + msg); 
				switch (code) {
				case MLCode.CODE_INIT_SUCCESS:
					mTianyouCallback.onResult(TianyouCallback.CODE_INIT, msg);
					break;
				case MLCode.CODE_LOGIN_SUCCESS:
//					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_SUCCESS, msg);
					break;
				case MLCode.CODE_LOGIN_FAIL:
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, msg);
					break;
				case MLCode.CODE_LOGOUT_SUCCESS:
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, msg);
					break;
				case MLCode.CODE_PAY_SUCCESS:
//					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_SUCCESS, msg);
					break;
				case MLCode.CODE_PAY_FAIL:
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, msg);
					break;
				}
			}
			
			@Override
			public void onPayResult(PayResult result) {
				LogUtils.d("onPayResult:result=" + result.toString());
				if (mOrderID != null) checkOrder(mOrderID);
			}
			
			@Override
			public void onLogout() {
				LogUtils.d("onLogout");
			}
			
			@Override
			public void onLoginResult(String result) {
				LogUtils.d("onLoginResult:result=" + result);
			}
			
			@Override
			public void onInitResult(InitResult result) {
				LogUtils.d("onInitResult:result=" + result.toString());
			}
			
			@Override
			public void onAuthResult(UToken token) {
				String userId = token.getSdkUserID();
				String session = token.getToken();
				LogUtils.d("userId=" + userId + ",session=" + session);
				checkLogin(userId + "", session);
				mChannelUserId = token.getUserID() + "";
			}
		});
		MLSDK.getInstance().init(mActivity);
	}

	@Override
	public void doLogin() {
		MLSDK.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				MLUser.getInstance().login();
			}
		});
	}

	@Override
	public void doChannelPay(OrderinfoBean orderInfo) {
		LogUtils.d("orderInfo:" + orderInfo);
		LogUtils.d("mPayInfo:" + mPayInfo);
		LogUtils.d("mRoleInfo:" + mRoleInfo);
		mOrderID = orderInfo.getCustomInfo();
		final PayParams payParam = new PayParams();
		payParam.setBuyNum(1); // 购买数量
		payParam.setCoinNum(Integer.parseInt(orderInfo.getMoNey()) * Integer.parseInt(orderInfo.getRate())); // 游戏币数量
		payParam.setOrderID(orderInfo.getOrderID()); // CP订单号
		payParam.setPrice(Integer.parseInt(orderInfo.getMoNey())); // 金额 单位：元
		payParam.setProductDesc(orderInfo.getCurrency()); // 商品描述
		payParam.setProductId(mPayInfo.getProductId()); // 商品ID
		payParam.setProductName(mPayInfo.getProductName()); // 商品名称
//		payParam.setRatio(Integer.parseInt(orderInfo.getRate())); // 充值比例
		payParam.setRoleId(mRoleInfo.getRoleId()); // 角色ID
		payParam.setRoleLevel(Integer.parseInt(mRoleInfo.getRoleLevel())); // 角色等级
		payParam.setRoleName(mRoleInfo.getRoleName()); // 角色名称
		payParam.setServerId(mRoleInfo.getServerId()); // 服务器ID
		payParam.setServerName(mRoleInfo.getServerName()); // 服务器名称
		payParam.setVip(mRoleInfo.getVipLevel()); // VIP等级
		payParam.setExtension(orderInfo.getCustomInfo()); // 透传参数
		LogUtils.d("payParam:" + payParam.toString());
		MLSDK.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				MLPay.getInstance().pay(payParam);
			}
		});
	}

	@Override
	public void doExitGame() {
		MLSDK.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				MLUser.getInstance().exit();
				submitGameInfo(UserExtraData.TYPE_LEVEL_UP);
			}
		});
	}

	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		MLSDK.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				submitGameInfo(UserExtraData.TYPE_CREATE_ROLE);
				submitGameInfo(UserExtraData.TYPE_LEVEL_UP);
			}
		});
		
	}
	
	private void submitGameInfo(int type) {
		UserExtraData extraData = new UserExtraData();
		extraData.setDataType(type);
		extraData.setExt(mRoleInfo.getCustomInfo());
		extraData.setMoneyNum(100);
		extraData.setRoleCreateTime(1481261366);
		extraData.setRoleID(mRoleInfo.getRoleId());
		extraData.setRoleLevel(mRoleInfo.getRoleLevel());
		extraData.setRoleLevelUpTime(1481261366);
		extraData.setRoleName(mRoleInfo.getRoleName());
		extraData.setRoleType("战士");
		extraData.setServerID(Integer.parseInt(mRoleInfo.getServerId()));
		extraData.setServerName(mRoleInfo.getServerName());
		extraData.setVip("5");
		MLUser.getInstance().submitExtraData(extraData);
	}
	
	@Override
	public void entryGame() {
		MLSDK.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				submitGameInfo(UserExtraData.TYPE_ENTER_GAME);
			}
		});
	}
	
	@Override
	public void doResume() {
		MLSDK.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				MLSDK.getInstance().onResume();
			}
		});
	}
	
	@Override
	public void doPause() {
		MLSDK.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				MLSDK.getInstance().onPause();
			}
		});
	}
	
	@Override
	public void doStart() {
		MLSDK.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				MLSDK.getInstance().onStart();
			}
		});
	}
	
	@Override
	public void doStop() {
		MLSDK.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				MLSDK.getInstance().onStop();
			}
		});
	}
	
	@Override
	public void doDestory() {
		MLSDK.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				MLSDK.getInstance().onDestroy();
			}
		});
	}
	
	@Override
	public void doNewIntent(final Intent intent) {
		MLSDK.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				MLSDK.getInstance().onNewIntent(intent);
			}
		});
	}
	
	@Override
	public void doActivityResult(final int requestCode, final int resultCode, final Intent data) {
		MLSDK.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				MLSDK.getInstance().onActivityResult(requestCode, resultCode, data);
			}
		});
	}
}
