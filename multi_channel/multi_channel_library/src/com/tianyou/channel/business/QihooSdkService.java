package com.tianyou.channel.business;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.qihoo.gamecenter.sdk.activity.ContainerActivity;
import com.qihoo.gamecenter.sdk.common.IDispatcherCallback;
import com.qihoo.gamecenter.sdk.matrix.Matrix;
import com.qihoo.gamecenter.sdk.protocols.CPCallBackMgr.MatrixCallBack;
import com.qihoo.gamecenter.sdk.protocols.ProtocolConfigs;
import com.qihoo.gamecenter.sdk.protocols.ProtocolKeys;
import com.tianyou.channel.bean.OrderInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.AppUtils;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.QihooPayInfo;
import com.tianyou.channel.utils.URLHolder;

public class QihooSdkService extends BaseSdkService {

	private static String payUrl;
	private String mUserId;
	private boolean isLand;

	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);

		Matrix.initInApplication((Application) context);

		isLand = island;
	}

	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		Matrix.setActivity(mActivity, new MatrixCallBack() {

			@Override
			public void execute(Context context, int code, String msg) {
				if (code == ProtocolConfigs.FUNC_CODE_INITSUCCESS) {
					// SDK初始化成功
					mTianyouCallback.onResult(TianyouCallback.CODE_INIT,
							"SDK初始化完成");
				}else if (code == ProtocolConfigs.FUNC_CODE_SWITCH_ACCOUNT) {
					// 切换账号
				}
			}
		});
	}


	@Override
	public void doLogin() {
		super.doLogin();
		Intent intent = new Intent(mActivity, ContainerActivity.class);
		intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLand);
		intent.putExtra(ProtocolKeys.FUNCTION_CODE,
				ProtocolConfigs.FUNC_CODE_LOGIN);
		Matrix.execute(mActivity, intent, mIDispatcherCallback);
	}

	@Override
	public void doLogout() {
		super.doLogout();
		Intent intent = new Intent(mActivity, ContainerActivity.class);
		intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, true);
		intent.putExtra(ProtocolKeys.FUNCTION_CODE,
				ProtocolConfigs.FUNC_CODE_SWITCH_ACCOUNT);
		Matrix.invokeActivity(mActivity, intent, mIDispatcherCallback);
	}

	private IDispatcherCallback mIDispatcherCallback = new IDispatcherCallback() {
		@Override
		public void onFinished(final String data) {
			if (isCancelLogin(data)) {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED,
						"data");
			} else {
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONObject dataInfo = jsonObject.getJSONObject("data");
					String accessToken = dataInfo.getString("access_token");
<<<<<<< HEAD
					String phoneIMEI = CommenUtil.getPhoeIMEI(mActivity);
					String mdSignature = CommenUtil
							.MD5("session=" + accessToken + "&appid="
									+ mChannelInfo.getAppId());
					Log.d("TAG", "qihoo login token = " + accessToken);
=======
					String phoneIMEI = AppUtils.getPhoeIMEI(mActivity);
					String mdSignature = AppUtils.MD5("session="+accessToken+"&appid="+mChannelInfo.getAppId());
					Log.d("TAG","qihoo login token = "+accessToken);
>>>>>>> 41a2fc61cf7c9eb020c733a649e01e280dc50348
					Map<String, String> param = new HashMap<String, String>();
					param.put("uid", "");
					param.put("appid", mChannelInfo.getGameId());
					param.put("session", accessToken);
					param.put("promotion", mChannelInfo.getChannelId());
					param.put("imei", phoneIMEI);
					param.put("signature", mdSignature);
					HttpUtils.post(mActivity, URLHolder.CHECK_LOGIN_URL_QIHOO,
							param, new HttpCallback() {
								@Override
								public void onSuccess(String tyData) {
									try {
										JSONObject tyJsonObject = new JSONObject(
												tyData);
										JSONObject tyDataInfo = (JSONObject) tyJsonObject
												.get("result");
										String code = tyDataInfo
												.getString("code");
										if ("200".equals(code)) {
											mUserId = tyDataInfo
													.getString("uid");
											payUrl = tyDataInfo
													.getString("payurl");
											mTianyouCallback
													.onResult(
															TianyouCallback.CODE_LOGIN_SUCCESS,
															mUserId);
										} else {
											mTianyouCallback
													.onResult(
															TianyouCallback.CODE_LOGIN_FAILED,
															"");
										}
									} catch (JSONException e) {
										e.printStackTrace();
										mTianyouCallback
												.onResult(
														TianyouCallback.CODE_LOGIN_FAILED,
														"");
									}
								}

								@Override
								public void onFailed(String code) {
									mTianyouCallback.onResult(
											TianyouCallback.CODE_LOGIN_FAILED,
											"");
								}
							});

				} catch (JSONException e) {
					e.printStackTrace();
					mTianyouCallback.onResult(
							TianyouCallback.CODE_LOGIN_FAILED, "");
				}
			}
		}
	};
	
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		QihooPayInfo info = new QihooPayInfo();
		info.setQihooUserId(orderInfo.getUserId());
		info.setMoneyAmount(orderInfo.getMoNey());
		info.setProductName(mPayInfo.getProductName());
		info.setProductId(mPayInfo.getProductId());
		info.setNotifyUri(payUrl);
		info.setAppName("龙神之光");
		info.setAppUserName(mRoleInfo.getRoleName());
		info.setAppUserId(mRoleInfo.getRoleId());
		info.setAppOrderId(orderInfo.getOrderID());
		// 支付基础参数
		Intent intent = getPayIntent(true, info);
		LogUtils.d("qihoo pay info= " + info.toString());
		// 必需参数，使用360SDK的支付模块。
		intent.putExtra(ProtocolKeys.FUNCTION_CODE,
				ProtocolConfigs.FUNC_CODE_PAY);
		// 可选参数，登录界面的背景图片路径，必须是本地图片路径
		intent.putExtra(ProtocolKeys.UI_BACKGROUND_PICTRUE, "");
		IDispatcherCallback mPayCallback = new MyResultCancleCallBack(
				mPayInfo.getProductId());
		Matrix.invokeActivity(mActivity, intent, mPayCallback);
	};

	@Override
	public void doExitGame() {
		super.doExitGame();
		Bundle bundle = new Bundle();
		// 界面相关参数，360SDK界面是否以横屏显示。
		bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, true);
		// 可选参数，登录界面的背景图片路径，必须是本地图片路径
		bundle.putString(ProtocolKeys.UI_BACKGROUND_PICTRUE, "");
		// 必需参数，使用360SDK的退出模块。
		bundle.putInt(ProtocolKeys.FUNCTION_CODE,
				ProtocolConfigs.FUNC_CODE_QUIT);
		Intent intent = new Intent(mActivity, ContainerActivity.class);
		intent.putExtras(bundle);
		Matrix.invokeActivity(mActivity, intent, new IDispatcherCallback() {
			@Override
			public void onFinished(String data) {
				try {
					JSONObject jsonObject = new JSONObject(data);
					int which = jsonObject.getInt("which");
					if (which == 2) {
						mTianyouCallback.onResult(
								TianyouCallback.CODE_QUIT_SUCCESS, "");
					} else {
						mTianyouCallback.onResult(
								TianyouCallback.CODE_QUIT_CANCEL, "");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUpdateRoleInfo(roleInfo);
	}
	@Override
	public void doDestory() {
		Matrix.destroy(mActivity);
	}
	@Override
	public void doStart() {
		super.doStart();
	}
	@Override
	public void doResume() {
		super.doResume();
	}
	@Override
	public void doPause() {
		super.doPause();
	}
	@Override
	public void doStop() {
		super.doStop();
	}
	@Override
	public void doRestart() {
		super.doRestart();
	}
	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) {
		super.doActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void doNewIntent(Intent intent) {
		super.doNewIntent(intent);
	}

	/**
	 * 角色信息采集接口
	 */
//	protected void doSdkGetUserInfoByCP() {
//		HashMap eventParams = new HashMap();
//		eventParams.put("type", "levelUp");// 角色信息接口触发的场景
//		eventParams.put("zoneid", 1);// 当前角色所在游戏区服id
//		eventParams.put("zonename", "刀塔传奇1区");// 当前角色所在游戏区服名称
//		eventParams.put("roleid", "12345678");// 当前角色id
//		eventParams.put("rolename", "三国风吹来的鱼");// 当前角色名称
//		Matrix.statEventInfo(mActivity, eventParams);
//
//	}

	private class MyResultCancleCallBack implements IDispatcherCallback {
		private String orderID;

		public MyResultCancleCallBack(String orderID) {
			this.orderID = orderID;
		}

		@Override
		public void onFinished(String data) {
			Log.d("TAG", "qihoo pay json = " + data);
			try {
				JSONObject jsonObject = new JSONObject(data);
				int errorCode = jsonObject.optInt("error_code");
				if (errorCode == 0) {
					// 支付成功
					Map<String, String> param = new HashMap<String, String>();
					param.put("orderID", orderID);
					param.put("userId", mUserId);
					param.put("promotion", mChannelInfo.getChannelId());
					HttpUtils.post(mActivity, URLHolder.CHECK_ORDER_URL, param,
							new HttpCallback() {
								@Override
								public void onSuccess(String tyData) {
									try {
										JSONObject jsonObject = new JSONObject(
												tyData);
										JSONObject result = (JSONObject) jsonObject
												.get("result");
										String code = result.getString("code");
										if ("200".equals(code)) {
											mTianyouCallback
													.onResult(
															TianyouCallback.CODE_PAY_SUCCESS,
															"");
										} else {
											mTianyouCallback
													.onResult(
															TianyouCallback.CODE_PAY_FAILED,
															"");
										}
									} catch (JSONException e) {
										e.printStackTrace();
										mTianyouCallback
												.onResult(
														TianyouCallback.CODE_PAY_FAILED,
														"");
									}
								}

								@Override
								public void onFailed(String code) {
									Log.d("TAG", "qihoo pay tycheck data= "
											+ code);
									mTianyouCallback
											.onResult(
													TianyouCallback.CODE_PAY_FAILED,
													"");
								}
							});

				} else if (errorCode == -1) {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL,
							"");
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED,
							data);
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "");
			}
		}
	}

	private boolean isCancelLogin(String data) {
		try {
			JSONObject joData = new JSONObject(data);
			int errno = joData.optInt("errno", -1);
			if (-1 == errno) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	protected Intent getPayIntent(boolean isLandScape, QihooPayInfo info) {
		Bundle bundle = new Bundle();

		// 界面相关参数，360SDK界面是否以横屏显示。
		bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE,
				isLandScape);

		// *** 以下非界面相关参数 ***

		// 设置QihooPay中的参数。

		// 必需参数，360账号id，整数。
		bundle.putString(ProtocolKeys.QIHOO_USER_ID, info.getQihooUserId());

		// 必需参数，所购买商品金额, 以分为单位。金额大于等于100分，360SDK运行定额支付流程； 金额数为0，360SDK运行不定额支付流程。
		bundle.putString(ProtocolKeys.AMOUNT, info.getMoneyAmount());

		// 必需参数，所购买商品名称，应用指定，建议中文，最大10个中文字。
		bundle.putString(ProtocolKeys.PRODUCT_NAME, info.getProductName());

		// 必需参数，购买商品的商品id，应用指定，最大16字符。
		bundle.putString(ProtocolKeys.PRODUCT_ID, info.getProductId());

		// 必需参数，应用方提供的支付结果通知uri，最大255字符。360服务器将把支付接口回调给该uri，具体协议请查看文档中，支付结果通知接口–应用服务器提供接口。
		bundle.putString(ProtocolKeys.NOTIFY_URI, info.getNotifyUri());

		// 必需参数，游戏或应用名称，最大16中文字。
		bundle.putString(ProtocolKeys.APP_NAME, info.getAppName());

		// 必需参数，应用内的用户名，如游戏角色名。 若应用内绑定360账号和应用账号，则可用360用户名，最大16中文字。（充值不分区服，
		// 充到统一的用户账户，各区服角色均可使用）。
		bundle.putString(ProtocolKeys.APP_USER_NAME, info.getAppUserName());

		// 必需参数，应用内的用户id。
		// 若应用内绑定360账号和应用账号，充值不分区服，充到统一的用户账户，各区服角色均可使用，则可用360用户ID最大32字符。
		bundle.putString(ProtocolKeys.APP_USER_ID, info.getAppUserId());

		// 必需参数，应用订单号，应用内必须唯一，最大32字符。
		bundle.putString(ProtocolKeys.APP_ORDER_ID, info.getAppOrderId());

		// 可选参数，应用扩展信息1，原样返回，最大255字符。
		// bundle.putString(ProtocolKeys.APP_EXT_1, info.getAppExt1());

		// 可选参数，应用扩展信息2，原样返回，最大255字符。
		// bundle.putString(ProtocolKeys.APP_EXT_2, info.getAppExt2());

		// 必需参数，使用360SDK的支付模块:CP可以根据需求选择使用 带有收银台的支付模块 或者
		// 直接调用微信支付模块或者直接调用支付宝支付模块。
		// functionCode 对应三种支付模块：
		// ProtocolConfigs.FUNC_CODE_PAY;//表示 带有360收银台的支付模块。
		// ProtocolConfigs.FUNC_CODE_WEIXIN_PAY;//表示 微信支付模块。
		// ProtocolConfigs.FUNC_CODE_ALI_PAY;//表示支付宝支付模块。
		// bundle.putInt(ProtocolKeys.FUNCTION_CODE,functionCode);

		// 必需参数，使用360SDK的支付模块。
		bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_PAY);

		Intent intent = new Intent(mActivity, ContainerActivity.class);
		intent.putExtras(bundle);

		return intent;
	}

}
