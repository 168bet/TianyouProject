package com.tianyou.channel.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.snowfish.cn.ganga.helper.SFOnlineSplashActivity;
import com.tianyou.channel.utils.ResUtils;

public class QinjianSpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(ResUtils.getResById(this, "activity_splash", "layout"));
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Class<?> mainClass;
				try {
					mainClass = Class.forName("com.tianyou.mjx.MultiChannelActivity");
					Intent intent = new Intent(QinjianSpActivity.this, mainClass);
					startActivity(intent);
					finish();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}, 1500);
	}
}
