package com.tianyou.channel.business;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.facebook.applinks.AppLinkData.CompletionHandler;
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
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.OrderInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.CommenUtil;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.URLHolder;

public class PangGooglepaySdkService extends BaseSdkService {

	private Pgmp2Sdk pgmp2Sdk = null;
	private GoogleServiceObj pgmp2SdkGoogleServiceObj;
	private IapPlugin mPlugin;
	
	// 谷歌支付
	private IInAppBillingService mService;
    private ServiceConnection mServiceConn;
    
    // Facebook广告
    
    @Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		IgawCommon.autoSessionTracking((Application)context);
		//谷歌包接入
		FacebookSdk.sdkInitialize(context);
		AppEventsLogger.activateApp((Application)context);
	}

	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		mIsOverseas = true;
		pgmpLogin();
//		oneStorePay();
		facebookDeepLink();
		IgawAdbrix.retention("game_start");
		boolean achievements = pgmp2SdkGoogleServiceObj.isUseGoogleAchievements();
		LogUtils.d("achievements:" + achievements);
		LogUtils.d("韩国登录接口gameStart");
		pgmp2Sdk.gameStart();
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "");
		
		// 谷歌支付
		mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, android.os.IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            	Log.d("TAG","mService = null-----------------");
                mService = null;
            }
        };

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        mActivity.bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
        
        // 申请权限
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE)
				!= PackageManager.PERMISSION_GRANTED) {
        	Log.d("TAG","111111111");
			ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
			Log.d("TAG","222222222");
        }
        
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
        		!= PackageManager.PERMISSION_GRANTED) {
        	Log.d("TAG","33333333");
        	ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        	Log.d("TAG","44444444");
        }
	}
	
	//谷歌包接入
	private void facebookDeepLink() {
		AppLinkData.fetchDeferredAppLinkData(mActivity, new CompletionHandler() {
			@Override
			public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
				if (appLinkData != null) {
					try {
		                Bundle bundle = appLinkData.getArgumentBundle();
		                // Get deeplink from ApplinkData
		                String deeplink = bundle.getString(AppLinkData.ARGUMENTS_NATIVE_URL);
		                // Report deeplink for IgawCommon to get tracking parameters
		                //IgawCommon.setReferralUrl(MainActivity.this, deeplink);
		                //4.2.5 版本开始 setReferralUrl 变更 setReferralUrlForFacebook
		                //IgawCommon.setReferralUrlForFacebook(MainActivity.this, deeplink);
		                // Deeplinking
		                Intent i = new Intent(Intent.ACTION_VIEW);
		                i.addCategory(Intent.CATEGORY_BROWSABLE);
		                i.setData(Uri.parse(deeplink));
		                mActivity.startActivity(i);
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
				}
			}
		});
	}
	
	private void dataStatistics() {
		//facebook
//		AppLinkData.fetchDeferredAppLinkData(mActivity, new AppLinkData.CompletionHandler() {
//			public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
//				if (appLinkData != null) {
//					try {
//						Bundle bundle = appLinkData.getArgumentBundle();
//						// Get deeplink from ApplinkData
//						String deeplink = bundle.getString(AppLinkData.ARGUMENTS_NATIVE_URL);
//						// Report deeplink for IgawCommon to get
//						// tracking parameters
//						// IgawCommon.setReferralUrl(MainActivity.this,
//						// deeplink);
//						// 4.2.5 版本开始 setReferralUrl 变更
//						// setReferralUrlForFacebook。
//						// IgawCommon.setReferralUrlForFacebook(MainActivity.this,
//						// deeplink);
//						// Deeplinking
//						Intent i = new Intent(Intent.ACTION_VIEW);
//						i.addCategory(Intent.CATEGORY_BROWSABLE);
//						i.setData(Uri.parse(deeplink));
//						MainActivity.this.startActivity(i);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
	}
	
