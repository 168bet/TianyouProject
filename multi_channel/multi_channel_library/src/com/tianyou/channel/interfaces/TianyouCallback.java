package com.tianyou.channel.interfaces;

public interface TianyouCallback {

	public static final int CODE_INIT = 0;
	
	public static final int CODE_LOGIN_SUCCESS = 1;	// 登录成功
	public static final int CODE_LOGIN_FAILED = 2;	// 登录失败
	public static final int CODE_LOGIN_CANCEL = 3;	// 登录取消
	
	public static final int CODE_LOGOUT = 4;	// 用户注销账号
	
	public static final int CODE_PAY_SUCCESS = 5;	// 支付成功
	public static final int CODE_PAY_FAILED = 6;	// 支付失败
	public static final int CODE_PAY_CANCEL = 7;	// 支付取消
	
	public static final int CODE_QUIT_SUCCESS = 8;	// 退出游戏成功
	public static final int CODE_QUIT_CANCEL = 9;	// 退出游戏取消
	
	public static final int CODE_VERIFIEDINFO_SUCCESS = 10;
	public static final int CODE_VERIFIEDINFO_FAILED = 11;
	
	/**
	 * sdk操作成功回调
	 * @param code
	 * @param msg
	 */
	public void onResult(int code, String msg);

}
