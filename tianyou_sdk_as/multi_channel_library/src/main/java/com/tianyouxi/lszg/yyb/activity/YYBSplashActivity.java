//package com.tianyouxi.lszg.yyb.activity;
//
//import com.tencent.ysdk.api.YSDKApi;
//import com.tianyou.channel.utils.ResUtils;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.res.Resources.NotFoundException;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//
//public class YYBSplashActivity extends Activity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(ResUtils.getResById(this, "activity_splash", "layout"));
//		Log.d("TAG", "+++++++++++++++++++++++++++++++++");
//		YSDKApi.onCreate(this);
//		YSDKApi.handleIntent(this.getIntent());
//		Log.d("TAG", "---------------------------------");
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Class<?> mainClass = Class.forName("org.cocos2dx.lua.AppActivity");
//					startActivity(new Intent(getApplicationContext(), mainClass));
//					finish();
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				} catch (NotFoundException e) {
//					e.printStackTrace();
//				}
//			}
//		}, 1500);
//	}
//	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		YSDKApi.onResume(this);
//	}
//	
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		YSDKApi.handleIntent(intent);
//		Log.d("TAG", "splash on new intent-------------");
//	}
//	
//}
