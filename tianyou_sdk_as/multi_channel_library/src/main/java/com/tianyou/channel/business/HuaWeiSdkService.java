package com.tianyou.channel.business;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.util.Log;

import com.android.huawei.pay.plugin.PayParameters;
import com.android.huawei.pay.util.HuaweiPayUtil;
import com.android.huawei.pay.util.Rsa;
import com.huawei.gameservice.sdk.GameServiceSDK;
import com.huawei.gameservice.sdk.api.GameEventHandler;
import com.huawei.gameservice.sdk.api.PayResult;
import com.huawei.gameservice.sdk.api.Result;
import com.huawei.gameservice.sdk.api.UserResult;
import com.huawei.gameservice.sdk.model.RoleInfo;
import com.tianyou.channel.bean.OrderInfo;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.GlobalParam;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.RSAUtil;

public class HuaWeiSdkService extends BaseSdkService {
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		String provider = "com.tianyouxi.lszg.huawei.installnewtype.provider";
		GameServiceSDK.init(activity, mChannelInfo.getAppId(), mChannelInfo.getPayId(), provider, new GameEventHandler() {
			@Override
			public void onResult(Result arg0) {
				checkUpdate();
				mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "");
			}
			
			@Override
			public String getGameSign(String appId, String cpId, String ts) {
				return createGameSign(appId + cpId + ts);
			}
		});
	}

	private void checkUpdate() {
		GameServiceSDK.checkUpdate(mActivity, new GameEventHandler() {
			@Override
			public void onResult(final Result result) {
				if(result.rtnCode != Result.RESULT_OK) {
					LogUtils.d("check update failed:" + result.rtnCode);
				}
			}
			
			@Override
			public String getGameSign(String appId, String cpId, String ts){
				return createGameSign(appId+cpId+ts);
			}
		});
	}
	
	private String createGameSign(String data){
		String str = data;
		try {
			String result = RSAUtil.sha256WithRsa(str.getBytes("UTF-8"), mChannelInfo.getBuoSecret());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		HashMap<String, String> playerInfo = new HashMap<String, String>();
		playerInfo.put(RoleInfo.GAME_RANK, mRoleInfo.getRoleLevel());
		playerInfo.put(RoleInfo.GAME_ROLE, mRoleInfo.getRoleName());
		playerInfo.put(RoleInfo.GAME_AREA, mRoleInfo.getServerId());
		playerInfo.put(RoleInfo.GAME_SOCIATY, mRoleInfo.getParty());
		GameServiceSDK.addPlayerInfo(mActivity, playerInfo, new GameEventHandler(){ 
			@Override
			public void onResult(Result result) {
				if(result.rtnCode != Result.RESULT_OK) {
					LogUtils.d("add player info failed:" + result.rtnCode);
				}
			}

			@Override
			public String getGameSign(String appId, String cpId, String ts) {
				return createGameSign(appId + cpId + ts);
			}
		});
	}
	
	@Override
	public void doLogin() {
		login(1);
	}
	
	private void login(int authType) {
		GameServiceSDK.login(mActivity, new GameEventHandler(){
			@Override
			public void onResult(Result result) {
				UserResult userResult = (UserResult) result;
				// 登录失败
				if (userResult.rtnCode != Result.RESULT_OK) {
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
					return;
				}
				// 场景一： 登录成功
				if (userResult.rtnCode == Result.RESULT_OK && userResult.isAuth != null) {
					String uid = userResult.playerId;
					String session = userResult.gameAuthSign;
					if (!session.isEmpty()) checkLogin(uid, session);
					return;
				}
				// 场景三： 通知帐号变换
				if (userResult.rtnCode == Result.RESULT_OK && userResult.isChange != null && userResult.isChange == 1) {
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "");
					return;
				}
			}
			
			@Override
			public String getGameSign(String appId, String cpId, String ts){
				return createGameSign(appId+cpId+ts);
			}
			
		}, authType);
	}
	
	@Override
	public void doChannelPay(final OrderInfo orderInfo) {
		Map<String, String> params = new HashMap<String, String>();
        params.put(GlobalParam.PayParams.USER_ID, mChannelInfo.getPayId());
        params.put(GlobalParam.PayParams.APPLICATION_ID, mChannelInfo.getAppId());
        params.put(GlobalParam.PayParams.AMOUNT, orderInfo.getPrice());
        params.put(GlobalParam.PayParams.PRODUCT_NAME, mPayInfo.getProductName());
        params.put(GlobalParam.PayParams.PRODUCT_DESC, mPayInfo.getProductDesc());
        params.put(GlobalParam.PayParams.REQUEST_ID, orderInfo.getOrderId());
		String noSign = HuaweiPayUtil.getSignData(params);
		String sign = Rsa.sign(noSign, mChannelInfo.getPayRsaPrivate());
		LogUtils.d("noSign:" + noSign);
		LogUtils.d("sign:" + sign);
		LogUtils.d("params:" + params);
		boolean doCheck = Rsa.doCheck(noSign, sign, mChannelInfo.getPayRsaPublic());
		LogUtils.d("doCheck:" + doCheck);
		Log.d("huawei", "huawei:noSign:" + noSign);
        Log.d("huawei", "huawei:GlobalParam.PAY_RSA_PRIVATE:" + GlobalParam.PAY_RSA_PRIVATE);
        Log.d("huawei", "huawei:sign:" + sign);
        Log.d("huawei", "huawei:doCheck:" + doCheck);
        Log.d("huawei", "huawei:params:" + params);
		
		Map<String, Object> payInfo = new HashMap<String, Object>();
        payInfo.put(GlobalParam.PayParams.AMOUNT, orderInfo.getPrice());
        payInfo.put(GlobalParam.PayParams.PRODUCT_NAME, mPayInfo.getProductName());
        payInfo.put(GlobalParam.PayParams.REQUEST_ID, orderInfo.getOrderId());
        payInfo.put(GlobalParam.PayParams.PRODUCT_DESC, mPayInfo.getProductDesc());
        payInfo.put(GlobalParam.PayParams.USER_NAME, "天游互动");
        payInfo.put(GlobalParam.PayParams.APPLICATION_ID, mChannelInfo.getAppId());
        payInfo.put(GlobalParam.PayParams.USER_ID, mChannelInfo.getPayId());
        payInfo.put(GlobalParam.PayParams.SIGN, sign);
        payInfo.put(GlobalParam.PayParams.SERVICE_CATALOG, "X6");
        payInfo.put(GlobalParam.PayParams.SHOW_LOG, true);
        payInfo.put(GlobalParam.PayParams.SCREENT_ORIENT, GlobalParam.PAY_ORI_LAND);
        LogUtils.d("payInfo:" + payInfo.toString());
		GameServiceSDK.startPay(mActivity, payInfo, new GameEventHandler() {
			@Override
			public String getGameSign(String appId, String cpId, String ts) {
				return createGameSign(appId + cpId + ts);
			}

			@Override
			public void onResult(Result result) {
				LogUtils.d("result:" + result);
	            Map<String, String> payResp = ((PayResult)result).getResultMap();
	            LogUtils.d("payResp.get(PayParameters.returnCode):" + payResp.get(PayParameters.returnCode));
	            if ("0".equals(payResp.get(PayParameters.returnCode))) {
	                if ("success".equals(payResp.get(PayParameters.errMsg))) {
	                    // 支付成功，验证信息的安全性；待验签字符串中如果有isCheckReturnCode参数且为yes，则去除isCheckReturnCode参数
	                	// If the response value contain the param "isCheckReturnCode" and its value is yes, then remove the param "isCheckReturnCode".
	                	if (payResp.containsKey("isCheckReturnCode") && "yes".equals(payResp.get("isCheckReturnCode"))) {
	                        payResp.remove("isCheckReturnCode");
	                    }
	                	// 支付成功，验证信息的安全性；待验签字符串中如果没有isCheckReturnCode参数活着不为yes，则去除isCheckReturnCode和returnCode参数
	                	// If the response value does not contain the param "isCheckReturnCode" and its value is yes, then remove the param "isCheckReturnCode".
	                	else {
	                        payResp.remove("isCheckReturnCode");
	                        payResp.remove(PayParameters.returnCode);
	                    }
	                    // 支付成功，验证信息的安全性；待验签字符串需要去除sign参数
	                	// remove the param "sign" from response
	                    String sign = payResp.remove(PayParameters.sign);
	                    String noSigna = HuaweiPayUtil.getSignData(payResp);
	                    // 使用公钥进行验签
	                    // check the sign using RSA public key
	                    boolean s = Rsa.doCheck(noSigna, sign, mChannelInfo.getPayRsaPublic());
	                    if (s) {
//	                        pay = getString(R.string.pay_result_success);
	                    }
	                    else
	                    {
//	                        pay = getString(R.string.pay_result_check_sign_failed);
	                    }
	                }
	            } else if ("30002".equals(payResp.get(PayParameters.returnCode))) {
//	                pay = getString(R.string.pay_result_timeout);
	            }
	            String code = payResp.get("returnCode");
				if ("0".equals(code)){
					checkOrder(orderInfo.getOrderId());
				} else if ("30000".equals(code)) {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "");
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "");
				}
			}
		});
	}
	
	@Override
	public void doResume() {
		GameServiceSDK.showFloatWindow(mActivity);
	}

	@Override
	public void doPause() {
		GameServiceSDK.hideFloatWindow(mActivity);
	}
	
	@Override
	public void doDestory() {
		GameServiceSDK.destroy(mActivity);
	}
	
	@Override
	public boolean isShowLogout() {
		return true;
	}

}
