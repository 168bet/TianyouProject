package com.tianyou.channel.business;

import java.util.HashMap;
import java.util.Map;

import com.gamebegin.sdk.GBLanguage;
import com.gamebegin.sdk.GBListener;
import com.gamebegin.sdk.GameBegin;
import com.gamebegin.sdk.SDKStatsExtra;
import com.gamebegin.sdk.ui.ExitDialogListener;
import com.gamebegin.sdk.webdialog.ChargeDialogListener;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;

import android.app.Activity;
import android.content.Intent;

public class GameBeginSdkService extends BaseSdkService {

	private GameBegin gameBegin;

	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		boolean isDebug = true; //修改测试环境地址
	    boolean printLog = true; //修改控制台SDK log输出
		gameBegin = GameBegin.getInstance(mActivity, mChannelInfo.getAppId(), 
				mChannelInfo.getAppToken(), GBLanguage.ZH_CN, isDebug, printLog);
	    //设置注销监听器
	    gameBegin.setLogoutListener(new GBListener() {
	        @Override
	        public void afterLogout() {
	        	mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "");
	        }
	    });
	    //初始化退出弹窗
	    GameBegin.initExitDialog("退出", "确定退出？", "确定", new ExitDialogListener() {
	        @Override
	        public void onClick() {
	        	mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "");
	        }
	    }, "取消", new ExitDialogListener() {
	        @Override
	        public void onClick() {
	        	mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "");
	        }
	    });
	    gameBegin.floatMenuOn(100, 100);
	    mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "");
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		gameBegin.openLogin(mActivity, new GBListener() {
		    @Override
		    public void afterLogin(int uid, String username, String token) {
		    	mLoginInfo.setChannelUserId(uid + "");
		    	mLoginInfo.setUserToken(token);
		    	mLoginInfo.setIsOverseas(true);
		    	checkLogin();
		    }
		});
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		gameBegin.charge(orderInfo.getProductId(), 1, orderInfo.getCustomInfo(), new ChargeDialogListener() {
		    @Override
		    public void afterClose(String result) {
		    	checkOrder(orderInfo.getOrderID());
		    }
		});
	}
	
	@Override
	public void doEntryGame() {
		super.doEntryGame();
		Map<String, String> playMap = new HashMap<String, String>();
		playMap.put(SDKStatsExtra.SERVER_ID, mRoleInfo.getServerId());
		playMap.put(SDKStatsExtra.ROLE_ID, mRoleInfo.getRoleId());
		playMap.put(SDKStatsExtra.ROLE_NAME, mRoleInfo.getRoleName());
		playMap.put(SDKStatsExtra.ROLE_LEVEL, mRoleInfo.getRoleLevel());
		playMap.put(SDKStatsExtra.GAME_COIN, mRoleInfo.getBalance());
		playMap.put(SDKStatsExtra.TOPUP_MONEY, mRoleInfo.getBalance());
		gameBegin.play(mActivity, playMap);
	}
	
	@Override
	public void doCreateRole(RoleInfo roleInfo) {
		super.doCreateRole(roleInfo);
		gameBegin.trackCreation(roleInfo.getProfession());
	}
	
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUpdateRoleInfo(roleInfo);
		gameBegin.trackUpdate("3.2.1");
	}
	
	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) {
		super.doActivityResult(requestCode, resultCode, data);
		GameBegin.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void doStart() {
		super.doStart();
		GameBegin.onStart(mActivity);
	}
	
	@Override
	public void doResume() {
		super.doResume();
		GameBegin.onResume(mActivity);
	}
	
	@Override
	public void doPause() {
		super.doPause();
		GameBegin.onPause(mActivity);
	}
	
	@Override
	public void doStop() {
		super.doStop();
		GameBegin.onStop(mActivity);
	}
	
	@Override
	public void doDestory() {
		super.doDestory();
		GameBegin.onDestroy(mActivity);
	}
}
