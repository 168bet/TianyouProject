package com.tianyou.channel.business;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import cn.egame.terminal.paysdk.EgameExitListener;
import cn.egame.terminal.paysdk.EgamePay;
import cn.egame.terminal.paysdk.EgamePayListener;

import com.tianyou.channel.bean.OrderInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;

import egame.terminal.usersdk.CallBackListener;
import egame.terminal.usersdk.EgameUser;

public class AyxSdkService extends BaseSdkService {

	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		EgamePay.init(activity);
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "");
	}
	
	@Override
	public void doLogin() {
		EgameUser.start(mActivity, Integer.parseInt(mChannelInfo.getCpId()), new CallBackListener() {
            @Override
            public void onSuccess(String code) {
            	LogUtils.d("code:" + code);
            	checkLogin("", code);
            }

            @Override
            public void onFailed(int code) {
            	mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "code:" + code);
            }

            @Override
            public void onCancel() {
            	mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "");
            }
        });
	}
	
	@Override
	public void doResume() {
		EgameUser.onResume(mActivity);
	}
	
	@Override
	public void doPause() {
		EgameUser.onPause(mActivity);
	}
	
	@Override
	public void doExitGame() {
		EgamePay.exit(mActivity, new EgameExitListener() {
			@Override
			public void exit() {
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "");
			}

			@Override
			public void cancel() {
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "");
			}
		});
	}
	
	@Override
	public void doChannelPay(OrderinfoBean orderInfo) {
		final String orderId = orderInfo.getOrderID();
		HashMap<String, String> payParams = new HashMap<String, String>();
		payParams.put(EgamePay.PAY_PARAMS_KEY_TOOLS_PRICE, orderInfo.getMoNey());
		payParams.put(EgamePay.PAY_PARAMS_KEY_CP_PARAMS, orderId);
		payParams.put(EgamePay.PAY_PARAMS_KEY_USE_SMSPAY, "false");
		EgamePay.pay(mActivity, payParams,new EgamePayListener() {
			@Override
			public void paySuccess(Map<String, String> params) {
				checkOrder(orderId);
			}
			
			@Override
			public void payFailed(Map<String, String> params, int errorInt) {
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, errorInt + params.toString());
			}
			
			@Override
			public void payCancel(Map<String, String> params) {
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, params.toString());
			}
		});
	}
	
	
}
