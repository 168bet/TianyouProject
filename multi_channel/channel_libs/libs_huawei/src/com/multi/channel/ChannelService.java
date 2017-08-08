package com.multi.channel;

import com.huawei.gameservice.sdk.GameServiceSDK;
import com.huawei.gameservice.sdk.control.GameEventHandler;
import com.huawei.gameservice.sdk.model.Result;
import com.huawei.gameservice.sdk.model.UserResult;
import com.huawei.gb.huawei.GlobalParam;
import com.huawei.gb.huawei.RSAUtil;
import com.huawei.gb.huawei.net.ReqTask;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ToastUtils;

import android.app.Activity;

public class ChannelService extends BaseSdkService{
	
	// 日志标签
	// definition the log tag
	public static final String TAG = "MainActivity";

	// 返回键是否可用
	// Identifies the back key is available
//	private static boolean isBackKeyEnable = true;

//	private Button start;
//
//	private Button getPlayerLevel;
//
//	private Button addPlayer;
//
//	private Button login;

//	private Handler uiHandler = null;
//
//	private int RELOAD_BUTTON = 1;
//	
//	private int HIDE_BUTTON = 2;

	private String buoyPrivateKey = null;
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
//		uiHandler = new Handler(mActivity.getMainLooper()) {
//			@Override
//			public void handleMessage(Message msg) {
//				if (msg.getData() == null) {
//					return;
//				}
//				if (msg.what == RELOAD_BUTTON) {
//					start.setVisibility(View.VISIBLE);
//					getPlayerLevel.setVisibility(View.VISIBLE);
//					addPlayer.setVisibility(View.VISIBLE);
//					login.setVisibility(View.GONE);
//				}else if(msg.what==HIDE_BUTTON)
//				{
//					start.setVisibility(View.GONE);
//					getPlayerLevel.setVisibility(View.GONE);
//					addPlayer.setVisibility(View.GONE);
//					login.setVisibility(View.VISIBLE);
//				}
//				String errorMsg = msg.getData().getString("errorMsg");
//				if (!StringUtil.isNull(errorMsg)) {
//                    LogUtil.i(TAG, errorMsg);
//				}
//			}
//		};

		// 为了安全把浮标密钥放到服务端，并使用https的方式获取下来存储到内存中，CP可以使用自己的安全方式处理
		// For safety, buoy key put into the server and use the https way to get
		// down into the client's memory.
		// By the way CP can also use their safe approach.
		ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate() {

			@Override
			public void execute(String result) {
				/**
				 * 从服务端获取的浮标私钥，由于没有部署最终的服务端，所以返回值写死一个值，CP需要从服务端获取，服务端代码参考华为Demo
				 * The demo app did not deployed the server, so the return value
				 * is written fixed.For real app,the CP need to get the buoy key
				 * from server.
				 */
				buoyPrivateKey = GlobalParam.BUOY_SECRET;

				// SDK初始化
				// SDK initialization
				init();
			}
		}, null, GlobalParam.GET_BUOY_PRIVATE_KEY);
		getBuoyPrivate.execute();
	}
	
	/**
	 * 初始化 initialization
	 */
	private void init() {
		GameServiceSDK.init(mActivity, GlobalParam.APP_ID, GlobalParam.PAY_ID,
				"com.huawei.gb.huawei.installnewtype.provider", new GameEventHandler() {

					@Override
					public void onResult(Result result) {
						if (result.rtnCode != Result.RESULT_OK) {
							ToastUtils.show(mActivity, "init the game service SDK failed:");
//							handleError("init the game service SDK failed:" + result.rtnCode);
							return;
						}
						login(1);
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

		// 为了安全把浮标密钥放到服务端，并使用https的方式获取下来存储到内存中，CP可以使用自己的安全方式处理
		// For safety, buoy key put into the server and use the https way to get
		// down into the client's memory.
		// By the way CP can also use their safe approach.

		String str = data;
		try {
			String result = RSAUtil.sha256WithRsa(str.getBytes("UTF-8"), buoyPrivateKey);
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
//					handleError("check update failed:" + result.rtnCode);
				}
			}

			@Override
			public String getGameSign(String appId, String cpId, String ts) {
				return null;
			}

		});
	}
	
	/**
	 * 帐号登录 Login
	 */
	private void login(int authType) {
		GameServiceSDK.login(mActivity, new GameEventHandler() {

			@Override
			public void onResult(Result result) {
				if (result.rtnCode != Result.RESULT_OK) {
//					handleError("login failed:" + result.toString());
					doNoticeGame(TianyouCallback.CODE_LOGIN_FAILED, "");
//					uiHandler.sendMessage(uiHandler.obtainMessage(HIDE_BUTTON));
				} else {
					UserResult userResult = (UserResult) result;
					if (userResult.isAuth != null && userResult.isAuth == 1) {
						boolean checkResult = checkSign(GlobalParam.APP_ID + userResult.ts + userResult.playerId,
								userResult.gameAuthSign);
						if (checkResult) {
//							handleError("login auth success:" + userResult.toString());
							mLoginInfo.setChannelUserId(userResult.playerId);
							checkLogin();
//							uiHandler.sendMessage(uiHandler.obtainMessage(RELOAD_BUTTON));
						} else {
							doNoticeGame(TianyouCallback.CODE_LOGIN_FAILED, "");
//							uiHandler.sendMessage(uiHandler.obtainMessage(HIDE_BUTTON));
//							handleError("login auth failed check game auth sign error");
						}

					} else if (userResult.isChange != null && userResult.isChange == 1) {
						login(1);
					} else {
						mLoginInfo.setChannelUserId(userResult.playerId);
						checkLogin();
//						handleError("login success:" + userResult.toString());
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
			return RSAUtil.verify(data.getBytes("UTF-8"), GlobalParam.LOGIN_RSA_PUBLIC, gameAuthSign);
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		login(1);
	}
}