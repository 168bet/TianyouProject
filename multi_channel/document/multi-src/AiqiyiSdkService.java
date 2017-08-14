package com.tianyou.channel.business;

import android.app.Activity;
import android.util.Log;

import com.iqiyi.sdk.listener.LoginListener;
import com.iqiyi.sdk.listener.PayListener;
import com.iqiyi.sdk.platform.GamePlatform;
import com.iqiyi.sdk.platform.GamePlatformInitListener;
import com.iqiyi.sdk.platform.GameSDKResultCode;
import com.iqiyi.sdk.platform.GameUser;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;

public class AiqiyiSdkService extends BaseSdkService{
	
	private GamePlatform mPlatform;
	private String orderID;
	
	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		// 获取sdk实例化对象
		mPlatform = GamePlatform.getInstance();
		// 初始化sdk
		mPlatform.initPlatform(activity, mChannelInfo.getAppId(), new GamePlatformInitListener() {
			@Override
			public void onSuccess() {
				// 添加登录的监听
				mPlatform.addLoginListener(new AiqiyiLoginListener());
				// 添加支付的监听
				mPlatform.addPaymentListener(new AiqiyiPayListener());
				// 初始化sdk成功后的回调
				mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
			}
			@Override
			public void onFail(String msg) {
				// sdk初始化失败的回调
				LogUtils.e("爱奇艺SDK初始化失败: "+msg);
			}
		});
		
	}
	
	// 登录
	@Override
	public void doLogin() {
		// 爱奇艺登录
		mPlatform.iqiyiUserLogin(mActivity);
	}
	
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		mRoleInfo = roleInfo;
		mPlatform.createRole(mActivity, "ppsmobile_s" + mRoleInfo.getServerId());
		mPlatform.enterGame(mActivity, "ppsmobile_s" + mRoleInfo.getServerId());
	}
	
	// 爱奇艺支付
	@Override
	public void doChannelPay(OrderinfoBean orderInfo) {
		orderID = orderInfo.getOrderID();
		mPlatform.iqiyiPayment(mActivity, Integer.parseInt(orderInfo.getMoNey()), mRoleInfo.getRoleId(), 
				"ppsmobile_s"+mRoleInfo.getServerId(), orderID);
	}
	
	@Override
	public void doExitGame() {
		mPlatform.iqiyiUserLogout(mActivity);
	}
	
	// 自定义类实现LoginListenert,处理登录后的逻辑
	private class AiqiyiLoginListener implements LoginListener {

		@Override
		public void exitGame() {
			// 退出游戏的回调
			Log.d("TAG", "exit-------------");
			mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "退出游戏");
		}

		@Override
		public void loginResult(int code, GameUser gameUser) {
			// 登录结束后的回调
			if (code == GameSDKResultCode.SUCCESSLOGIN && gameUser != null) {
				// 用户登录sdk成功后的回调
				mPlatform.initSliderBar(mActivity);
				Log.d("TAG", "uid= "+gameUser.uid+",sign= "+gameUser.sign+",time= "+gameUser.timestamp);
				checkLogin(gameUser.uid, gameUser.sign);
			} 
		}

		@Override
		public void logout() {
			// 登出的回调
			Log.d("TAG", "logout----------------");
			mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "用户注销账号");
		}
		
	}
	
	// 自定义类实现PayListener,处理支付逻辑
	private class AiqiyiPayListener implements PayListener {

		@Override
		public void leavePlatform() {
			Log.d("TAG", "leave platform--------------------");
			mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "支付取消");
		}

		@Override
		public void paymentResult(int code) {
			Log.d("TAG", "paymentResult---------------------");
			if (code == GameSDKResultCode.SUCCESSPAYMENT) {
				Log.d("TAG", "pay success----------------");
				checkOrder(orderID);
				
			} else {
				Log.d("TAG", "pay failed-----------------");
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
			}
			
		}
		
	}
	
}
