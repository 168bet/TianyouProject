package com.tianyou.channel.business;

import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;

import android.app.Activity;
import android.content.Context;

public class KakaoSdkService extends BaseSdkService {

	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
        KakaoSDK.init(new KakaoAdapter() {
			@Override
			public IApplicationConfig getApplicationConfig() {
				return null;
			}
		});
	}
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
	    Session.getCurrentSession().open(AuthType.KAKAO_TALK, mActivity);
	}
}
