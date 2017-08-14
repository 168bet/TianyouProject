package com.tianyou.channel.business;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.baidu.gamesdk.BDGameSDK;
import com.baidu.gamesdk.IResponse;
import com.baidu.gamesdk.OnGameExitListener;
import com.baidu.gamesdk.ResultCode;
import com.baidu.platformsdk.PayOrderInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;
import com.tianyou.channel.utils.URLHolder;

public class BaiduSdkService extends BaseSdkService{
	
	private String sid;
	private String promotion;
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		BDGameSDK.initApplication((Application)context);
	}
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		BDGameSDK.getAnnouncementInfo(mActivity);
		BDGameSDK.setSuspendWindowChangeAccountListener(new IResponse<Void>() {
			@Override
			public void onResponse(int resultCode, String msg, Void extraData) {
				switch (resultCode) {
				case ResultCode.LOGIN_SUCCESS:
					Log.d("TAG", "logout success------------");
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "用户注销账号");
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							BDGameSDK.showFloatView(mActivity);
							sid = BDGameSDK.getLoginUid();
							String token = BDGameSDK.getLoginAccessToken();
							Log.d("TAG","uid="+sid+",token="+token);
							checkLogin(sid, token);
						}
					}, 3000);
					break;
					
				case ResultCode.LOGIN_FAIL:
					Log.d("TAG", "logout failed----------");
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT,"");
					break;
					
				case ResultCode.LOGIN_CANCEL:
					Log.d("TAG", "logout cancle-------------");
					break;
				}
			}
		});
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
	}
	
	@Override
	public void doLogin() {
		BDGameSDK.login(new IResponse<Void>() {
			@Override
			public void onResponse(int resultCode, String resultMsg, Void extraData) {
				if (resultCode == ResultCode.LOGIN_SUCCESS){
					BDGameSDK.showFloatView(mActivity);
					sid = BDGameSDK.getLoginUid();
					String token = BDGameSDK.getLoginAccessToken();
					Log.d("TAG","uid="+sid+",token="+token);
					checkLogin(sid, token);
				} else if (resultCode == ResultCode.LOGIN_FAIL) {
					Log.d("TAG", "bdgamesdk login failed");
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED,"登陆失败");
				} else if (resultCode == ResultCode.LOGIN_CANCEL) {
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "登录取消");
				}
			}
		});
	}
	
//	@Override
//	public void doLogout(ResultCallback callback) {
//		BDGameSDK.logout();
//	}
	
	@Override
	public void doChannelPay(OrderinfoBean orderInfo) {
		super.doChannelPay(orderInfo);
		String orderID = orderInfo.getOrderID();
		final PayOrderInfo bdPayInfo = new PayOrderInfo();
		bdPayInfo.setCooperatorOrderSerial(orderID);
		bdPayInfo.setProductName(mPayInfo.getProductName());
		bdPayInfo.setTotalPriceCent(Long.parseLong(orderInfo.getMoNey()));
		bdPayInfo.setRatio(orderInfo.getRatio());
		
		BDGameSDK.pay(bdPayInfo,null, new IResponse<PayOrderInfo>() {

			@Override
			public void onResponse(int resultCode, String resultMsg, PayOrderInfo extraData) {
				String orderID = bdPayInfo.getCooperatorOrderSerial();
				switch (resultCode) {
					case ResultCode.PAY_SUCCESS:
						
						checkBaiduOrder(orderID);
						break;
	
					case ResultCode.PAY_SUBMIT_ORDER:
						checkBaiduOrder(orderID);
						break;
						
					case ResultCode.PAY_FAIL:
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
						break;
						
					case ResultCode.PAY_CANCEL:
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "支付取消");
						break;
				}
			}
		});
		
	}
	
	@Override
	public void doExitGame() {
		BDGameSDK.gameExit(mActivity, new OnGameExitListener() {
			
			@Override
			public void onGameExit() {
//				callback.onSuccess("退出游戏成功");
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "退出游戏");
			}
		});
	}
	
	
	private void checkBaiduOrder(String orderID) {
		Map<String, String> checkParam = new HashMap<String, String>();
		checkParam.put("orderID",orderID); 
		checkParam.put("userId",mUserId);
		checkParam.put("promotion", promotion);
		Log.d("TAG", "check param= "+checkParam);
		try {
			Thread.sleep(3000);
			HttpUtils.post(mActivity,URLHolder.CHECK_ORDER_URL, checkParam, new HttpCallback() {
				
				@Override
				public void onSuccess(String data) {
					Log.d("TAG", "pay success data= "+data);
					try {
						JSONObject jsonObject = new JSONObject(data);
						JSONObject result = jsonObject.getJSONObject("result");
						String code = result.getString("code");
						if ("200".equals(code)){
							mTianyouCallback.onResult(TianyouCallback.CODE_PAY_SUCCESS, "支付成功");
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
					Log.d("TAG", "pay failed code= "+code);
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED,code);
				}
			});
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, e1.getMessage());
		}
	}
	
	@Override
	public void doPause() {
		BDGameSDK.onPause(mActivity);
	}
	
	@Override
	public void doResume() {
		BDGameSDK.onResume(mActivity);
	}
	
	
	

}
