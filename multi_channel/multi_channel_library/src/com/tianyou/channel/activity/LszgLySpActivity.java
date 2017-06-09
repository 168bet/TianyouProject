//package com.tianyou.channel.activity;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.PersistableBundle;
//import android.view.Window;
//import android.view.WindowManager;
//
//import com.moge.sdk.mg.MogeSplashActivity;
//import com.tianyou.channel.utils.ResUtils;
//
//public class LszgLySpActivity extends MogeSplashActivity{
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setContentView(ResUtils.getResById(this, "activity_splash", "layout"));
//	}
//	
//	@Override
//	public void onFinish() {
//		Class<?> mainClass;
//		try {
//			mainClass = Class.forName("org.cocos2dx.lua.AppActivity");
//			Intent intent = new Intent(LszgLySpActivity.this, mainClass);
//			startActivity(intent);
//			finish();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
//	
//
//}
