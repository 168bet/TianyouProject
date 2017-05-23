package com.tianyou.channel.utils;

import android.util.Log;

/**
 * Created by itstrong on 2016/7/1.
 */
public class LogUtils {

	private static final String TAG = "tianyouxi";
	private static boolean isOpen = true;

	public static void d(String msg) {
		if (isOpen) { Log.d(TAG, "tianyou_" + msg); }
	}

	public static void w(String msg) {
		if (isOpen) { Log.w(TAG, "tianyou_" + msg); }
	}

	public static void e(String msg) {
		if (isOpen) { Log.e(TAG, "tianyou_" + msg); }
	}
}
