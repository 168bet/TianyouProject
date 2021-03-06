package com.tianyou.sdk.holder;

/**
 * 存储游戏及用户信息holder
 * @author itstrong
 * 
 */
public class ConfigHolder {

	// 游戏是否是横屏
	public static boolean isLandscape;
	
	// 游戏是否是横屏
	public static String oldPassword;
	// 域名地址
	public static String hostAddress;
	
	// 游戏是游客登录
	public static boolean isTourist;
	// 是否实名认证
	public static boolean isAuth;
	// 账号是否绑定手机
	public static boolean isPhone;
	
	// 是否是海外包
	public static boolean isOverseas;
	// 是否打开日志
	public static boolean isOpenLog = true;
	// 是否是工会模式
	public static boolean isUnion;
	// sdk版本
	public static String sdkVersion;

	// 存储游戏id
	public static String gameId;
	// 存储游戏token
	public static String gameToken;
	// 存储游戏名
	public static String gameName;

	// 存储渠道id
	public static String channelId;
	// 存储微信appId
	public static String wechatAppId;

	// 存储用户是否已登陆 */
	public static boolean userIsLogin;
	// 存储用户id
	public static String userId;
	// 存储用户名
	public static String userName;
	// 存储用户昵称
	public static String userNickname;
	// 存储用户密码
	public static String userPassword;
	// 存储用户token
	public static String userToken;
	// 存储用户验证码
	public static String userCode;
	// 存储用户绑定手机号
	public static String userPhone;
	// 存储是否通知游戏登陆成功
	public static boolean isNoticeGame;
	
}