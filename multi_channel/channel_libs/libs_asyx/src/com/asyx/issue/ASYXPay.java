package com.asyx.issue;

import android.app.Activity;

import com.u8.sdk.IPay;
import com.u8.sdk.PayParams;

public class ASYXPay implements IPay {
	private Activity context;

	public ASYXPay(Activity context) {
		this.context = context;
	}

	@Override
	public boolean isSupportMethod(String methodName) {
		// TODO 是否支持当前插件
		return true;
	}

	@Override
	public void pay(PayParams data) {
		// TODO 发起支付
		ASYXSDK.getInstance().pay(context, data);
	}

}
