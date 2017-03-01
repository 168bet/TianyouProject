package com.tianyou.sdk.holder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
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
	public void openProgressDialog(Activity activity) {
		mProgressDialog = new ProgressDialog(activity);
		mProgressDialog.setMessage("正在登陆...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}
	
	//关闭进度条对话框
	public void closeProgressDialog() {
		if (mProgressDialog != null) mProgressDialog.dismiss();
	}
	
}
