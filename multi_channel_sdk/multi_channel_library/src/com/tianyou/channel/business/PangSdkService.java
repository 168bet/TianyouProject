package com.tianyou.channel.business;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.igaworks.IgawCommon;
import com.igaworks.adbrix.IgawAdbrix;
import com.igaworks.liveops.IgawLiveOps;
import com.panggame.pgmp2sdk.AppConst;
import com.panggame.pgmp2sdk.Pgmp2Sdk;
import com.panggame.pgmp2sdk.Interface.Pgmp2EventListener;
import com.panggame.pgmp2sdk.Interface.Pgmp2NaverCafeSDKCallBackListener;
import com.panggame.pgmp2sdk.snsApi.lib.GoogleServiceObj;
import com.skplanet.dodo.IapPlugin;
import com.skplanet.dodo.IapPlugin.RequestCallback;
import com.skplanet.dodo.IapResponse;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.OneStoreParam;
import com.tianyou.channel.bean.OneStoreParam.ResultBean;
import com.tianyou.channel.bean.OneStoreParam.ResultBean.ProductBean;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.CommenUtil;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.URLHolder;

public class PangSdkService extends BaseSdkService {

	private Pgmp2Sdk pgmp2Sdk = null;
	private GoogleServiceObj pgmp2SdkGoogleServiceObj;
	private IapPlugin mPlugin;

	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		IgawCommon.autoSessionTracking((Application)context);
	}
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		mIsOverseas = true;
		pgmpLogin();
		oneStorePay();
		IgawAdbrix.retention("game_start");
		boolean achievements = pgmp2SdkGoogleServiceObj.isUseGoogleAchievements();
		LogUtils.d("achievements:" + achievements);
		LogUtils.d("韩国登录接口gameStart");
		pgmp2Sdk.gameStart();
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "");
	}
	
	private void oneStorePay() {
		mPlugin = IapPlugin.getPlugin(mActivity, "development");
//		mPlugin = IapPlugin.getPlugin(mActivity, "release");
	}
	
	private void pgmpLogin() {
		Pgmp2EventListener eventListener = new Pgmp2EventListener() {
			@Override
			public void Pgmp2UnityNoLoginCloseListener(String arg0) { LogUtils.d("Pgmp2UnityNoLoginCloseListener"); }
			
			@Override
			public void Pgmp2UnityNoAgreeNoLoginCloseListener(String arg0) { LogUtils.d("Pgmp2UnityNoAgreeNoLoginCloseListener"); }
			
			@Override
			public void Pgmp2UnityLogoutListener(String resultCode) { 
				LogUtils.d("resultCode:" + resultCode); 
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "");
			}
			
			@Override
			public void Pgmp2UnityLoginListener(String resultCode, final String guid, 
					String idsort, String is_guest, String email) {
				LoginInfo loginParam = new LoginInfo();
				loginParam.setChannelUserId(guid + "");
				loginParam.setNickname(email);
				loginParam.setUserToken(idsort + "");
				loginParam.setIsOverseas(true);
				loginParam.setIsGuest(is_guest);
				checkLogin(loginParam, new LoginCallback() {
					@Override
					public void onSuccess(String data) {
						IgawAdbrix.retention("login");
						IgawAdbrix.firstTimeExperience("Register");
						IgawCommon.setUserId(mActivity, guid);
						IgawLiveOps.initialize(mActivity);
					}
				});
			}
			
			@Override
			public void Pgmp2UnityCheckActivityCloseListener() { LogUtils.d("Pgmp2UnityCheckActivityCloseListener"); }
		};
		
		Pgmp2NaverCafeSDKCallBackListener pgmp2NaverCafeListener = new Pgmp2NaverCafeSDKCallBackListener() {
			@Override
			public void OnVotedListener(String arg0, String arg1, String arg2, String arg3, int arg4) {
				LogUtils.d("OnVotedListener");
			}
			
			@Override
			public void OnPostedCommentListener(String guid, String idsort, String is_guest, String email, int articleId) {
				LogUtils.d("OnPostedCommentListener");
				String url = "http://channel.tianyouxi.com/index.php?c=NaverCaff&a=OnPostedCommentListener";
				Map<String,String> map = new HashMap<String, String>();
				map.put("pid", mRoleInfo.getRoleId());
				map.put("is_guest", is_guest);
				map.put("appid", mChannelInfo.getAppId());
				map.put("email", email);
				map.put("userid", mLoginInfo.getTianyouUserId());
				map.put("guid", guid);
				map.put("articleId", articleId + "");
				map.put("sign", CommenUtil.MD5(mRoleInfo.getRoleId() + mChannelInfo.getAppId() + mLoginInfo.getTianyouUserId() + guid));
				HttpUtils.post(mActivity, url, map, new HttpCallback() {
					@Override
					public void onSuccess(String data) { }
					
					@Override
					public void onFailed(String code) { }
				});
			}
			
			@Override
			public void OnPostedArticleListener(String guid, String idsort, String is_guest, String email, int menuId, int imageCount, int videoCount) { 
				LogUtils.d("OnPostedArticleListener");
				String url = "http://channel.tianyouxi.com/index.php?c=NaverCaff&a=OnPostedArticleListener";
				Map<String,String> map = new HashMap<String, String>();
				map.put("pid", mRoleInfo.getRoleId());
				map.put("is_guest", is_guest);
				map.put("appid", mChannelInfo.getAppId());
				map.put("email", email);
				map.put("userid", mLoginInfo.getTianyouUserId());
				map.put("guid", guid);
				map.put("menuId", menuId + "");
				map.put("imageCount", imageCount + "");
				map.put("videoCount", videoCount + "");
				map.put("sign", CommenUtil.MD5(mRoleInfo.getRoleId() + mChannelInfo.getAppId() + mLoginInfo.getTianyouUserId() + guid));
				HttpUtils.post(mActivity, url, map, new HttpCallback() {
					@Override
					public void onSuccess(String data) { }
					
					@Override
					public void onFailed(String code) { }
				});
			}
			
			@Override
			public void OnJoinedListener(String guid, String idsort, String is_guest, String email) {
				LogUtils.d("OnJoinedListener");
				String url = "http://channel.tianyouxi.com/index.php?c=NaverCaff&a=OnJoinedListener";
				Map<String,String> map = new HashMap<String, String>();
				map.put("pid", mRoleInfo.getRoleId());
				map.put("is_guest", is_guest);
				map.put("appid", mChannelInfo.getAppId());
				map.put("email", email);
				map.put("userid", mLoginInfo.getTianyouUserId());
				map.put("guid", guid);
				map.put("sign", CommenUtil.MD5(mRoleInfo.getRoleId() + mChannelInfo.getAppId() + mLoginInfo.getTianyouUserId() + guid));
				HttpUtils.post(mActivity, url, map, new HttpCallback() {
					@Override
					public void onSuccess(String data) { }
					
					@Override
					public void onFailed(String code) { }
				});
			}
		};
		this.pgmp2Sdk = Pgmp2Sdk.getInstance();
		this.pgmp2Sdk.useFirstAgree();
		int pgmp2SdkInitGameResultCode  = this.pgmp2Sdk.initGame(38, "3b3cc01a3hhhh9e5", 8, "1.1", mActivity, eventListener, pgmp2NaverCafeListener, true);
