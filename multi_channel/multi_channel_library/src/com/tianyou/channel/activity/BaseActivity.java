//package com.tianyou.channel.activity;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.baidu.gamesdk.ActivityAdPage;
//import com.baidu.gamesdk.ActivityAdPage.Listener;
//import com.baidu.gamesdk.ActivityAnalytics;
//import com.baidu.gamesdk.BDGameSDK;
//
//public class BaseActivity extends Activity {
//
//	private ActivityAdPage mActivityAdPage;
//	private ActivityAnalytics mActivityAnalytics;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		init();
//
//	}
//
//	private void init() {
//		mActivityAnalytics = new ActivityAnalytics(this);
//
//		mActivityAdPage = new ActivityAdPage(this, new Listener() {
//
//			@Override
//			public void onClose() {
//				// TODO 关闭暂停页, CP可以让玩家继续游戏
//				Toast.makeText(getApplicationContext(), "继续游戏",
//						Toast.LENGTH_LONG).show();
//			}
//
//		});
//
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		mActivityAdPage.onResume();
//		mActivityAnalytics.onResume();
//		BDGameSDK.onResume(this);
//	}
//
//	@Override
//	protected void onStop() {
//		super.onStop();
//		mActivityAdPage.onStop();
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		mActivityAdPage.onPause();
//		mActivityAnalytics.onPause();
//		BDGameSDK.onPause(this);
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		mActivityAdPage.onDestroy();
//	}
//
//}
