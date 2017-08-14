package com.tianyou.channel.business;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.coolcloud.uac.android.api.Coolcloud;
import com.coolcloud.uac.android.api.ErrInfo;
import com.coolcloud.uac.android.api.OnResultListener;
import com.coolcloud.uac.android.common.Constants;
import com.coolcloud.uac.android.common.Params;
import com.iapppay.interfaces.authentactor.AccountBean;
import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.utils.RSAHelper;
import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.PayInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.AppUtils;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.URLHolder;
import com.yulong.paysdk.beens.CoolPayResult;
import com.yulong.paysdk.beens.CoolYunAccessInfo;
import com.yulong.paysdk.coolpayapi.CoolpayApi;
import com.yulong.paysdk.payinterface.IPayResult;

public class KupaiSdkService extends BaseSdkService{
	
	private String appID;
	private static Coolcloud coolcloud;
	
	private static boolean isLand;
	private static String tyAppID;
	private static String promotion;
	private static String payPrivate;
	private String uid;
	private String access_token;
	private String openID;
	
	private RoleInfo mRoleInfo;
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		isLand = island;
	}
	
	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(mActivity);
		appID = channelInfo.getAppId();
		tyAppID = channelInfo.getGameId();
		promotion = channelInfo.getChannelId();
		payPrivate = channelInfo.getPayRsaPrivate();
		coolcloud = Coolcloud.get(mActivity, appID);
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "初始化完成");
	}
	
	@Override
	public void doLogin() {
		
		Bundle input = new Bundle();
		if (isLand){
			input.putInt(Constants.KEY_SCREEN_ORIENTATION,ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
		} else {
			input.putInt(Constants.KEY_SCREEN_ORIENTATION,ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
		}
		input.putString(Constants.KEY_SCOPE, "get_basic_userinfo");
		input.putString(Constants.KEY_RESPONSE_TYPE, Constants.RESPONSE_TYPE_CODE);
		
		coolcloud.login(mActivity, input, new Handler(mActivity.getMainLooper()), new OnResultListener() {
			
			@Override
			public void onResult(Bundle result) {
				String code = result.getString(Params.KEY_AUTHCODE);
				Log.d("TAG", "kupai login success code= "+code);
				
				String phoneIMEI = AppUtils.getPhoeIMEI(mActivity);
				String mdSignature = AppUtils.MD5("session="+code+"&uid="+""+"&appid="+tyAppID);
				Map<String, String> loginParam = new HashMap<String, String>();
				loginParam.put("uid","");
				loginParam.put("session",code);
				loginParam.put("imei",phoneIMEI);
				loginParam.put("appid",tyAppID);
				loginParam.put("promotion",promotion);
				loginParam.put("signature", mdSignature);
				Log.d("TAG", "kupai login param= "+loginParam);
				HttpUtils.post(mActivity, URLHolder.CHECK_LOGIN_URL, loginParam, new HttpCallback() {
					
					@Override
					public void onSuccess(String data) {
						Log.d("TAG","kupai login ty check success data= "+data);
						try {
							JSONObject jsonObject = new JSONObject(data);
							JSONObject result = jsonObject.getJSONObject("result");
							String code = result.getString("code");
							if ("200".equals(code)){
								uid = result.getString("uid");
								access_token = result.getString("access_token");
								openID = result.getString("openid");
								mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_SUCCESS, uid);
							} else {
								mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED,"登录失败" );
							}
						} catch (JSONException e) {
							e.printStackTrace();
							mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, e.getMessage());
						}
					}
					
					@Override
					public void onFailed(String code) {
						Log.d("TAG", "kupai login ty check failed code= "+code);
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, code);
					}
				});
			}
			
			@Override
			public void onError(ErrInfo errInfo) {
				Log.d("TAG", "kupai login failed errInfo= "+errInfo.getMessage());
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, errInfo.getMessage());
			}
			
			@Override
			public void onCancel() {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "用户取消登录");
			}
		});
	}
	
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		mRoleInfo = roleInfo;
	}
	
	
