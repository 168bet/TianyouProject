package com.tianyou.channel.business;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.gionee.gamesdk.AccountInfo;
import com.gionee.gamesdk.GamePayer;
import com.gionee.gamesdk.QuitGameCallback;
import com.gionee.gamesdk.GamePlatform;
import com.gionee.gamesdk.GamePlatform.LoginListener;
import com.gionee.gamesdk.OrderInfo;
import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.CommenUtil;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.URLHolder;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;

public class JinliSdkService extends BaseSdkService{
	
	private String appKey;
	private GamePayer mGamePayer;
	private String sid;
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(context);
		appKey = channelInfo.getAppKey();
		GamePlatform.init(context, appKey);
	}
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "");
	}
	
	@Override
	public void doLogin() {
		GamePlatform.loginAccount(mActivity, true, new LoginListener() {
			@Override
			public void onSuccess(AccountInfo accountInfo) {
				Log.d("TAG","jinli login success info= "+accountInfo.toString());
				sid = accountInfo.mUserId;
				String session = accountInfo.mToken;
				Log.d("TAG", "playerid= "+accountInfo.mPlayerId);
				checkJinLiLogin(sid, session,accountInfo.mPlayerId);
			}
			
			@Override
			public void onError(Exception e) {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
			}

			@Override
			public void onCancel() {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "登录取消");
			}
			
		});
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		try {
			OrderInfo order = new OrderInfo();
			order.setApiKey(orderInfo.getApi_key());
			order.setOutOrderNo(orderInfo.getOut_order_no());
			order.setSubmitTime(orderInfo.getSubmit_time());
			LogUtils.d("doChannelPay:" + orderInfo.getApi_key());
			LogUtils.d("doChannelPay:" + orderInfo.getOut_order_no());
			LogUtils.d("doChannelPay:" + orderInfo.getSubmit_time());
			mGamePayer.pay(order, mGamePayer.new GamePayCallback() {
				@Override
				public void onPaySuccess() {
					checkOrder(orderInfo.getOrderID());
				}
				
				@Override
				public void onPayCancel() {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "");
				}
				
				@Override
				public void onPayFail(String stateCode) {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "stateCode:" + stateCode);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkJinLiLogin(String uid, String session,String playerid) {
    	String gameId = mChannelInfo.getGameId();
		Map<String, String> param = new HashMap<String, String>();
		param.put("uid", uid);
		param.put("session", session);
		param.put("imei", CommenUtil.getPhoeIMEI(mActivity));
		param.put("appid", gameId);
		param.put("promotion", mChannelInfo.getChannelId());
		param.put("playerid", playerid);
		param.put("signature", CommenUtil.MD5("session=" + session + "&uid=" + uid + "&appid=" + gameId));
		LogUtils.d("session:" + param.get("session"));
		HttpUtils.post(mActivity, URLHolder.CHECK_LOGIN_URL, param, new HttpCallback() {
			@Override
			public void onSuccess(String data) {
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONObject result = (JSONObject) jsonObject.get("result");
					String code = result.getString("code");
					if ("200".equals(code)) {
						String uid = result.getString("uid");
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_SUCCESS, uid);
					} else {
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
				}
			}
			
			@Override
			public void onFailed(String code) {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败" + code);
			}
		});
	}
	
	@Override
	public void doExitGame() {
		LogUtils.d("调用退出游戏接口");		
		GamePlatform.quitGame(mActivity, new QuitGameCallback() {
			
			@Override
			public void onQuit() {
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "退出游戏成功");
			}
			
			@Override
			public void onCancel() {
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "退出游戏取消");
			}
		});
	}
	
	@Override
	public void doResume() {
		mGamePayer = new GamePayer(mActivity);
		mGamePayer.onResume();
	}
	
	@Override
	public void doDestory() {
		mGamePayer.onDestroy();
	}
}