//		int pgmp2SdkInitGameResultCode  = this.pgmp2Sdk.initGame(33, "iii9934022021004", 8, "1.1", mActivity, eventListener, pgmp2NaverCafeListener);
		LogUtils.d("pgmp2SdkInitGameResultCode:" + pgmp2SdkInitGameResultCode);
		if (pgmp2SdkInitGameResultCode == 1) {
			this.pgmp2SdkGoogleServiceObj = new GoogleServiceObj(this.pgmp2Sdk,mActivity,mActivity);
		}
	}
	
	@Override
	public void doOpenNaverCafe() {
		super.doOpenNaverCafe();
		LogUtils.d("是否有NaverCafe信息：" + pgmp2Sdk.isUseNaverCafe());
		if (pgmp2Sdk.isUseNaverCafe()) {
			pgmp2Sdk.openNaverCafeApp();
		}
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		if (pgmp2Sdk.getLoginVO() == null) {
			pgmp2Sdk.gameStart();
		}
	}
	
	@Override
	public void doCustomerService() {
		super.doCustomerService();
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				pgmp2Sdk.openCSCenterAcitivity();
			}
		});
	}
	
	@Override
	public void doChannelPay(final PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		LogUtils.d("pid:" + orderInfo.getProductId());
		LogUtils.d("productName:" + orderInfo.getProductName());
		LogUtils.d("tId:" + orderInfo.getOrderID());
		mPlugin.sendPaymentRequest("OA00709800", orderInfo.getProductId(), 
				orderInfo.getProductName(), orderInfo.getOrderID(), "", new RequestCallback() {
			@Override
			public void onResponse(IapResponse iapResponse) {
				final String response = iapResponse.getContentToString();
				LogUtils.d("response:" + response);
				OneStoreParam oneStoreParam = new Gson().fromJson(response, OneStoreParam.class);
				final ResultBean result = oneStoreParam.getResult();
				if (result.getCode().equals("0000")) {
					ProductBean product = result.getProduct().get(0);
					Map<String, String> param = new HashMap<String, String>();
					param.put("api_version", oneStoreParam.getApi_version());
					param.put("identifier", oneStoreParam.getIdentifier());
					param.put("method", oneStoreParam.getMethod());
					param.put("code", result.getCode());
					param.put("message", result.getMessage());
					param.put("count", result.getCount() + "");
					param.put("txid", result.getTxid());
					param.put("receipt", result.getReceipt());
					param.put("name", product.getName());
					param.put("kind", product.getKind());
					param.put("id", product.getId());
					param.put("price", product.getPrice() + "");
					String url = (mIsOverseas ? URLHolder.URL_OVERSEAS : URLHolder.URL_BASE) + URLHolder.URL_ONE_STORE;
					HttpUtils.post(mActivity, url, param, new HttpCallback() {
						@Override
						public void onSuccess(String data) {
							LogUtils.d("调用purchase方法" + orderInfo.getProductId());
							LogUtils.d("调用purchase方法" + orderInfo.getMoNey());
							LogUtils.d("调用purchase方法result.getTxid()" + result.getTxid());
							pgmp2Sdk.purchase(orderInfo.getProductId(), orderInfo.getMoNey(), 2, result.getTxid(), response, 1);
							LogUtils.d("purchase:" + orderInfo.getOrderID());
							LogUtils.d("purchase:" + orderInfo.getProductId());
							LogUtils.d("purchase:" + orderInfo.getProductName());
							LogUtils.d("purchase:" + Double.parseDouble(orderInfo.getMoNey()));
							LogUtils.d("purchase:" + Integer.parseInt(payInfo.getAmount()));
							IgawAdbrix.purchase(mActivity, orderInfo.getOrderID(), orderInfo.getProductId(), 
									orderInfo.getProductName(), Double.parseDouble(orderInfo.getMoNey()), 
									Integer.parseInt(payInfo.getAmount()), IgawAdbrix.Currency.KR_KRW, "");
						}
						
						@Override
						public void onFailed(String code) {
							mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, code);
						}
					});
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, result.getMessage());
				}
			}
			
			@Override
			public void onError(String requestid, String errcode, String errmsg) {
				LogUtils.d("requestid:" + requestid);
				LogUtils.d("errcode:" + errcode);
				LogUtils.d("errmsg:" + errmsg);
			}
		});
	}
	
	private int[] level = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 18,
			20, 23, 25, 28, 30, 35, 40, 45, 50 };
	
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUpdateRoleInfo(roleInfo);
		pgmp2Sdk.sendCharacterLevel(Integer.parseInt(roleInfo.getRoleLevel()), mRoleInfo.getRoleName());
		String vipLevel = roleInfo.getVipLevel();
		if (!"0".equals(vipLevel)) {
			LogUtils.d("vip_" + vipLevel);
			IgawAdbrix.firstTimeExperience("vip_" + vipLevel);
		}
		int roleLevel = Integer.parseInt(roleInfo.getRoleLevel());
		for (int l : level) {
			if (roleLevel == l) {
				LogUtils.d("level_" + roleLevel);
				IgawAdbrix.firstTimeExperience("level_" + roleLevel);
			}
		}
	}
	
	@Override
	public void doDataStatistics(String content) {
		super.doDataStatistics(content);
		IgawAdbrix.firstTimeExperience(content);
	}
	
	@Override
	public void doLogout() {
		super.doLogout();
		pgmp2Sdk.openLogoutActivity();
	}
	
	@Override
	public void doExitGame() {
		IgawCommon.protectSessionTracking(mActivity);
		mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "");
	}
	
	@Override
	public void doGoogleAchieve(String achieve) {
		super.doGoogleAchieve(achieve);
		pgmp2SdkGoogleServiceObj.googleAchievementsUnlock(mActivity, mActivity, Arrays.asList(achieve));
	}
	
	@Override
	public void doGoogleAchieveActivity() {
		super.doGoogleAchieveActivity();
		pgmp2SdkGoogleServiceObj.openGoogleAchievements(mActivity, mActivity);
	}
	
	@Override
	public void doPushSwitch(boolean isOpen) {
		super.doPushSwitch(isOpen);
		IgawLiveOps.setTargetingData(mActivity, "night_push", isOpen == true ? 1 : 0);
	}
	
	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) {
		super.doActivityResult(requestCode, resultCode, data);
		LogUtils.d("doActivityResult:" + requestCode + "," + resultCode);
		if (requestCode == AppConst.ON_ACTIVITY_REQUEST_CODE_CHECK_GAME) {		
			// 设置维护后的行为
		} else {
			if (resultCode == AppConst.ON_ACTIVITY_RESULT_CODE_LOGIN) {
				// 设置登录后的行为 游戏玩家信息
			}
			else if (resultCode == AppConst.ON_ACTIVITY_RESULT_CODE_LOGOUT) { // 设置退出后的行为
				LogUtils.d("注销登录");
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "");
			}
			else if (resultCode == AppConst.ON_ACTIVITY_RESULT_CODE_ACTIVITY_CLOSE) {
				// 设置activity登录前结束时的行为
			}
			else if (resultCode == AppConst.ON_ACTIVITY_RESULT_CODE_FIRST_ACTIVITY_CLOSE) {
				// 设置同意activity登录前不勾选同意结束时的行为
			}
		}
		if (this.pgmp2SdkGoogleServiceObj != null) {
			this.pgmp2SdkGoogleServiceObj.onActivityResultFunction(requestCode, resultCode, data, mActivity, mActivity);
		}
	}
	
	@Override
	public void doResume() {
		super.doResume();
		IgawCommon.startSession(mActivity);
		IgawLiveOps.resume(mActivity);
	}
	
	@Override
	public void doPause() {
		super.doPause();
		IgawCommon.endSession();
		IgawLiveOps.resume(mActivity);
	}
	
	@Override
	public void doDestory() {
		super.doDestory();
		mPlugin.exit();
	}
	
}
