package com.tianyou.channel.utils;

import java.util.UUID;

import android.content.Context;

public class SPHandler {

	private static final String SP_NAME = "sp_name";					//sp_name
	public static final String SP_IMEI = "imei";						//创建IMEI
	
    public static String getIMEI(Context context, String key) {
    	return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getString(key, UUID.randomUUID().toString());
	}
    
    public static void putString(Context context, String key, String value) {
    	context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putString(key, value).commit();
	}
}
