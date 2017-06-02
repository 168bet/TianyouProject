package com.tianyou.sdk.holder;

import java.util.UUID;

import android.content.Context;

public class SPHandler {

	private static final String SP_NAME = "sp_name";					//sp_name
	
	public static final String SP_IS_SHOW_KEY = "is_show_key";			//存储是否显示一键登录
	public static final String SP_IMEI = "imei";						//创建IMEI
	public static final String SP_IS_PHONE_LOGIN = "is_phone_login";	//创建IMEI
	public static final String SP_IS_SWITCH_ACCOUNT = "is_switch_account";	//创建IMEI
	public static final String SP_IS_PHONE = "is_phone";	//创建IMEI
	
	public static final String SP_URL_PHONE = "imag_url_phone";		// 客服电话图片
	public static final String SP_URL_QQ = "imag_url_qq";			// 客服qq图片
	public static final String SP_URL_WX = "imag_url_wx";			// 客服微信二维码图片
	
	public static final String SP_TEXT_PHONE = "text_service_phone";	// 客服电话
	public static final String SP_TEXT_QQ = "text_service_qq";	// 客户qq号码
	public static final String SP_TEXT_WX = "text_service_wx";	// 客服微信
	
	public static final String SP_FLOAT_CONTROL = "sp_float_control";	// 悬浮球控制
	public static final String SP_LOGIN_WAY = "sp_login_way";	// 登陆方式
	public static final String SP_PAY_WAY = "sp_pay_way";	// 支付方式控制
	public static final String SP_PAY_MONEY = "sp_pay_money";	// 支付金额
	
//	public static final String SP_TOURIST = "sp_tourist";	// 支付金额
	
    public static String getString(Context context, String key) {
    	return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getString(key, "");
	}
    
    public static void putString(Context context, String key, String value) {
    	context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putString(key, value).commit();
	}
    
    public static boolean getBoolean(Context context, String key) {
    	return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getBoolean(key, false);
	}
    
    public static void putBoolean(Context context, String key, boolean value) {
    	context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
	}
    
    public static String getImeiString(Context context, String key) {
    	return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getString(key, UUID.randomUUID().toString());
	}
}
