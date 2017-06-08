package com.tianyou.sdk.holder;

import com.tianyou.sdk.utils.ResUtils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * 进度条、对话框处理器
 * @author itstrong
 *
 */
public class ProgressHandler {

	private static ProgressHandler mProgressHandler;
	private ProgressDialog mProgressDialog;
	
	private ProgressHandler() { }
	
	public static ProgressHandler getInstance() {
		if (mProgressHandler == null) {
			mProgressHandler = new ProgressHandler();
		}
		return mProgressHandler;
	}
	
	//普通进度条
	public void openProgressBar(Activity activity) {
		ProgressBar mProgressBar = new ProgressBar(activity);
		RelativeLayout layout = new RelativeLayout(activity);
		layout.addView(mProgressBar);
		layout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		activity.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	//打开进度条对话框
//	public void openProgressDialog(Activity activity) {
//		mProgressDialog = new ProgressDialog(activity, ResUtils.getResById(activity, "dialog_login_quick", "layout"));
//		mProgressDialog.setMessage(ResUtils.getString(activity, "ty_entry_load"));
//		mProgressDialog.setIndeterminate(true);
//		mProgressDialog.setCancelable(false);
//		mProgressDialog.show();
//	}
	
	//打开进度条对话框
	public void openProgressDialog(Activity activity) {
		LayoutInflater inflater = LayoutInflater.from(activity);
		View v = inflater.inflate(ResUtils.getResById(activity, "dialog_login_loading", "layout"), null);
		ImageView spaceshipImage = (ImageView) v.findViewById(ResUtils.getResById(activity, "img_loading_progress", "id"));
		
		// 加载动画
		ObjectAnimator animator = ObjectAnimator.ofFloat(spaceshipImage, "rotation", 0f, 360f);
		animator.setDuration(1000);
		animator.setRepeatCount(ValueAnimator.INFINITE);
		animator.setRepeatMode(ValueAnimator.INFINITE);
		animator.start();
		
		Dialog loadingDialog = new Dialog(activity, ResUtils.getResById(activity, "style_my_dialog", "style"));
		loadingDialog.setCancelable(false);
		loadingDialog.setContentView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		loadingDialog.show();
	}
	
	//关闭进度条对话框
	public void closeProgressDialog() {
		if (mProgressDialog != null) mProgressDialog.dismiss();
	}
}
