package com.tianyou.channel.business;

import android.app.Activity;
import android.util.Log;

import com.lenovo.lsf.gamesdk.GamePayRequest;
import com.lenovo.lsf.gamesdk.IAuthResult;
import com.lenovo.lsf.gamesdk.IPayResult;
import com.lenovo.lsf.gamesdk.LenovoGameApi;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;

public class LenovoSdkService extends BaseSdkService {
	
	private String appID;
	private String payPrivate;
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		appID = mChannelInfo.getAppId();
		payPrivate = mChannelInfo.getPayRsaPrivate();
		LenovoGameApi.doInit(mActivity, appID);
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "");
	}
	
	@Override
	public void doLogin() {
		LenovoGameApi.doAutoLogin(mActivity, new IAuthResult() {
			@Override
			public void onFinished(boolean ret, String data) {
				Log.d("TAG", "ret= " + ret + ",data= " + data);
				if (ret) {
					LoginInfo loginParam = new LoginInfo();
					loginParam.setChannelUserId("");
					loginParam.setUserToken(data);
					checkLogin(loginParam);
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
				}
			}
		});
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		GamePayRequest payRequest = new GamePayRequest();
		payRequest.addParam("appid", appID);
		payRequest.addParam("notifyurl", orderInfo.getNotifyurl());
		payRequest.addParam("waresid", orderInfo.getWaresid());
		payRequest.addParam("exorderno", orderInfo.getOrderID());
		payRequest.addParam("price", Integer.parseInt(orderInfo.getMoNey()));
		payRequest.addParam("cpprivateinfo", payInfo.getCustomInfo());
		LenovoGameApi.doPay(mActivity, payPrivate, payRequest, new IPayResult() {
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
	
	@Override
	public boolean isShowExitGame() { return true; }

}
