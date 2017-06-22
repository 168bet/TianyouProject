package com.tianyou.sdk.demo;

import com.tianyou.sdk.interfaces.TianyouSdk;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		String gameId = "ty_20029";
		String gameToken = "ac46aa91df4c59d2c927d024456ff402";
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