package com.multi.channel;

import java.util.HashMap;
import java.util.Map;

import com.multi.channel.account.AccountInfo;
import com.multi.channel.util.APNUtil;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import cn.uc.gamesdk.UCGameSdk;
import cn.uc.gamesdk.even.SDKEventKey;
import cn.uc.gamesdk.even.SDKEventReceiver;
import cn.uc.gamesdk.even.Subscribe;
import cn.uc.gamesdk.exception.AliLackActivityException;
import cn.uc.gamesdk.exception.AliNotInitException;
import cn.uc.gamesdk.open.GameParamInfo;
import cn.uc.gamesdk.open.OrderInfo;
import cn.uc.gamesdk.open.UCCallbackListener;
import cn.uc.gamesdk.open.UCOrientation;
import cn.uc.gamesdk.param.SDKParamKey;
import cn.uc.gamesdk.param.SDKParams;

public class ChannelService extends BaseSdkService {

	private Handler handler;

	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		ucNetworkAndInitUCGameSDK(getPullupInfo(mActivity.getIntent()));
		handler = new Handler(Looper.getMainLooper());
		UCGameSdk.defaultSdk().registerSDKEventReceiver(receiver);
	}

	public void ucNetworkAndInitUCGameSDK(String pullUpInfo) {
		// !!!在调用SDK初始化前进行网络检查
		// 当前没有拥有网络
		if (false == APNUtil.isNetworkAvailable(mActivity)) {
			AlertDialog.Builder ab = new AlertDialog.Builder(mActivity);
			ab.setMessage("网络未连接,请设置网络");
			ab.setPositiveButton("设置", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent("android.settings.SETTINGS");
					mActivity.startActivityForResult(intent, 0);
				}
			});
			ab.setNegativeButton("退出", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					System.exit(0);
				}
			});
			ab.show();
		} else {
			ucSdkInit(pullUpInfo);// 执行UCGameSDK初始化
		}
	}
	
	private void ucSdkInit(String pullUpInfo) {
        GameParamInfo gameParamInfo = new GameParamInfo();
        //gameParamInfo.setCpId(UCSdkConfig.cpId);已废用
//        UCSdkConfig.gameId = Integer.parseInt(mChannelInfo.getAppId());
        gameParamInfo.setGameId(Integer.parseInt(mChannelInfo.getAppId()));
        //gameParamInfo.setServerId(UCSdkConfig.serverId);已废用
        gameParamInfo.setOrientation(UCOrientation.PORTRAIT);

        SDKParams sdkParams = new SDKParams();

        sdkParams.put(SDKParamKey.GAME_PARAMS, gameParamInfo);
        sdkParams.put(SDKParamKey.PULLUP_INFO,pullUpInfo);


        //联调环境已经废用
        //  sdkParams.put(SDKParamKey.DEBUG_MODE, UCSdkConfig.debugMode);

        try {
            UCGameSdk.defaultSdk().initSdk(mActivity, sdkParams);
        } catch (AliLackActivityException e) {
            e.printStackTrace();
        }
    }

	private String getPullupInfo(Intent intent) {
		if (intent == null) {
			return null;
		}
		String pullupInfo = intent.getDataString();
		return pullupInfo;
	}

	UCCallbackListener<String> logoutListener = new UCCallbackListener<String>() {
		@Override
		public void callback(int statuscode, String data) {
			Log.d("TAG", "退出账号: statuscode= " + statuscode + ",data= " + data);
			doNoticeGame(TianyouCallback.CODE_LOGOUT, "");
		}
	};
	
	@Override
	public void doLogin() {
		super.doLogin();
		try {
            UCGameSdk.defaultSdk().login(mActivity, null);
        } catch (AliLackActivityException e) {
            e.printStackTrace();
        } catch (AliNotInitException e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(SDKParamKey.CALLBACK_INFO, payInfo.getCustomInfo());
        paramMap.put(SDKParamKey.NOTIFY_URL, orderInfo.getNotifyurl());
        paramMap.put(SDKParamKey.AMOUNT, orderInfo.getMoNey());
        paramMap.put(SDKParamKey.CP_ORDER_ID, orderInfo.getOrderID());
        paramMap.put(SDKParamKey.ACCOUNT_ID, mLoginInfo.getChannelUserId());
        paramMap.put(SDKParamKey.SIGN_TYPE, "MD5");

        SDKParams sdkParams = new SDKParams();

        Map<String, Object> map = new HashMap<String, Object>();
        map.putAll(paramMap);
        sdkParams.putAll(map);

        sdkParams.put(SDKParamKey.SIGN, orderInfo.getSign());
        System.out.println("sdkParams:"+sdkParams.toString());
        try {
            UCGameSdk.defaultSdk().pay(mActivity, sdkParams);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mActivity, "charge failed - Exception: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
	}
	
	private void dumpOrderInfo(OrderInfo orderInfo) {
        if (orderInfo != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("'orderId':'%s'", orderInfo.getOrderId()));
            sb.append(String.format("'orderAmount':'%s'", orderInfo.getOrderAmount()));
            sb.append(String.format("'payWay':'%s'", orderInfo.getPayWay()));
            sb.append(String.format("'payWayName':'%s'", orderInfo.getPayWayName()));

//            Log.i(TAG, "callback orderInfo = " + sb);
        }
    }
	
	@Override
	public void doExitGame() {
//		super.doExitGame();
		try {
            UCGameSdk.defaultSdk().exit(mActivity, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public void doDestory() {
		super.doDestory();
		UCGameSdk.defaultSdk().unregisterSDKEventReceiver(receiver);
	}
	
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		submitRoleData();
	}
	
	@Override
	public void doEntryGame() {
		super.doEntryGame();
		submitRoleData();
	}
	
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		submitRoleData();
	}

	private void submitRoleData() {
		SDKParams sdkParams = new SDKParams();
        sdkParams.put(SDKParamKey.STRING_ROLE_ID, mRoleInfo.getRoleId());
        sdkParams.put(SDKParamKey.STRING_ROLE_NAME, mRoleInfo.getRoleName());
        sdkParams.put(SDKParamKey.LONG_ROLE_LEVEL, mRoleInfo.getRoleLevel());
        sdkParams.put(SDKParamKey.LONG_ROLE_CTIME, mRoleInfo.getCreateTime());
        sdkParams.put(SDKParamKey.STRING_ZONE_ID, mRoleInfo.getServerId());
        sdkParams.put(SDKParamKey.STRING_ZONE_NAME, mRoleInfo.getServerName());
        LogUtils.d("sdkParams:" + sdkParams.toString());
        try {
            UCGameSdk.defaultSdk().submitRoleData(mActivity, sdkParams);
            LogUtils.d("sdkParams:doUpdateRoleInfo");
        } catch (AliNotInitException e) {
        	LogUtils.w("submitRoleData:AliNotInitException");
            e.printStackTrace();
        } catch (AliLackActivityException e) {
        	LogUtils.w("submitRoleData:AliLackActivityException");
            e.printStackTrace();
        }
	}

	SDKEventReceiver receiver = new SDKEventReceiver() {
		@Subscribe(event = SDKEventKey.ON_INIT_SUCC)
		private void onInitSucc() {
			// 初始化成功
			handler.post(new Runnable() {
				@Override
				public void run() {
					doNoticeGame(TianyouCallback.CODE_INIT, "");
				}
			});
		}

		@Subscribe(event = SDKEventKey.ON_INIT_FAILED)
		private void onInitFailed(String data) {
			// 初始化失败
			Toast.makeText(mActivity, "init failed", Toast.LENGTH_SHORT).show();
			ucNetworkAndInitUCGameSDK(null);
		}

		@Subscribe(event = SDKEventKey.ON_LOGIN_SUCC)
		private void onLoginSucc(String sid) {
			mLoginInfo.setUserToken(sid);
			checkLogin();
			AccountInfo.instance().setSid(sid);
			handler.post(new Runnable() {

				@Override
				public void run() {
//					paintGame();
				}
			});

		}

		@Subscribe(event = SDKEventKey.ON_LOGIN_FAILED)
		private void onLoginFailed(String desc) {
			doNoticeGame(TianyouCallback.CODE_LOGIN_FAILED, "");
//			Toast.makeText(mActivity, desc, Toast.LENGTH_SHORT).show();
			// printMsg(desc);
		}

		@Subscribe(event = SDKEventKey.ON_CREATE_ORDER_SUCC)
		private void onCreateOrderSucc(OrderInfo orderInfo) {
			dumpOrderInfo(orderInfo);
			if (orderInfo != null) {
				String txt = orderInfo.getOrderAmount() + "," + orderInfo.getOrderId() + "," + orderInfo.getPayWay();
				Toast.makeText(mActivity, "订单已生成，获取支付结果请留意服务端回调" + txt, Toast.LENGTH_SHORT).show();
			}
//			Log.i(TAG, "pay create succ");
		}

		@Subscribe(event = SDKEventKey.ON_PAY_USER_EXIT)
		private void onPayUserExit(OrderInfo orderInfo) {
			dumpOrderInfo(orderInfo);
			if (orderInfo != null) {
				String txt = orderInfo.getOrderAmount() + "," + orderInfo.getOrderId() + "," + orderInfo.getPayWay();
				Toast.makeText(mActivity, "支付界面关闭" + txt, Toast.LENGTH_SHORT).show();
			}
//			Log.i(TAG, "pay exit");
		}

		@Subscribe(event = SDKEventKey.ON_LOGOUT_SUCC)
		private void onLogoutSucc() {
			doNoticeGame(TianyouCallback.CODE_LOGOUT, "");
//			Toast.makeText(GameActivity.this, "logout succ", Toast.LENGTH_SHORT).show();
			AccountInfo.instance().setSid("");
			doLogin();
		}

		@Subscribe(event = SDKEventKey.ON_LOGOUT_FAILED)
		private void onLogoutFailed() {
//			Toast.makeText(GameActivity.this, "logout failed", Toast.LENGTH_SHORT).show();
			// printMsg("注销失败");
		}

		@Subscribe(event = SDKEventKey.ON_EXIT_SUCC)
		private void onExit(String desc) {
//			Toast.makeText(GameActivity.this, desc, Toast.LENGTH_SHORT).show();
			doNoticeGame(TianyouCallback.CODE_QUIT_SUCCESS, "");  
//			mActivity.finish();

			// 退出程序
			// Intent intent = new Intent(Intent.ACTION_MAIN);
			// intent.addCategory(Intent.CATEGORY_HOME);
			// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intent);
//			android.os.Process.killProcess(android.os.Process.myPid());
			// printMsg(desc);
		}

		@Subscribe(event = SDKEventKey.ON_EXIT_CANCELED)
		private void onExitCanceled(String desc) {
			doNoticeGame(TianyouCallback.CODE_QUIT_CANCEL, "");
//			Toast.makeText(GameActivity.this, desc, Toast.LENGTH_SHORT).show();
		}

	};
}