//	@Override
//	public void doLogout() {
//		
//		coolcloud.logout(mActivity);
//		
//		Bundle input = new Bundle();
//		if (isLand){
//			input.putInt(Constants.KEY_SCREEN_ORIENTATION,ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
//		} else {
//			input.putInt(Constants.KEY_SCREEN_ORIENTATION,ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
//		}
//		input.putString(Constants.KEY_SCOPE, "get_basic_userinfo");
//		input.putString(Constants.KEY_RESPONSE_TYPE, Constants.RESPONSE_TYPE_CODE);
//		
//		coolcloud.loginNew(mActivity, input, new Handler(), new OnResultListener() {
//			
//			@Override
//			public void onResult(Bundle result) {
//				Log.d("TAG", "logout onresult-------");
//			}
//			
//			@Override
//			public void onError(ErrInfo errInfo) {
//				Log.d("TAG", "logout errInfo--------------");
//			}
//			
//			@Override
//			public void onCancel() {
//				Log.d("TAG", "logout cancle----------");
//			}
//		});
//	}
	
	@Override
	public void doExitGame() {
		coolcloud.logout(mActivity);
		super.doExitGame();
	}
	
	@Override
	public void doPay(final PayParam payInfo) {
		LogUtils.d("调用支付接口");
		
		PayInfo productInfo = ConfigHolder.getPayInfo(mActivity, payInfo.getPayCode());
		String productDesc = productInfo.getProductDesc();
		String productID = productInfo.getProductId();
		String productName = productInfo.getProductName();
		String money = productInfo.getMoney();
		
		Map<String, String> payParam = new HashMap<String, String>();
		payParam.put("userId", uid);
		payParam.put("appID", tyAppID);
		payParam.put("roleId", mRoleInfo.getRoleId());
		payParam.put("serverID", mRoleInfo.getServerId());
		payParam.put("serverName", mRoleInfo.getServerName());
		payParam.put("customInfo", payInfo.getCustomInfo());
		payParam.put("productId",productID);
		payParam.put("productName", productName);
		payParam.put("productDesc", productDesc);
		payParam.put("moNey",money);
		payParam.put("promotion", promotion);
		Log.d("TAG", "kupai pay param= "+payParam);
		HttpUtils.post(mActivity, URLHolder.CREATE_ORDER_URL, payParam, new HttpCallback() {
			
			@Override
			public void onSuccess(String data) {
				Log.d("TAG", "kupai ty create order success data= "+data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONObject result = jsonObject.getJSONObject("result");
					String code = result.getString("code");
					if ("200".equals(code)){
						final String orderID = result.getJSONObject("orderinfo").getString("orderID");
						float price = Float.parseFloat(result.getJSONObject("orderinfo").getString("moNey"));
						int waresid = result.getJSONObject("orderinfo").getInt("waresid");
						Log.d("TAG", "access_token= "+access_token+",appID= "+appID+",openID= "+openID);
						AccountBean account = CoolPadPay.buildAccount(mActivity, access_token, appID, openID);
						String params = genUrl(appID, uid, payInfo.getCustomInfo(), 
								payPrivate, waresid, price, orderID);
						doKuPaiPay(params, account,orderID);
					} else {
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, e.getMessage());
				}
			}
			
			@Override
			public void onFailed(String code) {
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, code);
			}
		});
		
	}
	
	private void doKuPaiPay(String params,AccountBean buildAccount,final String orderID){
		
		CoolpayApi api = CoolpayApi.createCoolpayApi(mActivity, appID);
		CoolYunAccessInfo accessInfo = new CoolYunAccessInfo();
		accessInfo.setAccessToken("");
		accessInfo.setOpenId("");
		
		com.yulong.paysdk.beens.PayInfo kPayInfo = new com.yulong.paysdk.beens.PayInfo();
		kPayInfo.setAppId("");
		kPayInfo.setPayKey("");
		kPayInfo.setName("");
		kPayInfo.setPoint(1);
		kPayInfo.setQuantity(1);
		kPayInfo.setCpPrivate("");
		kPayInfo.setCpOrder("");
		
		api.startPay(mActivity, kPayInfo, accessInfo, new IPayResult() {
			
			@Override
			public void onResult(CoolPayResult payResult) {
				if (payResult != null) {
					int resultStatus = payResult.getResultStatus();
					String result = payResult.getResult();
					if (resultStatus == 0) {
						checkOrder(orderID);
					} else if (resultStatus == -2) {
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL,"支付取消");
					} else {
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
					}
				}
				
			}
		}, CoolpayApi.PAY_STYLE_DIALOG, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
			CoolPadPay.startPay(mActivity, params, buildAccount, new IPayResultCallback() {
			
				@Override
				public void onPayResult(int resultCode, String signvalue, String resultInfo) {
					if (resultCode == CoolPadPay.PAY_SUCCESS) {
						Log.d("TAG", "pay success-----------code= "+resultCode+",sign= "+signvalue+",info= "+resultInfo);
						Map<String, String> checkParam = new HashMap<String, String>();
						checkParam.put("orderID",orderID); 
						checkParam.put("userId",uid);
						checkParam.put("promotion", promotion);
						Log.d("TAG", "kupai pay check param= "+checkParam);
						HttpUtils.post(mActivity, URLHolder.CHECK_ORDER_URL, checkParam, new HttpCallback() {
							@Override
							public void onSuccess(String data) {
								Log.d("TAG", "kupai pay check success data= "+data);
								try {
									JSONObject jsonObject = new JSONObject(data);
									JSONObject result = jsonObject.getJSONObject("result");
									String code = result.getString("code");
									String msg = result.getString("msg");
									if ("200".equals(code)){
										mTianyouCallback.onResult(TianyouCallback.CODE_PAY_SUCCESS, msg);
									} else {
										mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, data);
									}
								} catch (JSONException e) {
									e.printStackTrace();
									mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, e.getMessage());
								}
							}

							@Override
							public void onFailed(String code) {
								Log.d("TAG", "kupai pay check failed code= "+code);
								mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, code);
							}
						});
					} else if (resultCode == CoolPadPay.PAY_CANCEL) {
						Log.d("TAG", "pay cancle--------------code= "+resultCode+",sign= "+signvalue+",info= "+resultInfo);
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "用户取消支付");
					} else {
						Log.d("TAG", "pay failed-------------code= "+resultCode+",sign= "+signvalue+",info= "+resultInfo);
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, resultInfo);
					}
				}
			});
	}
	
	private String genUrl( String appid, String appuserid, String cpprivateinfo, String appPrivateKey, int waresid, double price, String cporderid) {
		String json = "";

		JSONObject obj = new JSONObject();
		try {
			obj.put("appid", appid);
			obj.put("waresid", waresid);
			obj.put("cporderid", cporderid);
			obj.put("price", price);
			obj.put("appuserid", appuserid);

			
			/*CP私有信息，选填*/
			String cpprivateinfo0 = cpprivateinfo;
			if(!TextUtils.isEmpty(cpprivateinfo0)){
				obj.put("cpprivateinfo", cpprivateinfo0);
			}	
			
			/*支付成功的通知地址。选填。如果客户端不设置本参数，则使用服务端配置的地址。*/
//			String notifyurl0 = PayConfig.notifyurl;
//			if(!TextUtils.isEmpty(notifyurl0)){
//				obj.put("notifyurl", notifyurl0);
//			}			
			json = obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = "";
		try {
			sign = RSAHelper.signForPKCS1(json, appPrivateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "transdata=" + URLEncoder.encode(json) + "&sign=" + URLEncoder.encode(sign) + "&signtype=" + "RSA";
	}
	
}
