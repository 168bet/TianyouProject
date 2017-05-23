package com.tianyou.channel.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {

	public static final String LOGIN_TYPE = "login_type";
	
	private static SpUtils spUtils;
	private SharedPreferences sp_login;
	
	private SpUtils(Context context){
		sp_login = context.getSharedPreferences("sp_login_type", Context.MODE_PRIVATE);
	}
	
	public static SpUtils getInstance(Context context){
		if (spUtils == null) spUtils = new SpUtils(context);
		return spUtils;
	}

	public void putInt (String key,int value) {
		sp_login.edit().putInt(key, value).commit();
	}
	
	public int getInt (String key) {
		return sp_login.getInt(key, 0);
	}
	
}
