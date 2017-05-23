package com.tianyou.channel.business;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayInfo;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.URLHolder;
import com.u8.sdk.IU8SDKListener;
import com.u8.sdk.InitResult;
import com.u8.sdk.PayParams;
import com.u8.sdk.PayResult;
import com.u8.sdk.U8Code;
import com.u8.sdk.U8SDK;
import com.u8.sdk.UserExtraData;
import com.u8.sdk.log.Log;
import com.u8.sdk.plugin.U8Pay;
import com.u8.sdk.plugin.U8User;
import com.u8.sdk.verify.UToken;

public class AipuSdkService extends BaseSdkService{
	
	private String mOrderID;

	/**
	 * 1.balance没有传
	 * 2.替换新jar包，新添接口setRoleLevelUpTime
	 * 3.对支付接口
	 */

	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		U8SDK.getInstance().onAppCreate((Application) context);
	}
	
	@Override
	public void doApplicationAttach(Context base) {
		super.doApplicationAttach(base);
		MultiDex.install(base.getApplicationContext());
		Log.init(base);
		U8SDK.getInstance().onAppAttachBaseContext((Application) base.getApplicationContext(), base);
	}

	@Override
	public void doApplicationConfigurationChanged(Application application,Configuration newConfig) {
		super.doApplicationConfigurationChanged(application,newConfig);
		U8SDK.getInstance().onAppConfigurationChanged(application, newConfig);
	}
	
	@Override
	public void doApplicationTerminate() {
		super.doApplicationTerminate();
		U8SDK.getInstance().onTerminate();
		Log.destory();
	}
	
	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		U8SDK.getInstance().setSDKListener(new MyIU8SDKListener());
		
		U8SDK.getInstance().init(mActivity);
	}
	
	@Override
	public void doLogin() {
		
		U8SDK.getInstance().runOnMainThread(new Runnable() {
			
			@Override
			public void run() {
				U8User.getInstance().login();
			}
		});
	}
	
	@Override
	public void doChannelPay(OrderinfoBean orderInfo) {
		super.doChannelPay(orderInfo);
		mOrderID = orderInfo.getCustomInfo();
		final PayParams payParams = new PayParams();
		payParams.setProductId(mPayInfo.getProductId());
		payParams.setProductName(mPayInfo.getProductName());
		payParams.setProductDesc(mPayInfo.getProductDesc());
		payParams.setPrice(Integer.parseInt(orderInfo.getMoNey()));
		payParams.setRatio(orderInfo.getRatio());
		payParams.setBuyNum(1);
		payParams.setCoinNum(Integer.parseInt(orderInfo.getMoNey()) * Integer.parseInt(orderInfo.getRate()));
		payParams.setServerId(mRoleInfo.getServerId());
		payParams.setServerName(mRoleInfo.getServerName());
		payParams.setRoleId(mRoleInfo.getRoleId());
		payParams.setRoleName(mRoleInfo.getRoleName());
		payParams.setRoleLevel(Integer.parseInt(mRoleInfo.getRoleLevel()));
		payParams.setPayNotifyUrl(orderInfo.getNotifyurl());
		payParams.setVip(mRoleInfo.getVipLevel());
		payParams.setOrderID(orderInfo.getOrderID());
		payParams.setExtension(mRoleInfo.getCustomInfo());
		U8SDK.getInstance().runOnMainThread(new Runnable() {
			@Override
			public void run() {
				U8Pay.getInstance().pay(payParams);
			}
		});
	}
	
