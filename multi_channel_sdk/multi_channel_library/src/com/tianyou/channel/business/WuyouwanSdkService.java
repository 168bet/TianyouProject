package com.tianyou.channel.business;

import android.app.Activity;

import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.ToastUtils;
import com.wuyouwan.callback.HttpDataCallBack;
import com.wuyouwan.callback.InitCallBack;
import com.wuyouwan.callback.MemberLoginCallBack;
import com.wuyouwan.callback.MemberPayCallBack;
import com.wuyouwan.core.SDKInstace;
import com.wuyouwan.entity.PayOrderModel;

public class WuyouwanSdkService extends BaseSdkService {

	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		int platformNumber = Integer.parseInt(mChannelInfo.getPlatformId());
		SDKInstace.SDKInitialize(mActivity, platformNumber, new InitCallBack() {
			@Override
			public void InitSuccess(int arg0) {
				mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
			}

			@Override
			public void InitFail(int arg0) { ToastUtils.showToast(mActivity, "初始化失败：" + arg0); }
		});
	}

	@Override
	public void doLogin() {
		super.doLogin();
		SDKInstace.MemberRegLoginPanel(true, new MemberLoginCallBack() {
			@Override
			public void Success(long uid, String token) {
				LoginInfo param = new LoginInfo();
				param.setChannelUserId(uid + "");
				param.setUserToken(token);
				checkLogin(param);
			}

			@Override
			public void Fail(String arg0) {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
			}

			@Override
			public void Canel() {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "登录取消");
			}
		});
	}
	
	@Override
	public void doEntryGame() {
		super.doEntryGame();
		SDKInstace.InGame(mRoleInfo.getServerId(), mRoleInfo.getServerName(), 
				mRoleInfo.getServerId(), mRoleInfo.getRoleId(), mRoleInfo.getRoleName(), new HttpDataCallBack() {
			@Override
			public void HttpSuccess(String arg0) { LogUtils.d("SDKInstace.InGame：" + arg0); }

			@Override
			public void HttpFail(int arg0) { }
		});
	}
	
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUpdateRoleInfo(roleInfo);
		SDKInstace.MemberLevelUp(roleInfo.getServerId(), roleInfo.getRoleName(), 
				Integer.parseInt(roleInfo.getRoleLevel()), new HttpDataCallBack() {
			@Override
			public void HttpSuccess(String arg0) { LogUtils.d("SDKInstace.MemberLevelUp：" + arg0); }

			@Override
			public void HttpFail(int arg0) { }
		});
	}

	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		int money = Integer.parseInt(orderInfo.getMoNey());
		int rate = Integer.parseInt(orderInfo.getRate());
		LogUtils.d("doChannelPay:" + money);
		LogUtils.d("doChannelPay:" + rate);
		LogUtils.d("doChannelPay:" + money * rate);
		LogUtils.d("doChannelPay:" + mRoleInfo.getServerId());
		SDKInstace.PayOperatePanel(orderInfo.getOrderID(), mRoleInfo.getServerId(), 
				money, money * rate, orderInfo.getCurrency(), false, rate, new MemberPayCallBack() {
			@Override
			public void PaySuccess(PayOrderModel model) {
				checkOrder(model.getOutPayNo());
			}
		});
	}
	
	@Override
	public void doLogout() {
		super.doLogout();
		SDKInstace.MemberLogout();
	}

}
