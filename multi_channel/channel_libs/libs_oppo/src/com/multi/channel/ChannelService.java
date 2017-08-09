package com.multi.channel;

import org.json.JSONException;
import org.json.JSONObject;

import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.ApiCallback;
import com.nearme.game.sdk.callback.GameExitCallback;
import com.nearme.game.sdk.common.model.biz.PayInfo;
import com.nearme.platform.opensdk.pay.PayResponse;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;

import android.app.Activity;
import android.content.Context;

public class ChannelService extends BaseSdkService{

	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		GameCenterSDK.init(mChannelInfo.getAppToken(), context);
	}
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		doNoticeGame(TianyouCallback.CODE_INIT, "初始化成功");
	}
	
	@Override
	public void doLogin() {
		GameCenterSDK.getInstance().doLogin(mActivity, new ApiCallback() {
			@Override
			public void onSuccess(String resultMsg) {
				doGetTokenAndSsoid();
			}

			@Override
			public void onFailure(String resultMsg, int resultCode) {
				doNoticeGame(TianyouCallback.CODE_LOGIN_FAILED, "");
			}
		});
	}
	
	private void doGetTokenAndSsoid() {
		GameCenterSDK.getInstance().doGetTokenAndSsoid(new ApiCallback() {
			@Override
			public void onSuccess(String resultMsg) {
				try {
					JSONObject json = new JSONObject(resultMsg);
					String token = json.getString("token");
					String ssoid = json.getString("ssoid");
					mLoginInfo.setUserToken(token);
					mLoginInfo.setChannelUserId(ssoid);
					checkLogin();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String content, int resultCode) {
				doNoticeGame(TianyouCallback.CODE_LOGIN_FAILED, "");
			}
		});
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		PayInfo oppoPayInfo = new PayInfo(orderInfo.getOrderID(), "自定义字段", Integer.parseInt(orderInfo.getMoNey()));
		oppoPayInfo.setProductDesc(mPayInfo.getProductDesc());
		oppoPayInfo.setProductName(mPayInfo.getProductName());
		oppoPayInfo.setCallbackUrl(orderInfo.getNotifyurl());
		
		GameCenterSDK.getInstance().doPay(mActivity, oppoPayInfo, new ApiCallback() {
			@Override
			public void onSuccess(String resultMsg) {
				checkOrder(orderInfo.getOrderID());
			}

			@Override
			public void onFailure(String resultMsg, int resultCode) {
				if (PayResponse.CODE_CANCEL != resultCode) {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "");
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "");
				}
			}
		});
		
	}
	
	@Override
	public void doVerifiedInfo() {
		super.doVerifiedInfo();
		GameCenterSDK.getInstance().doGetVerifiedInfo(new ApiCallback() {
			@Override
			public void onSuccess(String msg) {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("age", msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mTianyouCallback.onResult(TianyouCallback.CODE_VERIFIEDINFO_SUCCESS, jsonObject.toString());
			}
			
			@Override
			public void onFailure(String msg, int code) {
				mTianyouCallback.onResult(TianyouCallback.CODE_VERIFIEDINFO_FAILED, "实名认证失败");
			}
		});
	}

	@Override
	public void doExitGame() {
		GameCenterSDK.getInstance().onExit(mActivity, new GameExitCallback() {
			@Override
			public void exitGame() {
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "");
			}
		});
	}

	@Override
	public void doResume() {
		GameCenterSDK.getInstance().onResume(mActivity);
	}
	
	@Override
	public void doPause() {
		GameCenterSDK.getInstance().onPause();
	}
}