//	private void oneStorePay() {
//		mPlugin = IapPlugin.getPlugin(mActivity, "development");
//	}

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
			public void OnPostedCommentListener(String arg0, String arg1, String arg2, String arg3, int arg4) {
				LogUtils.d("OnPostedCommentListener");
			}
			
			@Override
			public void OnPostedArticleListener(String arg0, String arg1, String arg2, String arg3, int arg4, int arg5, int arg6) { 
				LogUtils.d("OnPostedArticleListener");}
			
			@Override
			public void OnJoinedListener(String arg0, String arg1, String arg2, String arg3) {
				LogUtils.d("OnJoinedListener");
			}
		};
		this.pgmp2Sdk = Pgmp2Sdk.getInstance();
		this.pgmp2Sdk.useFirstAgree();
		String versionCode = CommenUtil.getMetaDataValue(mActivity, "google_version");
		LogUtils.d("versionCode= "+versionCode);
		int pgmp2SdkInitGameResultCode  = this.pgmp2Sdk.initGame(33, "iii9934022021004", 2, versionCode, 
				mActivity, eventListener, pgmp2NaverCafeListener);
		
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
	public void doEntryGame() {
		super.doEntryGame();
	}
	
	@Override
	public void doPay(PayParam payInfo) {
//		super.doPay(payInfo);
		String payCode = payInfo.getPayCode();
		mPayInfo = ConfigHolder.getPayInfo(mActivity, payCode);
		createGoogleOrder(payInfo);
	}
	
	private String money;
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
//		super.doChannelPay(payInfo, orderInfo);
		money = orderInfo.getMoNey();
		try {
			Log.d("TAG","get buyIntent packageName= "+mActivity.getPackageName()+",waresid= "+orderInfo.getWaresid()+",orderID= "+orderInfo.getOrderID());
			Log.d("TAG", "mService= "+mService);
			Bundle buyIntentBundle = mService.getBuyIntent(3, mActivity.getPackageName(), orderInfo.getWaresid(), "inapp", orderInfo.getOrderID());
			Log.d("TAG", "buyIntentBundle= "+buyIntentBundle);
			
	        int code = buyIntentBundle.getInt("RESPONSE_CODE");
	//        Log.d("TAG", "code= " + code);
	        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
	        Log.d("TAG", "pendingIntent= "+pendingIntent);
	//    try {
	        mActivity.startIntentSenderForResult(pendingIntent.getIntentSender(),
	                1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
	                Integer.valueOf(0));
		} catch(Exception e) {
			Log.d("TAG", "get buy intent exception= " + e.getMessage());
		}
	}
	
	protected void createGoogleOrder(final PayParam payInfo) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", mLoginInfo.getTianyouUserId());
		param.put("appID", mChannelInfo.getGameId());
		param.put("roleId", mRoleInfo.getRoleId());
		param.put("serverID", mRoleInfo.getServerId());
		param.put("serverName", mRoleInfo.getServerName());
		param.put("customInfo", payInfo.getCustomInfo());
		param.put("productId", mPayInfo.getProductId());
		param.put("productName", mPayInfo.getProductName());
		param.put("productDesc", mPayInfo.getProductDesc());
		param.put("moNey", mPayInfo.getMoney());
		param.put("promotion", mChannelInfo.getChannelId());
		param.put("playerid", mLoginInfo.getChannelUserId());
		param.put("roleName", mRoleInfo.getRoleName());
		HttpUtils.post(mActivity, URLHolder.URL_OVERSEAS+URLHolder.CREATE_ORDER_URL, param, new HttpCallback() {
			@Override
			public void onSuccess(String data) {
				OrderInfo orderInfo = new Gson().fromJson(data, OrderInfo.class);
				if ("200".equals(orderInfo.getResult().getCode())) {
					doChannelPay(payInfo, orderInfo.getResult().getOrderinfo());
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "创建订单失败");
				}
			}
			
			@Override
			public void onFailed(String code) {
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "创建订单失败");
			}
		});
	}
	
	private int[] level = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 18,
			20, 23, 25, 28, 30, 35, 40, 45, 50 };
	
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUpdateRoleInfo(roleInfo);
		IgawAdbrix.retention("item_select_vip4");
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
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				pgmp2SdkGoogleServiceObj.openGoogleAchievements(mActivity, mActivity);
			}
		});
	}
	
	@Override
	public void doPushSwitch(boolean isOpen) {
		super.doPushSwitch(isOpen);
		IgawLiveOps.setTargetingData(mActivity, "night_push", isOpen == true ? 1 : 0);
	}
	
	
	private String googelOrderID;
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
		
		// 谷歌支付
		if (requestCode == 1001) {  
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            Log.d("TAG", "pay responsecode= " + responseCode);
            final String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
            
            Log.d("TAG", "purchaseData= " + purchaseData + ",dataSignature= " + dataSignature);
            
            Log.d("TAG", "result code= " + resultCode);
 
            if (resultCode == mActivity.RESULT_OK) {
            	Log.d("TAG", "mActivity.RESULT_OK---------------");
            	new Thread(new Runnable() {
        			@Override
        			public void run() {
        				// 消耗谷歌商品
        				try {
        					JSONObject dataObject = new JSONObject(purchaseData);
        					String purchaseToken = dataObject.getString("purchaseToken");LogUtils.d("purchaseToken= "+purchaseToken);
        					int response = mService.consumePurchase(3, mActivity.getPackageName(), purchaseToken);
        					LogUtils.d("packageName= "+mActivity.getPackageName()+",purchaseToken= "+purchaseToken);
        					LogUtils.d("onActivityRestul response= "+response);
        					googelOrderID = dataObject.getString("orderId");
        					LogUtils.d("googleOrderID= "+googelOrderID);
        				} catch (Exception e) {
        					Log.d("TAG", "consumePurchase= "+e.getMessage());
        					e.printStackTrace();
        				}
        			}
        		}).start();
            }
        	checkGoogleOrder(purchaseData, dataSignature);
		} else {
			mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
		}
	}
	
	// 校验谷歌订单
	private void checkGoogleOrder(final String purchaseData,String dataSignature){
		Map<String, String> param = new HashMap<String, String>();
        param.put("appID", mChannelInfo.getGameId());
        param.put("inapp_purchase_data", purchaseData);
        param.put("inapp_data_signature", dataSignature);
        param.put("promotion", mChannelInfo.getChannelId());
        Log.d("TAG", "google pay param= " + param);
        HttpUtils.post(mActivity, URLHolder.CHECK_ORDER_GOOGLE, param, new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                Log.d("TAG", "pay success data= " + data);
                try {
					JSONObject jsonObject = new JSONObject(data);
					JSONObject result = jsonObject.getJSONObject("result");
					String code = result.getString("code");
					String msg = result.getString("msg");
					if ("200".equals(code)) {
						LogUtils.d("支付成功");
						LogUtils.d("google pay response= "+purchaseData+",money= "+money+",googleOrderId= "+googelOrderID);
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_SUCCESS, msg);
						pgmp2Sdk.purchase(mPayInfo.getProductId(), money, 2, googelOrderID, purchaseData, 1);
						IgawAdbrix.purchase(mActivity, googelOrderID, mPayInfo.getProductId(), 
								mPayInfo.getProductName(), Double.parseDouble(money), 
								Integer.parseInt("1"), IgawAdbrix.Currency.KR_KRW, "");
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
                Log.d("TAG", "pay failed----------code= " + code);
            }
        });
	}
	
	@Override
	public void doResume() {
		super.doResume();
		IgawCommon.startSession(mActivity);
		IgawLiveOps.resume(mActivity);
		//谷歌包接入
		AppEventsLogger.activateApp(mActivity);
	}
	
	@Override
	public void doPause() {
		super.doPause();
		IgawCommon.endSession();
		IgawLiveOps.resume(mActivity);
		//谷歌包接入
		AppEventsLogger.deactivateApp(mActivity);
	}
	
	@Override
	public void doDestory() {
		super.doDestory();
		mPlugin.exit();
		if (mService != null) {
			mActivity.unbindService(mServiceConn);
		}
	}
	
}
