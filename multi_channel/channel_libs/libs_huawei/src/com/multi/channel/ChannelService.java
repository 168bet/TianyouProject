package com.multi.channel;

import java.util.Map;

import com.huawei.gameservice.sdk.GameServiceSDK;
import com.huawei.gameservice.sdk.control.GameEventHandler;
import com.huawei.gameservice.sdk.model.PayResult;
import com.huawei.gameservice.sdk.model.Result;
import com.huawei.gameservice.sdk.model.UserResult;
import com.huawei.gb.huawei.GameBoxUtil;
import com.huawei.gb.huawei.RSAUtil;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.ToastUtils;

import android.app.Activity;

public class ChannelService extends BaseSdkService {

	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		GameServiceSDK.init(mActivity, mChannelMap.get("APP_ID"), mChannelMap.get("PAY_ID"),
				"com.huawei.gb.huawei.installnewtype.provider", new GameEventHandler() {
					@Override
					public void onResult(Result result) {
						LogUtils.d("description:" + result.description);
						if (result.rtnCode != Result.RESULT_OK) {
							ToastUtils.show(mActivity, "init the game service SDK failed:");
							return;
						}
						checkUpdate();
						doNoticeGame(TianyouCallback.CODE_INIT, "");
					}

					@Override
					public String getGameSign(String appId, String cpId, String ts) {
						return createGameSign(appId + cpId + ts);
					}
				});
	}

	/**
	 * 生成游戏签名 generate the game sign
	 */
	private String createGameSign(String data) {
		String str = data;
		try {
			String result = RSAUtil.sha256WithRsa(str.getBytes("UTF-8"), mChannelMap.get("BUOY_SECRET"));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 检测游戏更新 check the update for game
	 */
	private void checkUpdate() {
		GameServiceSDK.checkUpdate(mActivity, new GameEventHandler() {
			@Override
			public void onResult(Result result) {
				if (result.rtnCode != Result.RESULT_OK) {
					ToastUtils.show(mActivity, "check update failed:");
				}
			}

			@Override
			public String getGameSign(String appId, String cpId, String ts) {
				return null;
			}
		});
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		login(1);
	}

	/**
	 * 帐号登录 Login
	 */
	private void login(int authType) {
		GameServiceSDK.login(mActivity, new GameEventHandler() {
			@Override
			public void onResult(Result result) {
				if (result.rtnCode != Result.RESULT_OK) {
					doNoticeGame(TianyouCallback.CODE_LOGIN_FAILED, "");
				} else {
					UserResult userResult = (UserResult) result;
					if (userResult.isAuth != null && userResult.isAuth == 1) {
						boolean checkResult = checkSign(mChannelMap.get("APP_ID") + userResult.ts + userResult.playerId,
								userResult.gameAuthSign);
						if (checkResult) {
							mLoginInfo.setChannelUserId(userResult.playerId);
							checkLogin();
						} else {
							doNoticeGame(TianyouCallback.CODE_LOGIN_FAILED, "");
						}
					} else if (userResult.isChange != null && userResult.isChange == 1) {
						login(1);
					} else {
						mLoginInfo.setChannelUserId(userResult.playerId);
						checkLogin();
					}
				}
			}

			@Override
			public String getGameSign(String appId, String cpId, String ts) {
				return null;
			}
		}, authType);
	}

	/**
	 * 校验签名 check the auth sign
	 */
	protected boolean checkSign(String data, String gameAuthSign) {
		/*
		 * 建议CP获取签名后去游戏自己的服务器校验签名，公钥值请参考开发指导书5.1 登录鉴权签名的验签公钥
		 */
		/*
		 * The CP need to deployed a server for checking the sign.
		 */
		try {
			return RSAUtil.verify(data.getBytes("UTF-8"), mChannelInfo.getPublicKey(), gameAuthSign);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		GameBoxUtil.startPay(mActivity, mPayInfo.getMoney(), mPayInfo.getProductName(), mPayInfo.getProductDesc(),
				mPayInfo.getOrderId(), payHandler);
	}

	/**
	 * 支付回调handler
	 */
	private GameEventHandler payHandler = new GameEventHandler() {
		@Override
		public String getGameSign(String appId, String cpId, String ts) {
			return null;
		}

		@Override
		public void onResult(Result result) {
			Map<String, String> payResp = ((PayResult) result).getResultMap();
			// String pay = getString(R.string.pay_result_failed);
			// 支付成功，进行验签
			// payment successful, then check the response value
			if ("0".equals(payResp.get("returnCode"))) {
				if ("success".equals(payResp.get("errMsg"))) {
					// 支付成功，验证信息的安全性；待验签字符串中如果有isCheckReturnCode参数且为yes，则去除isCheckReturnCode参数
					// If the response value contain the param
					// "isCheckReturnCode" and its value is yes, then remove the
					// param "isCheckReturnCode".
					if (payResp.containsKey("isCheckReturnCode") && "yes".equals(payResp.get("isCheckReturnCode"))) {
						payResp.remove("isCheckReturnCode");

					}
					// 支付成功，验证信息的安全性；待验签字符串中如果没有isCheckReturnCode参数活着不为yes，则去除isCheckReturnCode和returnCode参数
					// If the response value does not contain the param
					// "isCheckReturnCode" and its value is yes, then remove the
					// param "isCheckReturnCode".
					else {
						payResp.remove("isCheckReturnCode");
						payResp.remove("returnCode");
					}
					// 支付成功，验证信息的安全性；待验签字符串需要去除sign参数
					// remove the param "sign" from response
					String sign = payResp.remove("sign");

					String noSigna = GameBoxUtil.getSignData(payResp);

					// 使用公钥进行验签
					// check the sign using RSA public key
					boolean s = RSAUtil.doCheck(noSigna, sign, mChannelMap.get("PAY_RSA_PUBLIC"));

					// if (s) {
					// pay = getString(R.string.pay_result_success);
					// } else {
					// pay = getString(R.string.pay_result_check_sign_failed);
					// }
				}

			} else if ("30002".equals(payResp.get("returnCode"))) {
				// pay = getString(R.string.pay_result_timeout);
			}
			// Toast.makeText(GameActivity.this, pay,
			// Toast.LENGTH_SHORT).show();

			// 重新生成订单号，订单编号不能重复，所以使用时间的方式，CP可以根据实际情况进行修改，最长30字符
			// generate the pay ID using the date format, and it can not be
			// repeated.
			// CP can generate the pay ID according to the actual situation, a
			// maximum of 30 characters
			// DateFormat format = new
			// java.text.SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS", Locale.US);
			// String requestId = format.format(new Date());
			// ((TextView) findViewById(R.id.requestId)).setText(requestId);

		}
	};
}