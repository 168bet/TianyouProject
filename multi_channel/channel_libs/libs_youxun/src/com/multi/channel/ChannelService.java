package com.multi.channel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.youxun.sdk.app.YouxunProxy;
import com.youxun.sdk.app.YouxunXF;
import com.youxun.sdk.app.model.MessageEvent;

import android.app.Activity;
import android.content.Intent;

public class ChannelService extends BaseSdkService {

	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		EventBus.getDefault().register(this);
		YouxunProxy.init(mChannelInfo.getAppId(), mChannelInfo.getAppKey());
		YouxunXF.onCreate(mActivity, 0.4f);
		doNoticeGame(TianyouCallback.CODE_INIT, "");
	}

	@Override
	public void doLogin() {
		super.doLogin();
		YouxunProxy.startLogin(mActivity);
	}

	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		YouxunProxy.uploadRole(mActivity, roleInfo.getServerId(), roleInfo.getRoleName(), 
				roleInfo.getRoleLevel(), roleInfo.getCreateTime(), roleInfo.getRoleId(), roleInfo.getServerName());
	}

	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		YouxunProxy.startPay(mActivity, orderInfo.getProductName(), 
				orderInfo.getMoNey(), orderInfo.getOrderID(), orderInfo.getServerID());
	}

	@Subscribe
	public void msgEventBus(MessageEvent message) {
		int code = message.getCode();
		Intent data = message.getIntent();
		if (code == 0) {
			if (data.getStringExtra("data").equals("success")) {
				String userid = data.getStringExtra("userid");
				mLoginInfo.setChannelUserId(userid);
				checkLogin();
				YouxunProxy.updateDialog(mActivity, data);
				YouxunXF.hintUserInfo(mActivity);
			} else {
				doNoticeGame(TianyouCallback.CODE_LOGIN_FAILED, "");
			}
		}

		if (code == 1) {
			if (data.getStringExtra("data").equals("success")) {
				checkOrder(mPayInfo.getOrderId());
			} else {
				doNoticeGame(TianyouCallback.CODE_PAY_FAILED, "");
			}
		}

		if (code == 2) {
			doNoticeGame(TianyouCallback.CODE_LOGOUT, "");
			YouxunProxy.startLogin(mActivity);
		}
	}

	@Override
	public void doExitGame() {
		super.doExitGame();
		YouxunProxy.exitLogin(mActivity);
	}

	@Override
	public void doDestory() {
		super.doDestory();
		YouxunXF.onDestroy();
	}
}
