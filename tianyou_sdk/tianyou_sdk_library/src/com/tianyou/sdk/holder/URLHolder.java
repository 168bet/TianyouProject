package com.tianyou.sdk.holder;

public class URLHolder {

	/** ------------------------- 登陆接口 ------------------------- */
	// 注册
	public static final String URL_UNION_REGISTER = getHostAddrs() + "Register/register";
	// 快速注册
	public static final String URL_LOGIN_QUICK = getHostAddrs() + "register/quickregister";
	// 快速注册
	public static final String URL_REGISTER_PHONE = getHostAddrs() + "register/phoneregister";
	// 账号登陆
	public static final String URL_UNION_ACCOUNT_LOGIN = getHostAddrs() + "login/login";
	// 手机登陆
	public static final String URL_UNION_PHONE_LOGIN = getHostAddrs() + "login/phonelogin";
	// 获取验证码
	public static final String URL_GET_CODE = getHostAddrs() + "sms/sendmsg";
	// 验证验证码
	public static final String URL_VERIFY_CODE = getHostAddrs() + "Sms/Verify";
	// 公告
	public static final String URL_UNION_ANNOUNCE = getHostAddrs() + "sdkinfo/getnotice";
	// 绑定手机
	public static final String URL_BINDING_PHONE = getHostAddrs() + "login/bindphone";
	// QQ登录
	public static final String URL_UNION_QQ_LOGIN = getHostAddrs() + "login/qqlogin";
	// 谷歌登陆
	public static final String URL_PAY_GOOGLE = getHostAddrs() + "Google/googlesdk";
	// Facebook登陆
	public static final String URL_PAY_FACEBOOK = getHostAddrs() + "Facebook/fbSdk";
	// QQ WebView URL
	public static final String URL_QQ_WEB = "https://xui.ptlogin2.qq.com/cgi-bin/xlogin?appid=716027609&pt_3rd_aid=101322155&daid=383&pt_skey_valid=1&style=35&s_url=http%3A%2F%2Fconnect.qq.com&refer_cgi=authorize&which=&auth_time=1470121319621&client_id=101322155&src=1&state=&response_type=token&scope=add_share%2Cadd_topic%2Clist_album%2Cupload_pic%2Cget_simple_userinfo&redirect_uri=auth%3A%2F%2Ftauth.qq.com%2F";
	// 一键登录
	public static final String URL_KEY_LOGIN = getHostAddrs() + "login/keylogin";
	// 忘记密码
	public static final String URL_FORGET_PASS = getHostAddrs() + "commonality/getBackPassword";
	// 身份认证
	public static final String URL_IDENTIFI = getHostAddrs() + "member/upuserauth";
	// 手机账号升级
	public static final String URL_UPGRADE_PHONE = getHostAddrs() + "member/upgradeuserbyphone";
	// 用户名账号升级
	public static final String URL_UPGRADE_ACCOUNT = getHostAddrs() + "member/upgradeuserbyname";
	// 验证是否绑定手机号
	public static final String URL_CHECK_PHONE = getHostAddrs() + "member/checkmemberphone";
	// 验证是否绑定手机号
	public static final String URL_IMG_VERIFI = getHostAddrs() + "verify/verify";
	// 用户注册
	public static final String URL_USER_REGISTER = getHostAddrs() + "Register/registerVerify";
	// 手机号修改密码
	public static final String URL_ALERT_PHONE = getHostAddrs() + "login/findpassbyphone";
	// 原密码修改密码
	public static final String URL_ALERT_ACCOUNT = getHostAddrs() + "member/updatememberusername";
	// 登陆客服信息
	public static final String URL_LOGIN_SERVER_INFO = getHostAddrs() + "Toconfigure/Representations";
	// 获取登陆方式
	public static final String URL_LOGIN_WAY = getHostAddrs() + "sdkinfo/getloginswitch";
		
	/** ------------------------- 支付接口 ------------------------- */
	// 创建订单
	public static final String URL_CREATE_ORDER = getHostAddrs() + "order/creatorder";
	// 钱包支付
	public static final String URL_PAY_WALLET = getHostAddrs() + "walletorder/creatorder";
	// 查询订单
	public static final String URL_QUERY_ORDER = getHostAddrs() + "order/getorderstatus";
	// 客服信息
	public static final String URL_SERVER_INFO = getHostAddrs() + "sdkinfo/getcustom";
	// 支付方式控制
	public static final String URL_PAY_WAY = getHostAddrs() + "sdkinfo/getpaytype";
	// 悬浮窗控制
	public static final String URL_FLOAT_CONTROL = getHostAddrs() + "sdkinfo/getframe";
	// PayPal支付查单
	public static final String URL_CHECK_PAYPAL = getHostAddrs() + "Payorder/paypalupdateorder";
	// Google支付查单
	public static final String URL_CHECK_GOOGLE = getHostAddrs() + "Payorder/googleupdateorder";
	// 支付宝回调地址
	public static final String URL_NOTIFY_ALIPAY = "http://www.tianyouxi.com/tianyousdk/index.php/Home/AliPay/notifyurl";
	// 微信回调地址
	public static final String URL_NOTIFY_WECHAT = "http://www.tianyouxi.com/tianyousdk/weixin/notify.php";
	
	/** ------------------------- 其他接口 ------------------------- */
	// 创建角色
	public static final String URL_UNION_CREATE_ROLE = getHostAddrs() + "role/createrole";
	// 更新角色信息
	public static final String URL_UNION_UPDATE_ROLE = getHostAddrs() + "role/updaterole";
	// 完善用户信息
	public static final String URL_UNION_PERFECT = getHostAddrs() + "member/updateuser";
	// 获取金额数值
	public static final String URL_MONEY_VALUE = getHostAddrs() + "walletorder/getpaymoney";
	// 钱包余额
	public static final String URL_WALLET_REMAIN = getHostAddrs() + "walletorder/getwallet";
	// 游戏推荐
	public static final String URL_GAME_RECOMMEND = getHostAddrs() + "sdkinfo/getgames";
	// 获取汇款二维码
	public static final String URL_GET_REMIT_CODE = "http://www.tianyouxi.com/Public/tyx/shewm.png";
	// 去品台支付
	public static final String URL_PAY_ONPLAT = "http://www.tianyouxi.com/svnonethink/mobilesdk.php?s=/CheckSdk/sdkpayCentre&";
	
	public static final String URL_USER_AGREEMENT = getHostAddrs() + "sdkinfo/getagreement";
	
	private static String getHostAddrs() {
		return ConfigHolder.hostAddress;
	}
}
