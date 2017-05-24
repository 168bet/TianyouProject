package com.tianyou.sdk.utils;

import com.tianyou.sdk.holder.ConfigHolder;

import android.util.Log;

/**
 * Created by itstrong on 2016/7/1.
 */
public class LogUtils {

	private static final String TAG = "tianyou";

	public static void d(String msg) { if (ConfigHolder.isOpenLog) Log.d(TAG, "tianyou_" + msg); }

	public static void w(String msg) { if (ConfigHolder.isOpenLog) Log.w(TAG, "tianyou_" + msg); }

	public static void e(String msg) { if (ConfigHolder.isOpenLog) Log.e(TAG, "tianyou_" + msg); }
}
