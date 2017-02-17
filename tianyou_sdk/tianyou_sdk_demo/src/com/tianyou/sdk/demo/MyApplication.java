package com.tianyou.sdk.demo;

import com.tianyou.sdk.interfaces.Tianyouxi;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		String gameId = "1021";
		String gameToken = "0768281a05da9f27df178b5c39a51263";
		/**
		 * gameId：app唯一标识，非常重要，请认真填写，确保正确
		 * gameToken：appkey
		 * isLandscape：游戏横屏为true，竖屏为false
		 */
		Tianyouxi.init(this, gameId, gameToken, false);
	}
}
 