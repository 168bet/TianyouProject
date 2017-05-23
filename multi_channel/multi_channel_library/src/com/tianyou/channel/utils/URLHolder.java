package com.tianyou.channel.utils;

public class URLHolder {

	// 域名地址
	public static String URL_BASE = "http://channel.tianyouxi.com"; // 国内正式地址
	public static String URL_TSET = "http://192.168.1.169/morechannel/index.php"; // 本地测试地址
	public static String URL_OVERSEAS = "http://bmchannel.tianyouxi.com/index.php"; // 海外正式地址
	public static String URL_TEST_BASE = "http://testchannel.tianyouxi.com/index.php"; // 国内测试地址

	//登录校验地址
	public static String CHECK_LOGIN_URL = "?c=login&a=MemberLogin";
	public static String CHECK_LOGIN_URL_QIHOO = URL_BASE + "?c=login&a=UserCheck";
	//创建订单地址
	public static String CREATE_ORDER_URL = "?c=Order&a=CreatOrder";
	//校验订单地址
	public static String CHECK_ORDER_URL = "?c=Order&a=SendOrder";
	//one store查单
	public static String URL_ONE_STORE = "?c=OneStore&a=GetPayInfo";
	// google 查单
	public static String CHECK_ORDER_GOOGLE = "http://bmchannel.tianyouxi.com/index.php?s=Korea/GoogleUpdateOrder";

	//校验应用宝订单地址
	public static String CHECK_ORDER_URL_YYB = URL_BASE + "?c=Ysdk&a=GetPayInfo";
	
	// 秒乐猎妖应用宝校验订单地址
	public static String CHECK_ORDER_ML_LY_YYB = "http://channel.tianyouxi.com/index.php/MLyyb/GetPayInfo";
	//获取时间戳
	public static String GET_TIME = "http://channel.tianyouxi.com/index.php/MoreChannel/gettimeinfo";

	//本地创建订单地址
	public static String LOCAL_ORDER = "http://192.168.1.169/morechannel/index.php/Order/CreatOrder";
	public static String UPLOAD_ROLE_INFO = "http://channel.tianyouxi.com/index.php?c=Roleinfo&a=PutRoleInfo";
}
