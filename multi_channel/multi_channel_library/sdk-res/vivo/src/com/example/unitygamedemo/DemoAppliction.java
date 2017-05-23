package com.example.unitygamedemo;

import com.tianyou.channel.interfaces.TianyouSdk;

import android.app.Application;
import android.content.Context;

public class DemoAppliction extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// 第二个参数代表横竖屏，true为横屏，false为竖屏
		TianyouSdk.getInstance(getApplicationContext()).doApplicationCreate(getApplicationContext(), false);
	}
	
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		TianyouSdk.getInstance(base).doApplicationAttach(base);
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
