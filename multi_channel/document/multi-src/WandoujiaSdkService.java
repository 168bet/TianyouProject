package com.tianyou.channel.business;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ConfigHolder;
import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi;
import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi.ExitCallback;
import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi.SwitchUser;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnLoginFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnLogoutFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnPayFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.WandouAccountListener;
import com.wandoujia.mariosdk.plugin.api.model.model.LoginFinishType;
import com.wandoujia.mariosdk.plugin.api.model.model.LogoutFinishType;
import com.wandoujia.mariosdk.plugin.api.model.model.PayResult;
import com.wandoujia.mariosdk.plugin.api.model.model.UnverifiedPlayer;

public class WandoujiaSdkService extends BaseSdkService {

	private static long appID;
	private static String appSecret;
	private static WandouGamesApi wandouGamesApi;
	
	private static String uid;
	
	private static String tyAppID;
	private static String promotion;
	

	public static WandouGamesApi getWandouGamesApi() {
		return wandouGamesApi;
	}
	
	@Override
	public void doApplicationAttach(Context base) {
		Log.d("TAG", "attach-------------------");
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(base);
		
		appID = Long.parseLong(channelInfo.getAppId());
		appSecret = channelInfo.getAppSecret();
		
		tyAppID = channelInfo.getGameId();
		promotion = channelInfo.getChannelId();
		WandouGamesApi.initPlugin(base, appID, appSecret);
	}

	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		wandouGamesApi = new WandouGamesApi.Builder(context, appID, appSecret).create();
		wandouGamesApi.setLogEnabled(true);
		
		wandouGamesApi.addWandouAccountListener(new WandouAccountListener() {
			
			@Override
			public void onLogoutSuccess() {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "用户注销账号");
			}
			
			@Override
			public void onLoginSuccess() {
//				checkLogin(uid, session);
//				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_SUCCESS, );
				Log.d("TAG", "on login success ----------------");
			}
			
			@Override
			public void onLoginFailed(int arg0, String arg1) {
				Log.d("TAG", "on login failed -----------------");
			}
		});
	}
	
	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		wandouGamesApi.init(mActivity);
		wandouGamesApi.onCreate(mActivity);
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
	}
	
	@Override
	public void doLogin() {
		wandouGamesApi.login(new OnLoginFinishedListener() {
			@Override
			public void onLoginFinished(LoginFinishType type, UnverifiedPlayer player) {
				switch (type) {
				case CANCEL:
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "登录取消");
					break;

				case LOGIN:
					String sid = player.getId();
					String token = player.getToken();
					Log.d("TAG", "sid= "+sid+",token= "+token);
					checkLogin(sid, token);
					break;
				
				}
			}
		});
	}
	
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		wandouGamesApi.postGameInformation("enterServer", mRoleInfo.getRoleId(),
				mRoleInfo.getRoleName(), Long.parseLong(mRoleInfo.getRoleLevel()), 
				Long.parseLong(mRoleInfo.getServerId()),
				mRoleInfo.getServerName(), Long.parseLong(mRoleInfo.getBalance()), 
				Long.parseLong(mRoleInfo.getVipLevel()), mRoleInfo.getParty());
		
	}
	
//	@Override
//	public void doLogout() {
//		wandouGamesApi.logout(new OnLogoutFinishedListener() {
//			@Override
//			public void onLoginFinished(LogoutFinishType type) {
//				switch (type) {
//				case CANCEL:
////					callback.onFailed("注销账号取消");
//					Log.d("TAG", "logout cancle------");
//					break;
//
//				case LOGOUT_SUCCESS:
////					callback.onSuccess("登录账号成功");
//					Log.d("TAG", "logout success------");
//					break;
//					
//				case LOGOUT_FAIL:
////					callback.onFailed("注销账号失败");
//					Log.d("TAG", "logout failed------");
//					break;
//				}
//			}
//		});
//	}
	
	
	@Override
	public void doGetTokenInfo() {
		super.doGetTokenInfo();
	}
	
	@Override
	public void doChannelPay(OrderinfoBean orderInfo) {
		super.doChannelPay(orderInfo);
		long money = Long.parseLong(orderInfo.getMoNey());
		long itemCount = Long.parseLong(orderInfo.getProductNum());
		final String orderID = orderInfo.getOrderID();
		wandouGamesApi.pay(mActivity, mPayInfo.getProductName(), money, itemCount, orderID, new OnPayFinishedListener() {
			@Override
			public void onPaySuccess(PayResult result) {
				checkOrder(orderID);
			}
			
			@Override
			public void onPayFail(PayResult result) {
				Log.d("TAG", "faile result= "+result.toString());
//				callback.onFailed("充值失败");
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
			}
			
		});
		
		
	}
	
	@Override
	public void doExitGame() {
		wandouGamesApi.exit(mActivity, new ExitCallback() {
			@Override
			public void onGameExit() {
//				callback.onSuccess("退出游戏成功");
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "退出游戏");
			}
			
			@Override
			public void onChannelExit() {}
		}, true);
	}
	
	@Override
	public void doResume() {
		wandouGamesApi.onResume(mActivity);
	}
	
	@Override
	public void doPause() {
		wandouGamesApi.onPause(mActivity);
	}
	
	@Override
	public void doStop() {
		wandouGamesApi.onStop(mActivity);
	}
	
	@Override
	public void doRestart() {
		wandouGamesApi.onRestart(mActivity);
	}
	
	@Override
	public void doNewIntent(Intent intent) {
		wandouGamesApi.onNewIntent(mActivity);
	}
	
	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) {
		wandouGamesApi.onActivityResult(mActivity, requestCode, resultCode, data);
	}
	
}
