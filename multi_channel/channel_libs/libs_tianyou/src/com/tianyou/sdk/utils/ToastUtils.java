package com.tianyou.sdk.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

	public static void show(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

//	public static void showDialog(Context context, String msg) {
//		AlertDialog.Builder builder = new Builder(context);
//		builder.setMessage(msg);
//		builder.setTitle("提示");
//		builder.setPositiveButton("确认", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int arg1) {
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}
}
