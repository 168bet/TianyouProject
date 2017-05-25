package com.tianyou.channel.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

	public static void show(Context context, final String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void show(final Activity activity, final String msg) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
