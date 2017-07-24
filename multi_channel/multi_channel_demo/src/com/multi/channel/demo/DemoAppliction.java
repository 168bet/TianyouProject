package com.multi.channel.demo;

import com.tianyou.channel.activity.TianyouApplication;
import com.tianyou.channel.interfaces.TianyouSdk;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

public class DemoAppliction extends TianyouApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		// 第二个参数代表横竖屏，true为横屏，false为竖屏
		TianyouSdk.getInstance(getApplicationContext()).doApplicationCreate(getApplicationContext(), true);
	}
	
	@Override
	public void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		TianyouSdk.getInstance(base).doApplicationAttach(base);
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		TianyouSdk.getInstance(getApplicationContext()).doApplicationTerminate();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		TianyouSdk.getInstance(getApplicationContext()).doApplicationConfigurationChanged(this, newConfig);
	}
}
