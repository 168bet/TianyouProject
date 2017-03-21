package com.tianyou.channel.business;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import cn.m4399.operate.OperateCenter;
import cn.m4399.operate.OperateCenter.OnInitGloabListener;
import cn.m4399.operate.OperateCenter.OnLoginFinishedListener;
import cn.m4399.operate.OperateCenter.OnQuitGameListener;
import cn.m4399.operate.OperateCenter.OnRechargeFinishedListener;
import cn.m4399.operate.OperateCenterConfig;
import cn.m4399.operate.OperateCenterConfig.PopLogoStyle;
import cn.m4399.operate.OperateCenterConfig.PopWinPosition;
import cn.m4399.operate.User;

import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;

public class M4399SdkService extends BaseSdkService {

	private OperateCenter mOpeCenter;
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		mOpeCenter = OperateCenter.getInstance();
		OperateCenterConfig opeConfig = new OperateCenterConfig.Builder(mActivity)
			.setGameKey(mChannelInfo.getAppId())
		    .setDebugEnabled(false)
		    .setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
			.setPopLogoStyle(PopLogoStyle.POPLOGOSTYLE_ONE)
			.setPopWinPosition(PopWinPosition.POS_LEFT)
			.setSupportExcess(true)
			.build();
		mOpeCenter.setConfig(opeConfig);
		mOpeCenter.init(mActivity, new OnInitGloabListener() {
			@Override
			public void onUserAccountLogout(boolean arg0, int arg1) {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, arg1 + "");
			}
			
			@Override
			public void onSwitchUserAccountFinished(User arg0) {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, arg0.toString());
			}
			
			@Override
			public void onInitFinished(boolean flag, User arg1) {
				mTianyouCallback.onResult(TianyouCallback.CODE_INIT, arg1.toString());
//				if (flag) {
//					mTianyouCallback.onResult(TianyouCallback.CODE_INIT, arg1.toString());
//				} else {
//					ToastUtils.showToast(mActivity, "初始化失败");
//				}
			}
		});
	}
	
	@Override
	public void doLogin() {
		mOpeCenter.login(mActivity, new OnLoginFinishedListener() {
			@Override
			public void onLoginFinished(boolean success, int code, User user) {
				LogUtils.d("success:" + success + ",code：" + code);
				if (success) {
					checkLogin(user.getUid(), user.getState());
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "code:" + code);
				}
			}
		});
	}
	
	@Override
	public void doExitGame() {
		mOpeCenter.shouldQuitGame(mActivity, new OnQuitGameListener() {
			@Override
			public void onQuitGame(boolean arg0) {
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "");
			}
		});
	}
	
	@Override
	public void doChannelPay(final OrderinfoBean orderInfo) {
		mOpeCenter.recharge(mActivity, Integer.parseInt(mPayInfo.getMoney()), 
				orderInfo.getOrderID(), mPayInfo.getProductName(), new OnRechargeFinishedListener() {
			@Override
			public void onRechargeFinished(boolean success, int code, String msg) {
				LogUtils.d("success:" + success + ",code:" + code + ",msg:" + msg);
				if (success) {
					checkOrder(orderInfo.getOrderID());
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "");
				}
			}
		});
	}
	
}
