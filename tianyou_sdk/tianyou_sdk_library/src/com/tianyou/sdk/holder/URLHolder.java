package com.tianyou.sdk.holder;

public class URLHolder {

	// 国内正式服
//	private static final String URL_BASE = "http://api.tianyouxi.com/index.php";
	// 国内测试服
	private static final String URL_BASE = " http://newsdk.tianyouxi.com/api/";
	// 海外正式服
//	private static final String URL_OVERSEAS = "http://testapi.tianyouxi.com/index.php";
	// 海外测试服
	private static final String URL_OVERSEAS = "http://testapi.tianyouxi.com/api/";
	// 工会正式服
	private static final String URL_UNION = "http://ghsdk.tianyouxi.com/api/";
	// 工会测试服
//	public static final String URL_UNION = "http://192.168.1.169/tygh/api/";
	
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
	// 公告
	public static final String URL_UNION_ANNOUNCE = getHostAddrs() + "sdkinfo/getnotice";
	// 绑定手机
	public static final String URL_BINDING_PHONE = URL_BASE + "login/bindphone";
	// QQ登录
	public static final String URL_UNION_QQ_LOGIN = getHostAddrs() + "login/qqlogin";
	// 谷歌登陆
	public static final String URL_PAY_GOOGLE = URL_OVERSEAS + "Google/googlesdk";
	// Facebook登陆
	public static final String URL_PAY_FACEBOOK = URL_OVERSEAS + "Facebook/fbSdk";
	// QQ WebView URL
	public static final String URL_QQ_WEB = "https://xui.ptlogin2.qq.com/cgi-bin/xlogin?appid=716027609&pt_3rd_aid=101322155&daid=383&pt_skey_valid=1&style=35&s_url=http%3A%2F%2Fconnect.qq.com&refer_cgi=authorize&which=&auth_time=1470121319621&client_id=101322155&src=1&state=&response_type=token&scope=add_share%2Cadd_topic%2Clist_album%2Cupload_pic%2Cget_simple_userinfo&redirect_uri=auth%3A%2F%2Ftauth.qq.com%2F";
	// 一键登录
	public static final String URL_KEY_LOGIN = getHostAddrs() + "login/keylogin";
	// 忘记密码
	public static final String URL_FORGET_PASS = "http://newsdk.tianyouxi.com/api/commonality/getBackPassword";
	// 身份认证
	public static final String URL_IDENTIFI = getHostAddrs() + "member/upuserauth";
	// 手机账号升级
	public static final String URL_UPGRADE_PHONE = getHostAddrs() + "member/upgradeuserbyphone";
	// 用户名账号升级
	public static final String URL_UPGRADE_ACCOUNT = getHostAddrs() + "member/upgradeuserbyphone";
		
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
	// 登陆方式控制
	public static final String URL_LOGIN_WAY = getHostAddrs() + "sdkinfo/getloginswitch";
	// 悬浮窗控制
	public static final String URL_FLOAT_CONTROL = getHostAddrs() + "sdkinfo/getframe";
	// PayPal支付查单
	public static final String URL_CHECK_PAYPAL = URL_OVERSEAS + "Payorder/paypalupdateorder";
	// Google支付查单
	public static final String URL_CHECK_GOOGLE = URL_OVERSEAS + "Payorder/googleupdateorder";
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
	
	private static String getHostAddrs() {
		if (ConfigHolder.isOverseas) {
			return URL_OVERSEAS;
		} else if (ConfigHolder.isUnion) {
			return URL_UNION;
		} else {
			return URL_BASE;
		}
	}
	
