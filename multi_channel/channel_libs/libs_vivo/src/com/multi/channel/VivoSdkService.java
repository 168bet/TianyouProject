package com.multi.channel;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;
import com.vivo.unionsdk.open.VivoAccountCallback;
import com.vivo.unionsdk.open.VivoExitCallback;
import com.vivo.unionsdk.open.VivoPayCallback;
import com.vivo.unionsdk.open.VivoPayInfo;
import com.vivo.unionsdk.open.VivoRoleInfo;
import com.vivo.unionsdk.open.VivoUnionSDK;

public class VivoSdkService extends BaseSdkService {

	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);

		VivoUnionSDK.initSdk(context, mChannelInfo.getAppId(), false);

	}

	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);

		VivoUnionSDK.registerAccountCallback(activity,
				new VivoAccountCallback() {

					@Override
					public void onVivoAccountLogin(String userName,
							String openId, String authToken) {
						// 登陆成功操作
						mLoginInfo.setChannelUserId(openId);
						mLoginInfo.setUserToken(authToken);
						checkLogin();
					}

					@Override
					public void onVivoAccountLogout(int requestCode) {
						// 注销登录操作
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT,
								"");
					}

					@Override
					public void onVivoAccountLoginCancel() {
						// 登录取消操作
						mTianyouCallback.onResult(
								TianyouCallback.CODE_LOGIN_CANCEL, "");
					}
				});
	}

	@Override
	public void doLogin() {
		super.doLogin();
		VivoUnionSDK.login(mActivity);
	}

	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		VivoUnionSDK.reportRoleInfo(new VivoRoleInfo(roleInfo.getRoleId(),
				roleInfo.getRoleLevel(), roleInfo.getRoleName(), roleInfo
						.getServerId(), roleInfo.getServerName()));
	}

	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);

		VivoPayInfo vivoPayInfo = new VivoPayInfo(orderInfo.getProduct_name(),
				orderInfo.getProductDesc(), payInfo.getAmount(),
				orderInfo.getAccessKey(), orderInfo.getAppID(),
				orderInfo.getOrderNumber(), mLoginInfo.getChannelUserId());
		
		Log.d("tianyou", vivoPayInfo.toString());
		
		VivoUnionSDK.pay(mActivity, vivoPayInfo, new VivoPayCallback() {

			@Override
			public void onVivoPayResult(String transNo, boolean isSucc,
					String errorCode) {
				LogUtils.d(errorCode);
			}
		});
	}

	@Override
	public void doExitGame() {
		super.doExitGame();
		VivoUnionSDK.exit(mActivity, new VivoExitCallback() {

			@Override
			public void onExitConfirm() {
				mTianyouCallback
						.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "");
			}

			@Override
			public void onExitCancel() {
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "");

			}
		});
	}
}