//	@Override
//	public void doPay(String payCode) {
//		PayInfo payInfo = ConfigHolder.getPayInfo(mActivity, payCode);
//		final PayParams payParams = new PayParams();
//		payParams.setProductId(payInfo.getProductId());
//		payParams.setProductName(payInfo.getProductName());
//		payParams.setProductDesc(payInfo.getProductDesc());
//		payParams.setPrice(1);
//		payParams.setRatio(1);
//		payParams.setBuyNum(1);
//		payParams.setCoinNum(1);
//		payParams.setServerId(mRoleInfo.getServerId());
//		payParams.setServerName(mRoleInfo.getServerName());
//		payParams.setRoleId(mRoleInfo.getRoleId());
//		payParams.setRoleName(mRoleInfo.getRoleName());
//		payParams.setRoleLevel(80);
//		payParams.setPayNotifyUrl("");
//		payParams.setVip(mRoleInfo.getVipLevel());
//		payParams.setOrderID("1089493019557625916");
//		payParams.setExtension(mRoleInfo.getCustomInfo());
//	}
	
	@Override
	public void doLogout() {
//		super.doLogout();
//		U8SDK.getInstance().runOnMainThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				U8User.getInstance().logout();
//			}
//		});
		U8SDK.getInstance().runOnMainThread(new Runnable() {
			
			@Override
			public void run() {
				U8User.getInstance().switchLogin();
			}
		});
	}
	
	@Override
	public void doExitGame() {
//		super.doExitGame();
		U8SDK.getInstance().runOnMainThread(new Runnable() {
			
			@Override
			public void run() {
				U8User.getInstance().exit();
			}
		});
	}
	
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		
		U8SDK.getInstance().runOnMainThread(new Runnable() {
			
			@Override 
			public void run() {
				submitInfo(UserExtraData.TYPE_SELECT_SERVER);
				submitInfo(UserExtraData.TYPE_CREATE_ROLE); 
				submitInfo(UserExtraData.TYPE_LEVEL_UP);
				submitInfo(UserExtraData.TYPE_EXIT_GAME);
			}
		});
	}
	
	@Override
	public void entryGame() {
		submitInfo(UserExtraData.TYPE_ENTER_GAME);
	}
	
	private void submitInfo(final int type) {
		HttpUtils.post(mActivity, URLHolder.GET_TIME, null, new HttpCallback() {
			@Override
			public void onSuccess(String data) {
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONObject result = jsonObject.getJSONObject("result");
					JSONObject channelInfo = result.getJSONObject("channelinfo");
					String timeStr = channelInfo.getString("timestr");
					String dataStr = channelInfo.getString("datastr");
					
					UserExtraData extraData = new UserExtraData();
					extraData.setDataType(type);
					extraData.setServerID(Integer.parseInt(mRoleInfo.getServerId()));
					extraData.setServerName(mRoleInfo.getServerName());
					extraData.setRoleID(mRoleInfo.getRoleId());
					extraData.setRoleName(mRoleInfo.getRoleName());
					extraData.setRoleLevel(mRoleInfo.getRoleLevel());
					extraData.setMoneyNum(0);
					extraData.setRoleCreateTime(Long.parseLong(mRoleInfo.getCreateTime()));
					extraData.setRoleLevelUpTime(Long.parseLong(timeStr));
					extraData.setVip(mRoleInfo.getVipLevel());
					U8User.getInstance().submitExtraData(extraData);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailed(String code) {
				LogUtils.d("get time failed");
			}
		});
	}
	
	private class MyIU8SDKListener implements IU8SDKListener {
		
		@Override
		public void onResult(int code, String msg) {
			LogUtils.d("onResult code= "+code+",msg= "+msg);
			switch (code) {
			case U8Code.CODE_INIT_SUCCESS:
				mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
				break;
				
			case U8Code.CODE_LOGIN_FAIL:
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
				break;
				
			case U8Code.CODE_LOGOUT_SUCCESS:
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "用户注销");
				break;
				
			case U8Code.CODE_PAY_FAIL:
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
				break;
				
			case U8Code.CODE_PAY_SUCCESS:
				if (mOrderID !=null) checkOrder(mOrderID);
				break;
			}
		}

		@Override
		public void onAuthResult(UToken uToken) {
			LogUtils.d("aipu sdk onAuthResult-----");
			int userID = uToken.getUserID();
			String token = uToken.getToken();
			checkLogin(userID+"", token);
		}

		@Override
		public void onInitResult(InitResult initResult) {
			LogUtils.d("aipu sdk initResult------");
		}

		@Override
		public void onLoginResult(String msg) {
			LogUtils.d("aipu sdk onLoginResult------"+msg);
		}

		@Override
		public void onLogout() {
			LogUtils.d("aipu sdk onLogout------");
		}

		@Override
		public void onPayResult(PayResult payToken) {
			LogUtils.d("aipu sdk onPayResult------");
//			if (mOrderID != null) checkOrder(mOrderID);
		}

		@Override
		public void onSwitchAccount() {
			LogUtils.d("aipu sdk onSwitchAccount-----");
		}

		@Override
		public void onSwitchAccount(String msg) {
			LogUtils.d("aipu sdk onSwitchAccount-----"+msg);
		}
	}
	
	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) {
		super.doActivityResult(requestCode, resultCode, data);
		U8SDK.getInstance().onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void doStart() {
		super.doStart();
		U8SDK.getInstance().onStart();
	}
	
	@Override
	public void doPause() {
		super.doPause();
		U8SDK.getInstance().onPause();
	}
	
	@Override
	public void doResume() {
		super.doResume();
		U8SDK.getInstance().onResume();
	}
	
	@Override
	public void doNewIntent(Intent intent) {
		super.doNewIntent(intent);
		U8SDK.getInstance().onNewIntent(intent);
	}
	
	@Override
	public void doStop() {
		super.doStop();
		U8SDK.getInstance().onStop();
	}
	
	@Override
	public void doDestory() {
		super.doDestory();
		U8SDK.getInstance().onDestroy();
	}
	
	@Override
	public void doRestart() {
		super.doRestart();
		U8SDK.getInstance().onRestart();
	}
}
