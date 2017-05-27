package com.tianyou.channel.business;

import java.util.Map;

import android.app.Activity;

import com.iapppay.interfaces.callback.ILoginResultCallback;
import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.iapppay.sdk.main.IAppPayOrderUtils;
import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.LogUtils;

public class SamsungSdkService extends BaseSdkService{
	
	// 声明渠道参数变量
	private String appID;
	private String clientID;
	private String clientSecret;
	private String privateKey;
	private String publicKey;

	/**
	 * Activity初始化接口
	 */
	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(mActivity);	// 获取渠道配置信息
		initVariables(channelInfo);		// 变量赋值
		LogUtils.d("init SDK appID= "+appID+",clientID= "+clientID+",clientSecret= "+clientSecret);
		IAppPay.init(mActivity, IAppPay.LANDSCAPE, appID, clientID, clientSecret);	// 初始化SDK
	}
	
	/**
	 * 登录接口
	 */
	@Override
	public void doLogin() {
		super.doLogin();
		String loginParams = IAppPayOrderUtils.getLoginParams(appID,privateKey);	// 获取登录参数
		IAppPay.startLogin(mActivity, loginParams, mLoginResultCallback);	// 调用渠道登录接口
	}
	
	/**
	 * 支付接口
	 */
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		String payParams = getPayParams(orderInfo);		// 获取支付参数
		IAppPay.startPay(mActivity, payParams, null);		//调用渠道支付接口
	}
	
	/**
	 * 获取支付参数
	 */
	private String getPayParams(OrderinfoBean orderInfo){
		IAppPayOrderUtils orderUtils = new IAppPayOrderUtils();
		orderUtils.setAppid(appID);
		orderUtils.setWaresid(0);
		orderUtils.setCporderid(orderInfo.getOrderID());
		orderUtils.setAppuserid("");
		orderUtils.setPrice(Float.parseFloat(orderInfo.getMoNey()));
		orderUtils.setWaresname("");
		orderUtils.setCpprivateinfo(orderInfo.getOrderID());
		orderUtils.setNotifyurl(orderInfo.getNotifyurl());
		String payParams = orderUtils.getTransdata(privateKey);
		LogUtils.d("payParams= "+payParams);
		return payParams;
	}
	
	/**
	 * 变量赋值
	 */
	private void initVariables(ChannelInfo channelInfo){
		appID = channelInfo.getAppId();
		privateKey = channelInfo.getPravateKey();
		clientID = channelInfo.getClientId();
		clientSecret = channelInfo.getClientSecret();
		publicKey = channelInfo.getPublicKey();
	}
	
	/****************************	回调接口	***************************************/
	/**
	 * 登录回调接口
	 */
	private ILoginResultCallback mLoginResultCallback = new ILoginResultCallback() {
		
		@Override
		public void onSuccess(Map<String, String> resultMapStr) {
			Map<String, String> loginResult = IAppPayOrderUtils.checkLoginResult(resultMapStr, publicKey);	//调用 IAppPayOrderUtils 的验签方法进行登陆结果验证
			if(loginResult != null && !loginResult.isEmpty()){
				String iapppayUserid =resultMapStr.get("iapppayUserid");
				String samGlobalID =resultMapStr.get("samGlobalID");
				String tokenMsg =resultMapStr.get("tokenMsg");
				LogUtils.d("login success userID= "+iapppayUserid+",globalID= "+samGlobalID+",token= "+tokenMsg);
			}else{
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
				LogUtils.e("登录成功，验签失败");
			}	
		}
		
		@Override
		public void onFaild(String errorCode, String errorMsg) {
			LogUtils.e("login failed errorCode= "+errorCode+",errorMsg= "+errorMsg);
			mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED,"登录失败");
		}
		
		@Override
		public void onCanceled() { mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "取消登录");}		// 登录取消的回调
	};
	
	/**
	 * 支付回调接口
	 */
	private IPayResultCallback mPayResultCallback = new IPayResultCallback() {

		@Override
		public void onPayResult(int resultCode, String signValue, String resultInfo, Map<String, String> authMap) {
			//----实名认证结果的处理，如果对这些参数没有任何使用，可以忽略此处----Start
			String authTypeDes = "";
			if(authMap != null && !authMap.isEmpty()){
				String authType = authMap.get("authType");//实名认证的类型。1-弱认证，2-强认证
				String samGlobalID = authMap.get("samGlobalID");//三星账户唯一标识
				String samUserEmail = authMap.get("samUserEmail");//邮箱或者手机号码
				LogUtils.d("authType:"+authType + ",samGlobalID:" + samGlobalID + ",samUserEmail:"+samUserEmail);
				authTypeDes = ",实名认证的类型 ="+authType;
			}else{
				authTypeDes =  "";
			}
			//----实名认证结果的处理，如果对这些参数没有任何使用，可以忽略此处----End
			
			switch (resultCode) {
			case IAppPay.PAY_SUCCESS:
				//调用 IAppPayOrderUtils 的验签方法进行支付结果验证
				boolean payState = IAppPayOrderUtils.checkPayResult(signValue, publicKey);
				if(payState){
					// 支付成功
				}
				break;
			case IAppPay.PAY_ING:
//				Toast.makeText(GoodsActivity.this, "成功下单"+authTypeDes, Toast.LENGTH_LONG).show();
				break ;
				 
			default:
//				Toast.makeText(GoodsActivity.this, resultInfo+authTypeDes, Toast.LENGTH_LONG).show();
				break;
			}
//			Log.d(TAG, "requestCode:"+resultCode + ",signvalue:" + signvalue + ",resultInfo:"+resultInfo);
		}
		
	};
}
