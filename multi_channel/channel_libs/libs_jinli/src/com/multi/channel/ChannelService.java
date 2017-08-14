package com.multi.channel;

import com.gionee.gamesdk.floatwindow.AccountInfo;
import com.gionee.gamesdk.floatwindow.GamePayCallBack;
import com.gionee.gamesdk.floatwindow.GamePayManager;
import com.gionee.gamesdk.floatwindow.GamePlatform;
import com.gionee.gamesdk.floatwindow.GamePlatform.LoginListener;
import com.gionee.gamesdk.floatwindow.QuitGameCallback;
import com.gionee.gsp.GnEFloatingBoxPositionModel;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class ChannelService extends BaseSdkService{
	
	private GamePayManager mGamePayManager;
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		GamePlatform.init((Application)context, mChannelInfo.getAppKey());
	}
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
        GamePlatform.setFloatingBoxOriginPosition(GnEFloatingBoxPositionModel.RIGHT_TOP);
        GamePlatform.requestFloatWindowsPermission(mActivity);
        mGamePayManager = GamePayManager.getInstance();
		doNoticeGame(TianyouCallback.CODE_INIT, "");
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		GamePlatform.loginAccount(mActivity, true, new LoginListener() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
            	mLoginInfo.setChannelUserId(accountInfo.mUserId);
            	mLoginInfo.setUserToken(accountInfo.mToken);
            	mLoginInfo.setPlayId(accountInfo.mPlayerId);
            	checkLogin();
            }

            @Override
            public void onError(Object e) {
            	doNoticeGame(TianyouCallback.CODE_LOGIN_FAILED, "");
            }

            @Override
            public void onCancel() {
            	doNoticeGame(TianyouCallback.CODE_LOGIN_CANCEL, "");
            }
        });
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		mGamePayManager.pay(mActivity, orderInfo.getOrderID(), new GamePayCallBack() {
            @Override
            public void onCreateOrderSuccess(String s) { }

            @Override
            public void onPaySuccess() {
            	checkOrder(orderInfo.getOrderID());
            }

            @Override
            public void onPayFail(Exception e) {
            	doNoticeGame(TianyouCallback.CODE_PAY_FAILED, "");
            }
        });
		
		
		mGamePayManager.pay(mActivity, mOrderInfo, new GamePayCallBack() {

            @Override
            public void onCreateOrderSuccess(String s) {

            }

            @Override
            public void onPaySuccess() {

            }

            @Override
            public void onPayFail(Exception e) {

            }
        });
	}
	
	@Override
	public void doExitGame() {
		super.doExitGame();
		GamePlatform.quitGame(mActivity, new QuitGameCallback() {
            @Override
            public void onQuit() {
            	doNoticeGame(TianyouCallback.CODE_QUIT_SUCCESS, "");
            }

            @Override
            public void onCancel() {
            	doNoticeGame(TianyouCallback.CODE_QUIT_CANCEL, "");
            }
        });
	}
}