package com.tianyou.channel.business;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.meizu.gamesdk.model.callback.MzLoginListener;
import com.meizu.gamesdk.model.callback.MzPayListener;
import com.meizu.gamesdk.model.model.LoginResultCode;
import com.meizu.gamesdk.model.model.MzAccountInfo;
import com.meizu.gamesdk.model.model.PayResultCode;
import com.meizu.gamesdk.online.core.MzGameBarPlatform;
import com.meizu.gamesdk.online.core.MzGameCenterPlatform;
import com.meizu.gamesdk.online.model.model.MzBuyInfo;
import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.URLHolder;

public class MeizuSdkService extends BaseSdkService {

	private MzGameBarPlatform mzGameBarPlatform;
	
	private String appID;
	private String appKey;
	private String appSecret;

	private String promotion;
	private String tyAppID;
	
	private String sid;
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(context);
		appID = channelInfo.getAppId();
		appKey = channelInfo.getAppKey();
		appSecret = channelInfo.getAppSecret();
		promotion = channelInfo.getChannelId();
		tyAppID = channelInfo.getGameId();
		Log.d("TAG",appID+" "+appKey+" "+appSecret+" "+promotion+" "+tyAppID);
		MzGameCenterPlatform.init(context, appID, appKey);
	}
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		mzGameBarPlatform = new MzGameBarPlatform(mActivity, MzGameBarPlatform.GRAVITY_LEFT_TOP);
		mzGameBarPlatform.onActivityCreate();
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "");
	}
	
	@Override
	public void doLogin() {
		MzGameCenterPlatform.login(mActivity, new MzLoginListener() {
			@Override
			public void onLoginResult(int code, MzAccountInfo accountInfo, String errorMsg) {
				switch (code) {
				case LoginResultCode.LOGIN_SUCCESS:
					sid = accountInfo.getUid();
					checkLogin(sid, accountInfo.getSession());
					break;
				case LoginResultCode.LOGIN_ERROR_CANCEL:
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "");
					break;
				default:
					Log.d("TAG","meizu login failed"+errorMsg);
					try {
						JSONObject result = new JSONObject();
						result.accumulate("errorMsg", errorMsg);
						result.accumulate("code", code);
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, result.toString());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		});
	}
	
	@Override
	public void doPay(String payCode) {
		PayInfo payInfo = ConfigHolder.getPayInfo(mActivity, payCode);
		String productId = payInfo.getProductId();
		String productSubject = payInfo.getProductName();
		String productBody = payInfo.getProductDesc();
		String buyAmount = mRoleInfo.getBuyAmount();
		String money = payInfo.getMoney();
		// 创建订单
		Map<String, String> payParam = new HashMap<String, String>();
		payParam.put("userId", mUserId);
		payParam.put("appID", tyAppID);
		payParam.put("roleId", mRoleInfo.getRoleId());
		payParam.put("serverID", mRoleInfo.getServerId());
		payParam.put("serverName", mRoleInfo.getServerName());
		payParam.put("customInfo", mRoleInfo.getCustomInfo());
		payParam.put("productId",productId);
		payParam.put("productName", productSubject);
		payParam.put("productDesc", productBody);
		payParam.put("moNey",money);
		payParam.put("promotion", promotion);
		// 魅族所需参数
		payParam.put("app_id", appID);
		payParam.put("buy_amount",buyAmount);
		payParam.put("pay_type", "0");
		payParam.put("product_body", productBody);
		payParam.put("product_id", productId);
		payParam.put("product_per_price", money);
		payParam.put("product_subject", productSubject);
		payParam.put("product_unit", "");
		payParam.put("uid", sid);
		payParam.put("user_info", mRoleInfo.toString());
		HttpUtils.post(mActivity, URLHolder.CREATE_ORDER_URL, payParam, new HttpCallback() {
			
			@Override
			public void onSuccess(String data) {
				Log.d("TAG","meizu pay ty createOrder success data= "+data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONObject result = jsonObject.getJSONObject("result");
					String code = result.getString("code");
					if ("200".equals(code)){
						JSONObject orderInfo = result.getJSONObject("orderinfo");
						String orderID = orderInfo.getString("orderID");
						String createTime = orderInfo.getString("create_time");
						String sign = orderInfo.getString("signinfo");
						String totalPrice = orderInfo.getString("total_price");
						Log.d("TAG","sign= "+sign+"------------");
						
						Bundle buyBundle = new MzBuyInfo().setBuyCount(Integer.parseInt(orderInfo.getString("buy_amount")))
								.setCpUserInfo(orderInfo.getString("user_info")).setOrderAmount(totalPrice)
								.setOrderId(orderID).setPerPrice(orderInfo.getString("product_per_price"))
								.setProductBody(orderInfo.getString("product_body")).setProductId(orderInfo.getString("productId"))
								.setProductSubject(orderInfo.getString("product_subject")).setProductUnit(orderInfo.getString("product_unit"))
								.setSign(sign).setSignType("md5").setCreateTime(Long.parseLong(createTime))
								.setAppid(appID).setUserUid(sid).setPayType(Integer.parseInt(orderInfo.getString("pay_type")))
								.toBundle();
						doMeizuPay(buyBundle,orderID);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailed(String code) {
				Log.d("TAG","meizi pay ty createOrder failed code= "+code);
			}
		});
	}
	
	private void doMeizuPay(Bundle buyBundle,final String orderID){
		
		MzGameCenterPlatform.payOnline(mActivity, buyBundle, new MzPayListener() {
			@Override
			public void onPayResult(int code, Bundle bundle, String errorMsg) {
				Log.d("TAG","meizu pay result = "+bundle);
				switch (code) {
				case PayResultCode.PAY_SUCCESS:
					Map<String, String> data = new HashMap<String, String>();
					data.put("orderID",orderID); 
					data.put("userId",mUserId);
					data.put("promotion", promotion);
					Log.d("TAG", "meizu pay ty check param= "+data);
					
					try {
						Thread.sleep(3000);
						HttpUtils.post(mActivity, URLHolder.CHECK_ORDER_URL, data, new HttpCallback() {
							@Override
							public void onSuccess(String data) {
								Log.d("TAG","meizu pay ty check order successs= "+data);
								try {
									JSONObject jsonObject = new JSONObject(data);
									JSONObject result = jsonObject.getJSONObject("result");
									String code = result.getString("code");
									String msg = result.getString("msg");
									if ("200".equals(code)){
//										callback.onSuccess(msg);
										mTianyouCallback.onResult(TianyouCallback.CODE_PAY_SUCCESS, "支付成功");
									} else {
//										callback.onFailed(data);
										mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
									}
								} catch (JSONException e) {
									e.printStackTrace();
//									callback.onFailed(e.getMessage());
									mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
								}
							}

							@Override
							public void onFailed(String code) {
								Log.d("TAG","meizu pay ty check order failed= "+code);
//								callback.onFailed(code);
								mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
							}
						});
					} catch (InterruptedException e1) {
						e1.printStackTrace();
//						callback.onFailed(e1.getMessage());
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
					}
					
					break;
				case PayResultCode.PAY_ERROR_CANCEL:
//					callback.onFailed("");
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "支付取消");
					break;
				default:
					JSONObject result = new JSONObject();
					try {
						result.put("errorMsg", errorMsg);
						result.put("code", code);
					} catch (JSONException e) {
						e.printStackTrace();
					}
//					callback.onFailed(result.toString());
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
					break;
				}
			}
		});
	}
	
	@Override
	public void doExitGame() {
		MzGameCenterPlatform.logout(mActivity);
		mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "");
	}

	@Override
	public void doResume() {
		mzGameBarPlatform.onActivityResume();
	}

	@Override
	public void doPause() {
		mzGameBarPlatform.onActivityPause();
	}
	
	@Override
	public void doDestory() {
		mzGameBarPlatform.onActivityDestroy();
	}
	
}
