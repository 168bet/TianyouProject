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
		// TODO �Ƿ�֧�ֵ�ǰ���
		return true;
	}

	@Override
	public void pay(PayParams data) {
		// TODO ����֧��
		ASYXSDK.getInstance().pay(context, data);
	}

}
