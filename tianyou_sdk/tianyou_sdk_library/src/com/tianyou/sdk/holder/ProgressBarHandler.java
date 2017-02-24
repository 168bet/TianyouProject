package com.tianyou.sdk.holder;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * 进度条处理器
 * @author itstrong
 *
 */
public class ProgressBarHandler {

	private static ProgressBarHandler mProgressBarHandler;
	private static ProgressBar mProgressBar;
	
	private ProgressBarHandler() { }
	
	public static ProgressBarHandler getInstance() {
		if (mProgressBarHandler == null) {
			mProgressBarHandler = new ProgressBarHandler();
		}
		return mProgressBarHandler;
	}
	
//	public void delayed(Activity activity, int time) {
//		LogUtils.d("delayed start");
//		if (progressBar == null) {
//			LogUtils.d("delayed ProgressBar为空");
//			progressBar = new ProgressBar(activity);
//			RelativeLayout layout = new RelativeLayout(activity);
//			layout.addView(progressBar);
//			layout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
//			activity.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//		}
//		progressBar.setVisibility(View.VISIBLE);
//		LogUtils.d("delayed time：" + time);
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				LogUtils.d("hide delayed");
//				progressBar.setVisibility(View.GONE);
//			}
//		}, time);
//	}
	
	public void open(Activity activity) {
		mProgressBar = new ProgressBar(activity);
		RelativeLayout layout = new RelativeLayout(activity);
		layout.addView(mProgressBar);
		layout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		activity.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	public void close() {
		mProgressBar.setVisibility(View.INVISIBLE);
	}
	
}
