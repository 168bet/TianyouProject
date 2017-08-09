package com.multi.channel;

import com.lenovo.lsf.gamesdk.GamePayRequest;
import com.lenovo.lsf.gamesdk.IAuthResult;
import com.lenovo.lsf.gamesdk.IPayResult;
import com.lenovo.lsf.gamesdk.LenovoGameApi;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;

import android.app.Activity;

public class ChannelService extends BaseSdkService{
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		LenovoGameApi.doInit(mActivity, mChannelInfo.getAppId());
		doNoticeGame(TianyouCallback.CODE_INIT, "");
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		LenovoGameApi.doAutoLogin(mActivity, new IAuthResult() {
			@Override
			public void onFinished(boolean ret, String data) {
				LogUtils.d("ret:" + ret + ",data:" + data);
				if (ret) {
					mLoginInfo.setChannelUserId("");
					mLoginInfo.setUserToken(data);
					checkLogin();
				} else {
					doNoticeGame(TianyouCallback.CODE_LOGIN_FAILED, "");
				}
			}
		});
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		GamePayRequest payRequest = new GamePayRequest();
		payRequest.addParam("appid", mChannelInfo.getAppId());
		payRequest.addParam("notifyurl", orderInfo.getNotifyurl());
		payRequest.addParam("waresid", orderInfo.getWaresid());
		payRequest.addParam("exorderno", orderInfo.getOrderID());
		payRequest.addParam("price", Integer.parseInt(orderInfo.getMoNey()));
		payRequest.addParam("cpprivateinfo", payInfo.getCustomInfo());
		LenovoGameApi.doPay(mActivity, mChannelInfo.getAppKey(), payRequest, new IPayResult() {
			@Override
			public void onPayResult(int resultCode, String signValue, String resultInfo) {
				if (resultCode == LenovoGameApi.PAY_SUCCESS) {
					checkOrder(orderInfo.getOrderID());
				} else if (resultCode == LenovoGameApi.PAY_CANCEL) {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "支付取消");
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
				}
			}
		});
	}
	
	@Override
	public void doExitGame() {
		LenovoGameApi.doQuit(mActivity, new IAuthResult() {
			@Override
			public void onFinished(boolean ret, String msg) {
				if (ret) {
					mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "");
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "");
				}
			}
		});
	}
}