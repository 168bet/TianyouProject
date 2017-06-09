package com.tianyou.sdk.demo;

import com.tianyou.sdk.interfaces.TianyouSdk;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		String gameId = "34";
		String gameToken = "e369853df766fa44e1ed0ff613f563bd";
		String gameName = "Test";
		/**
		 * gameId：app唯一标识，非常重要，请认真填写，确保正确
		 * gameToken：appkey
		 * gameName: 游戏名
		 * isLandscape：游戏横屏为true，竖屏为false
		 */
		TianyouSdk.getInstance().applicationInit(this, gameId, gameToken, gameName, true);
	}
}