	/** ------------------------- 登录接口 ------------------------- */
	// 获取登录方式
//	public static final String URL_LOGIN_WAY = (ConfigHolder.isOverseas ? URL_OVERSEAS : URL_BASE) + "?c=Member&a=GetLoginSwitch";
//	// 获取验证码
//	public static final String URL_GET_CODE = URL_BASE + "?c=sms&a=SendMsg";
//	// 公告接口
//	public static final String URL_ANNOUNCE = URL_BASE + "?c=Member&a=Getnotice";
//	// QQ登录
//	public static final String URL_QQ_LOGIN = URL_BASE + "?c=register&a=QQQuick";
//	// 用户登录
//	public static final String URL_CODE_LOGIN = (ConfigHolder.isOverseas ? URL_OVERSEAS : URL_BASE) + "?c=NewLog&a=VerificationLog";
//	// 快速登录
//	public static final String URL_LOGIN_QUICK = (ConfigHolder.isOverseas ? URL_OVERSEAS : URL_BASE) + "?c=NewReg&a=FastReg";
//	// 完善用户信息
//	public static final String URL_LOGIN_PERFECT = URL_BASE + "?c=NewReg&a=perfect";
//	// 找回密码
//	public static final String URL_FIND_PASS = URL_BASE +  "?c=login&a=FindPassByPhone";
//	
//	// 微信回调地址
//	public static final String URL_NOTIFY_ALIPAY = "http://www.tianyouxi.com/tianyousdk/index.php/Home/AliPay/notifyurl";
//	// 进入游戏
//	public static final String URL_ENTER_GAME = URL_BASE + "?c=UidSellInfo&a=EnterGame";
//	// 创建角色
//	public static final String URL_CREATE_ROLE = URL_BASE + "?c=UidSellInfo&a=CreateRole";
//	// 创建角色
//	public static final String URL_BINDING_PHONE = URL_BASE + "?c=login&a=BindPhone";
//	// facebook 用户验证
//	public static final String URL_FACEBOOK_LOGIN = "http://testapi.tianyouxi.com/index.php?s=/Facebook/fbSdk";
//	// Google 用户验证
//	public static final String URL_GOOGLE_LOGIN = "http://testapi.tianyouxi.com/index.php?s=Google/GoogleSdk";
//	// 更新角色信息
//	public static final String URL_UPDATE_ROLE_INFO = (ConfigHolder.isOverseas ? URL_OVERSEAS : URL_BASE) + "?c=UidSellInfo&a=UpdateRoleInfo";
//	
//	/** ------------------------- 支付接口 ------------------------- */
//	// 产品信息接口
//	public static String URL_GET_PRODUCT_INFO = URL_BASE + "?c=GameProduct&a=GetProduct";
//	// 获取金额数值
//	public static String URL_WALLET_MONEY_VALUE = URL_BASE + "?c=Order&a=getWalletRecharge";
//	// 客服图片接口
//	public static String URL_SERVER_IMG = (ConfigHolder.isOverseas ? URL_OVERSEAS : URL_BASE) + "?c=Member&a=GetCustom";
//	// 支付方式控制
//	public static String URL_PAY_WAY_CONTROL = (ConfigHolder.isOverseas ? URL_OVERSEAS : URL_BASE) + "?c=Member&a=GetPayType";
//	// 创建订单海外接口
//	public static String URL_CREATE_ORDER_OVERSEAS = "http://testapi.tianyouxi.com/index.php?s=Order/CreatOrder";
//	// 谷歌支付校验地址
//	public static String URL_CHECK_ORDER_GOOGLE = "http://testapi.tianyouxi.com/index.php?s=Order/GoogleUpdateOrder";
//	// PayPal支付校验地址
//	public static String URL_CHECK_ORDER_PAYPAL = "http://testapi.tianyouxi.com/index.php?s=Order/PaypalUpdateOrder";
//	
//	/** ------------------------- 悬浮窗接口 ------------------------- */
//	// 论坛接口
//	public static String URL_BBS = "http://bbs.tianyouxi.com/member.php?mod=logging&action=sdktianyouxilogin";
//	// 首页接口
//	public static String URL_INDEX = "http://www.tianyouxi.com/svnonethink/mobilesdk.php?s=/CheckSdk/sdkindex";
//	// 个人中心接口
//	public static String URL_CENTER = "http://www.tianyouxi.com/svnonethink/mobilesdk.php?s=/CheckSdk/sdkpersonal";
//	// 礼包接口
//	public static String URL_GIFT = "http://www.tianyouxi.com/svnonethink/mobilesdk.php?s=/CheckSdk/sdkgift";
//	// 帮助游戏
//	public static String URL_MENU_HELP = "http://www.tianyouxi.com/svnonethink/mobilesdk.php?s=/CustomService/index.html";
//	// 热门游戏推荐
//	public static String URL_HOT_GAME = "http://www.tianyouxi.com/svnonethink/mobilesdk.php?s=/GameCentre/index.html";
//	// 忘记密码
//	public static String URL_FORGET_PASS = "http://www.tianyouxi.com/svnonethink/mobilesdk.php?s=/Public/getBackPassword.html";
//	
//	// 获取汇款二维码
//	public static String URL_GET_REMIT_CODE = "http://www.tianyouxi.com/Public/tyx/shewm.png";
//	// 悬浮球开关控制
//	public static String URL_FLOAT_CONTROL = (ConfigHolder.isOverseas ? URL_OVERSEAS : URL_BASE) + "?s=/Member/GetFrame";
}
