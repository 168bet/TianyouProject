package com.tianyou.sdk.activity;

import com.tianyou.sdk.utils.ResUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(ResUtils.getResById(this, "activity_splash", "layout"));
		PushAgent.getInstance(this).onAppStart();
		View mViewLogo = findViewById(ResUtils.getResById(this, "img_splash_logo", "id"));
		View mViewText = findViewById(ResUtils.getResById(this, "img_splash_text", "id"));
		
		ObjectAnimator anim1 = ObjectAnimator.ofFloat(mViewLogo, "translationY", mViewLogo.getTranslationY() - 500, mViewLogo.getTranslationY());
		ObjectAnimator anim2 = ObjectAnimator.ofFloat(mViewText, "translationY", mViewText.getTranslationY() + 500, mViewText.getTranslationY());
		ObjectAnimator anim3 = ObjectAnimator.ofFloat(mViewLogo, "alpha", 0.0f, 1.0F);
		ObjectAnimator anim4 = ObjectAnimator.ofFloat(mViewText, "alpha", 0.0f, 1.0F);
		
		AnimatorSet set = new AnimatorSet();
		set.play(anim1).with(anim2).with(anim3).with(anim4);
		set.setDuration(1000);
		set.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator arg0) { }
			
			@Override
			public void onAnimationRepeat(Animator arg0) { }
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						try {
							Class<?> mainClass = Class.forName(getResources().getString
									(ResUtils.getResById(getApplicationContext(), "ty_main_activity", "string")));
							startActivity(new Intent(getApplicationContext(), mainClass));
							finish();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (NotFoundException e) {
							e.printStackTrace();
						}
					}
				}, 1500);
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) { }
		});
		set.start();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// 友盟统计
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// 友盟统计
		MobclickAgent.onPause(this);
	}
}
